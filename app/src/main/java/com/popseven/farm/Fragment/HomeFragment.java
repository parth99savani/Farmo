package com.popseven.farm.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.popseven.farm.Common.Common;
import com.popseven.farm.FarmListActivity;
import com.popseven.farm.Model.OfferModel;
import com.popseven.farm.R;
import com.popseven.farm.SearchActivity;
import com.popseven.farm.ViewHolder.OfferViewHolder;
import com.savvi.rangedatepicker.CalendarPickerView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements NumberPicker.OnValueChangeListener{

    private LinearLayout btnNumberPicker,btnSearchbar,layoutCheckIn,layoutCheckOut;
    private TextView txtNoOfGuest,txtLocation,txtDateCheckIn,txtDateCheckOut,txtNightCount;
    private List<Date> selectedDateRange;
    private Calendar tommorowDate,todayDate;
    private Button btnFind;
    private RecyclerView recyclerViewOffers;
    private LinearLayoutManager linearLayoutManager;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore database;
    private CollectionReference offerRef;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        txtLocation = (TextView)view.findViewById(R.id.txtSearchLocation);
        btnSearchbar = (LinearLayout)view.findViewById(R.id.btnSearchbar);
        btnNumberPicker = (LinearLayout)view.findViewById(R.id.btnNumberPicker);
        txtNoOfGuest = (TextView)view.findViewById(R.id.txtNoOfGuest);
        layoutCheckIn = (LinearLayout)view.findViewById(R.id.layoutCheckIn);
        layoutCheckOut = (LinearLayout)view.findViewById(R.id.layoutCheckOut);
        txtDateCheckIn = (TextView)view.findViewById(R.id.txtDateCheckIn);
        txtDateCheckOut = (TextView)view.findViewById(R.id.txtDateCheckOut);
        txtNightCount = (TextView)view.findViewById(R.id.txtNightCount);
        btnFind = (Button)view.findViewById(R.id.btn_find);
        recyclerViewOffers = (RecyclerView)view.findViewById(R.id.recyclerview_offers);

        Common.AMENITIES_LIST_FILTER = null;
        Common.DISCOUNT_FILTER = null;
        Common.RATING_FILTER = null;
        Common.MIN_PRICE_FILTER = null;
        Common.MAX_PRICE_FILTER = null;

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewOffers.setLayoutManager(linearLayoutManager);

        database = FirebaseFirestore.getInstance();
        offerRef = database.collection("offers");

        FirestoreRecyclerOptions<OfferModel> response = new FirestoreRecyclerOptions.Builder<OfferModel>()
                .setQuery(offerRef, OfferModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<OfferModel, OfferViewHolder>(response) {
            @Override
            public void onBindViewHolder(OfferViewHolder holder, final int position, final OfferModel model) {

                Picasso.get().load(model.getOfferImageUrl()).into(holder.imageOffer);

            }

            @Override
            public OfferViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_offer, group, false);

                return new OfferViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        recyclerViewOffers.setAdapter(adapter);

        todayDate = Calendar.getInstance();
        tommorowDate = Calendar.getInstance();
        tommorowDate.add(Calendar.DATE,1);

        if(Common.NO_OF_GUEST!=null){
            txtNoOfGuest.setText(Common.NO_OF_GUEST);
        }

        if(Common.DATE_RANGE!=null){
            setDateText(Common.DATE_RANGE);
            selectedDateRange = Common.DATE_RANGE;
        }else {
            ArrayList<Date> dateRangeList = new ArrayList<>();
            dateRangeList.add(todayDate.getTime());
            dateRangeList.add(tommorowDate.getTime());
            setDateText(dateRangeList);
            selectedDateRange=dateRangeList;
        }


        if(Common.LOCATION_CITY!=null){
            txtLocation.setText(Common.LOCATION_CITY+","+Common.LOCATION_STATE);
        }

        btnSearchbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSearch = new Intent(getActivity(), SearchActivity.class);
                startActivity(intentSearch);
            }
        });

        btnNumberPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialogNumberPicker = new Dialog(getContext());
                dialogNumberPicker.setContentView(R.layout.dialog_number_picker);
                Button b1 = (Button) dialogNumberPicker.findViewById(R.id.btnApply);
                final NumberPicker np = (NumberPicker) dialogNumberPicker.findViewById(R.id.numberPicker1);
                np.setMaxValue(100);
                np.setMinValue(1);
                np.setWrapSelectorWheel(true);
                b1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        txtNoOfGuest.setText(String.valueOf(np.getValue()));
                        dialogNumberPicker.dismiss();
                    }
                });
                dialogNumberPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogNumberPicker.show();

            }
        });

        layoutCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickDialog();

            }
        });

        layoutCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickDialog();

            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.LOCATION_CITY!=null){
                    Intent farmListIntent = new Intent(getActivity(), FarmListActivity.class);
                    Common.DATE_RANGE = selectedDateRange;
                    Common.NO_OF_GUEST = txtNoOfGuest.getText().toString();
                    Common.NO_OF_NIGHTS=Common.DATE_RANGE.size()-1;
                    startActivity(farmListIntent);
                }else {
                    Toast.makeText(getActivity(), "select City/Location", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

   private void datePickDialog(){

       final Dialog dialogDatePick = new Dialog(getContext());
       dialogDatePick.setContentView(R.layout.dialog_date_pick);
       Calendar startMonth = Calendar.getInstance();
       Calendar endMonth = Calendar.getInstance();
       endMonth.add(Calendar.MONTH, 12);
       ArrayList<Date> selectedDates = new ArrayList<>();
       selectedDates.add(todayDate.getTime());
       selectedDates.add(tommorowDate.getTime());
       Button btnDateApply = (Button) dialogDatePick.findViewById(R.id.btnDateApply);
       final CalendarPickerView calendarPickerView = (CalendarPickerView)dialogDatePick.findViewById(R.id.calendarDatePick);
       calendarPickerView.init(startMonth.getTime(),endMonth.getTime(),new SimpleDateFormat("MMMM,yyyy", Locale.getDefault()))
               .inMode(CalendarPickerView.SelectionMode.RANGE)
               .withSelectedDates(selectedDates);

       dialogDatePick.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       dialogDatePick.show();
       btnDateApply.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               selectedDateRange = calendarPickerView.getSelectedDates();
               setDateText(selectedDateRange);
               dialogDatePick.dismiss();
           }
       });
   }

    private void setDateText(List<Date> dateRange){

        if(dateRange.get(0).equals(todayDate.getTime())){
            txtDateCheckIn.setText("Today");
        }else {
            txtDateCheckIn.setText(getDateInText(dateRange.get(0).getDay(),dateRange.get(0).getMonth(),dateRange.get(0).getDate()));
        }

        if (dateRange.get(dateRange.size()-1).equals(tommorowDate.getTime())){
            txtDateCheckOut.setText("Tommorow");
        }else {
            txtDateCheckOut.setText(getDateInText(dateRange.get(dateRange.size()-1).getDay(),dateRange.get(dateRange.size()-1).getMonth(),dateRange.get(dateRange.size()-1).getDate()));
        }

        txtNightCount.setText(dateRange.size()-1+"N");

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


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

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

        Common.AMENITIES_LIST_FILTER = null;
        Common.DISCOUNT_FILTER = null;
        Common.RATING_FILTER = null;
        Common.MIN_PRICE_FILTER = null;
        Common.MAX_PRICE_FILTER = null;

        if(Common.NO_OF_GUEST!=null){
            txtNoOfGuest.setText(Common.NO_OF_GUEST);
        }

        if(Common.DATE_RANGE!=null){
            setDateText(Common.DATE_RANGE);
            selectedDateRange = Common.DATE_RANGE;
        }else {

            ArrayList<Date> dateRangeList = new ArrayList<>();
            dateRangeList.add(todayDate.getTime());
            dateRangeList.add(tommorowDate.getTime());
            setDateText(dateRangeList);
            selectedDateRange=dateRangeList;
        }


        if(Common.LOCATION_CITY != null){
            txtLocation.setText(Common.LOCATION_CITY/*+","+Common.LOCATION_STATE*/);
        }
    }
}
