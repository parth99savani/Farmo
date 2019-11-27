package com.popseven.farm.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.popseven.farm.BookingDetailsActivity;
import com.popseven.farm.Common.Common;
import com.popseven.farm.Model.BookingModel;
import com.popseven.farm.Model.FarmModel;
import com.popseven.farm.R;
import com.popseven.farm.ViewHolder.BookingViewHolder;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingFragment extends Fragment {

    private RecyclerView recyclerviewBooking;
    private ProgressBar progressbarBooking;
    private FirebaseFirestore database;
    private FirestoreRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private Query queryBooking;
    private String userId;
    private SharedPreferences sp;
    private Calendar tommorowDate,todayDate;
    public static final String TAG = "BookingActivity";

    public BookingFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        recyclerviewBooking = view.findViewById(R.id.recyclerview_booking);
        progressbarBooking = view.findViewById(R.id.progressbarBooking);

        todayDate = Calendar.getInstance();
        tommorowDate = Calendar.getInstance();
        tommorowDate.add(Calendar.DATE,1);

        sp = getActivity().getSharedPreferences("Login",MODE_PRIVATE);
        userId=sp.getString("Uid",null);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerviewBooking.setLayoutManager(linearLayoutManager);
        database = FirebaseFirestore.getInstance();
        queryBooking = database.collection("user").document(userId).collection("bookedFarms");
        getFarmList(queryBooking);

        return view;
    }

    void getFarmList(Query query) {

        FirestoreRecyclerOptions<BookingModel> response = new FirestoreRecyclerOptions.Builder<BookingModel>()
                .setQuery(query, BookingModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<BookingModel, BookingViewHolder>(response) {
            @Override
            public void onBindViewHolder(final BookingViewHolder holder, final int position, final BookingModel model) {
                progressbarBooking.setVisibility(View.GONE);
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                final String docId = snapshot.getId();

                DocumentReference docRef = database.collection("farm").document(model.getFarmId());
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        FarmModel farmModel = documentSnapshot.toObject(FarmModel.class);
                        Date date = new Date();
                        holder.txtBookingIdBooking.setText(docId);
                        holder.txtAmountPaidBooking.setText("₹"+model.getAmountPaid());
                        holder.txtGuestBooking.setText(model.getGuest()+" Guests");
                        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
                        try {
                            date = format.parse(model.getBookedDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        holder.txtDateTimeBooking.setText("Booked on "+getDateInText(date.getDay(),date.getMonth(),date.getDate())+".");
                        holder.txtNameBooking.setText(farmModel.getName());
                        holder.txtAddressBooking.setText(farmModel.getStreetAddress()+","+farmModel.getCity()+"-"+farmModel.getZip()+","+farmModel.getState());
                        Picasso.get().load(farmModel.getImageUrl().get(0)).into(holder.imageBooking);
                        holder.txtTimeCheckInBooking.setText(farmModel.getCheckInTime());
                        holder.txtTimeCheckOutBooking.setText(farmModel.getCheckOutTime());
                        if(getStringToDate(model.getBookedDates()).get(0).equals(todayDate.getTime())){
                            holder.txtDateCheckInBooking.setText("Today");
                        }else {
                            holder.txtDateCheckInBooking.setText(getDateInText(getStringToDate(model.getBookedDates()).get(0).getDay(),getStringToDate(model.getBookedDates()).get(0).getMonth(),getStringToDate(model.getBookedDates()).get(0).getDate()));
                        }

                        if (getStringToDate(model.getBookedDates()).get(getStringToDate(model.getBookedDates()).size()-1).equals(tommorowDate.getTime())){
                            holder.txtDateCheckOutBooking.setText("Tommorow");
                        }else {
                            holder.txtDateCheckOutBooking.setText(getDateInText(getStringToDate(model.getBookedDates()).get(getStringToDate(model.getBookedDates()).size()-1).getDay(),getStringToDate(model.getBookedDates()).get(getStringToDate(model.getBookedDates()).size()-1).getMonth(),getStringToDate(model.getBookedDates()).get(getStringToDate(model.getBookedDates()).size()-1).getDate()));
                        }

                        holder.txtNightCountBooking.setText(getStringToDate(model.getBookedDates()).size()-1+"N");

                        if(model.getCancelStatus()==true){
                            holder.layoutHader.setBackgroundColor(Color.parseColor("#ff0000"));
                            holder.txtStatusBooking.setText("Cancel");
                            holder.btnRateNowBooking.setVisibility(View.GONE);
                        }else {
                            if(getStringToDate(model.getBookedDates()).get(getStringToDate(model.getBookedDates()).size()-1).before(todayDate.getTime())){
                                //holder.layoutHader.setBackground(ContextCompat.getDrawable(getActivity(), R.color.colorPrimary));
                                holder.layoutHader.setBackgroundColor(Color.parseColor("#2F60E7"));
                                holder.txtStatusBooking.setText("Past");
                                if(model.getRatingStatus()==true){
                                    holder.btnRateNowBooking.setVisibility(View.GONE);
                                }
                            }else {
                                holder.layoutHader.setBackgroundColor(Color.parseColor("#2DBE04"));
                                holder.txtStatusBooking.setText("Upcoming");
                                holder.btnRateNowBooking.setVisibility(View.GONE);
                            }
                        }


                    }
                });

                holder.btnRateNowBooking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialogRating = new Dialog(getContext());
                        dialogRating.setContentView(R.layout.dialog_rating);
                        Button btnSubmitRating = (Button) dialogRating.findViewById(R.id.btnSubmitRating);
                        Button btnLaterRating = (Button) dialogRating.findViewById(R.id.btnLaterRating);
                        final RatingBar ratingBar = (RatingBar) dialogRating.findViewById(R.id.ratingBar);
                        btnLaterRating.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogRating.dismiss();
                            }
                        });
                        btnSubmitRating.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DocumentReference ratingRef = database.collection("farm").document(model.getFarmId());
                                ratingRef.update("rating", FieldValue.arrayUnion(ratingBar.getRating()));

                                final DocumentReference docRef = database.collection("farm").document(model.getFarmId());
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        FarmModel farmModel = documentSnapshot.toObject(FarmModel.class);
                                        float rating = 0;
                                        int i;
                                        for (i = 0; i < farmModel.getRating().size(); i++) {
                                            rating = rating + farmModel.getRating().get(i);
                                        }
                                        docRef.update("ratingTotal",rating/farmModel.getRating().size());
                                    }
                                });

                                database.collection("user").document(userId).collection("bookedFarms").document(docId)
                                        .update("ratingStatus", true);
                                dialogRating.dismiss();
                            }
                        });
                        dialogRating.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogRating.show();
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.BOOKING_MODEL=model;
                        Intent intentBookingDetails = new Intent(getActivity(), BookingDetailsActivity.class);
                        intentBookingDetails.putExtra("docId", docId);
                        startActivity(intentBookingDetails);
                    }
                });

            }

            @Override
            public BookingViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.booking_item, group, false);

                return new BookingViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        recyclerviewBooking.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.startListening();
    }

    private ArrayList<Date> getStringToDate(ArrayList<String> dateArrayListInString){
        ArrayList<Date> dateArrayListInDate = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        int i;
        try {
            for(i=0;i<dateArrayListInString.size();i++){
                Date date = format.parse(dateArrayListInString.get(i));
                dateArrayListInDate.add(i,date);
            }
        }catch (ParseException e){

        }
        return dateArrayListInDate;
    }

    private String getDateInText(int dayInt,int monthInt,int date){

        String day,month;
        switch (dayInt){
            case 0 :
                day = "Sun";
                break;
            case 1 :
                day = "Mon";
                break;
            case 2 :
                day = "Tue";
                break;
            case 3 :
                day = "Wed";
                break;
            case 4 :
                day = "Thu";
                break;
            case 5 :
                day = "Fri";
                break;
            case 6 :
                day = "Sat";
                break;
            default:
                day = null;
        }

        switch (monthInt){
            case 0 :
                month = "Jan";
                break;
            case 1 :
                month = "Feb";
                break;
            case 2 :
                month = "Mar";
                break;
            case 3 :
                month = "Apr";
                break;
            case 4 :
                month = "May";
                break;
            case 5 :
                month = "Jun";
                break;
            case 6 :
                month = "Jul";
                break;
            case 7 :
                month = "Aug";
                break;
            case 8 :
                month = "Sep";
                break;
            case 9 :
                month = "Oct";
                break;
            case 10 :
                month = "Nov";
                break;
            case 11 :
                month = "Dec";
                break;
            default:
                month = null;
                break;
        }

        return day+", "+date+" "+month;
    }
}
