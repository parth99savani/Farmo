package com.popseven.farm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.popseven.farm.Adapter.ImageFarmAdapter;
import com.popseven.farm.Common.Common;
import com.popseven.farm.Model.FarmModel;
import com.popseven.farm.Model.ImageFarmModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookingDetailsActivity extends AppCompatActivity {

    private TextView txtToolbarTitleDetails;
    private Toolbar toolbarBookingDetails;
    private TextView txtBookingIdDetails;
    private TextView txtBookingDateTimeDetails;
    private RecyclerView recyclerviewImageBookingDetails;
    private ImageFarmAdapter imageFarmAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<ImageFarmModel> imageList = new ArrayList<>();
    private TextView txtFarmNameDetails;
    private ImageView btnShowMapDetails;
    private TextView txtFarmAddressDetails;
    private TextView txtRateDetails;
    private TextView txtRateCountDetails;
    private TextView txtAmountPaidDetails;
    private TextView txtDateCheckInDetails;
    private TextView txtTimeCheckInDetails;
    private TextView txtNightCountDetails;
    private TextView txtDateCheckOutDetails;
    private TextView txtTimeCheckOutDetails;
    private TextView txtGuestDetails;
    private RatingBar ratingBarDetails;
    private Button btnSubmitRatingDetails;
    private CardView cardViewRating;
    private String docId;
    private FirebaseFirestore database;
    private Calendar tommorowDate, todayDate;
    private String userId;
    private SharedPreferences sp;
    private Button btnCancelBooking;
    public static final String TAG = "BookingDetailsActivity";
    private TextView txtCancelStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        database = FirebaseFirestore.getInstance();

        todayDate = Calendar.getInstance();
        tommorowDate = Calendar.getInstance();
        tommorowDate.add(Calendar.DATE, 1);

        sp = this.getSharedPreferences("Login", MODE_PRIVATE);
        userId = sp.getString("Uid", null);

        toolbarBookingDetails = findViewById(R.id.toolbar_booking_details);
        setSupportActionBar(toolbarBookingDetails);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txtToolbarTitleDetails = (TextView) toolbarBookingDetails.findViewById(R.id.txtToolbarTitleDetails);

        txtBookingIdDetails = findViewById(R.id.txtBookingIdDetails);
        txtBookingDateTimeDetails = findViewById(R.id.txtBookingDateTimeDetails);
        recyclerviewImageBookingDetails = findViewById(R.id.recyclerview_imageBookingDetails);
        txtFarmNameDetails = findViewById(R.id.txtFarmNameDetails);
        btnShowMapDetails = findViewById(R.id.btnShowMapDetails);
        txtFarmAddressDetails = findViewById(R.id.txtFarmAddressDetails);
        txtRateDetails = findViewById(R.id.txtRateDetails);
        txtRateCountDetails = findViewById(R.id.txtRateCountDetails);
        txtAmountPaidDetails = findViewById(R.id.txtAmountPaidDetails);
        txtDateCheckInDetails = findViewById(R.id.txtDateCheckInDetails);
        txtTimeCheckInDetails = findViewById(R.id.txtTimeCheckInDetails);
        txtNightCountDetails = findViewById(R.id.txtNightCountDetails);
        txtDateCheckOutDetails = findViewById(R.id.txtDateCheckOutDetails);
        txtTimeCheckOutDetails = findViewById(R.id.txtTimeCheckOutDetails);
        txtGuestDetails = findViewById(R.id.txtGuestDetails);
        ratingBarDetails = findViewById(R.id.ratingBarDetails);
        btnSubmitRatingDetails = findViewById(R.id.btnSubmitRatingDetails);
        cardViewRating = findViewById(R.id.cardViewRating);
        btnCancelBooking = findViewById(R.id.btn_cancelBooking);
        txtCancelStatus = findViewById(R.id.txtCancelStatus);

        Intent intentBookingDetails = getIntent();
        docId = intentBookingDetails.getStringExtra("docId");

        mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerviewImageBookingDetails.setLayoutManager(mLayoutManager);

        txtBookingIdDetails.setText(docId);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        try {
            date = format.parse(Common.BOOKING_MODEL.getBookedDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txtBookingDateTimeDetails.setText(getDateInText(date.getDay(), date.getMonth(), date.getDate()));
        txtAmountPaidDetails.setText("₹" + Common.BOOKING_MODEL.getAmountPaid());
        txtGuestDetails.setText(Common.BOOKING_MODEL.getGuest() + " Guests");

        if (getStringToDate(Common.BOOKING_MODEL.getBookedDates()).get(0).equals(todayDate.getTime())) {
            txtDateCheckInDetails.setText("Today");
        } else {
            txtDateCheckInDetails.setText(getDateInText(getStringToDate(Common.BOOKING_MODEL.getBookedDates()).get(0).getDay(), getStringToDate(Common.BOOKING_MODEL.getBookedDates()).get(0).getMonth(), getStringToDate(Common.BOOKING_MODEL.getBookedDates()).get(0).getDate()));
        }

        if (getStringToDate(Common.BOOKING_MODEL.getBookedDates()).get(getStringToDate(Common.BOOKING_MODEL.getBookedDates()).size() - 1).equals(tommorowDate.getTime())) {
            txtDateCheckOutDetails.setText("Tommorow");
        } else {
            txtDateCheckOutDetails.setText(getDateInText(getStringToDate(Common.BOOKING_MODEL.getBookedDates()).get(getStringToDate(Common.BOOKING_MODEL.getBookedDates()).size() - 1).getDay(), getStringToDate(Common.BOOKING_MODEL.getBookedDates()).get(getStringToDate(Common.BOOKING_MODEL.getBookedDates()).size() - 1).getMonth(), getStringToDate(Common.BOOKING_MODEL.getBookedDates()).get(getStringToDate(Common.BOOKING_MODEL.getBookedDates()).size() - 1).getDate()));
        }
        txtNightCountDetails.setText(getStringToDate(Common.BOOKING_MODEL.getBookedDates()).size() - 1 + "N");

        DocumentReference docRef = database.collection("farm").document(Common.BOOKING_MODEL.getFarmId());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                FarmModel farmModel = documentSnapshot.toObject(FarmModel.class);
                Common.FARM_MODEL = farmModel;

                for (int i = 0; i < farmModel.getImageUrl().size(); i++) {
                    ImageFarmModel imageFarmModel = new ImageFarmModel(farmModel.getImageUrl().get(i));
                    imageList.add(imageFarmModel);
                }

                imageFarmAdapter = new ImageFarmAdapter(imageList);
                recyclerviewImageBookingDetails.setHasFixedSize(true);

                recyclerviewImageBookingDetails.setAdapter(imageFarmAdapter);
                imageFarmAdapter.notifyDataSetChanged();

                txtFarmNameDetails.setText(farmModel.getName());
                txtFarmAddressDetails.setText(farmModel.getStreetAddress() + ", " + farmModel.getCity() + ", " + farmModel.getState());
                float rating = 0;
                int i;
                for (i = 0; i < farmModel.getRating().size(); i++) {
                    rating = rating + farmModel.getRating().get(i);
                }
                txtRateDetails.setText(rating / farmModel.getRating().size() + "/5");
                txtRateCountDetails.setText("(" + farmModel.getRating().size() + ")");
                txtTimeCheckInDetails.setText(farmModel.getCheckInTime());
                txtTimeCheckOutDetails.setText(farmModel.getCheckOutTime());

            }
        });

        btnShowMapDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingDetailsActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        if (Common.BOOKING_MODEL.getCancelStatus() == true) {
            txtCancelStatus.setVisibility(View.VISIBLE);
            cardViewRating.setVisibility(View.GONE);
            btnCancelBooking.setVisibility(View.GONE);
        } else {
            if (getStringToDate(Common.BOOKING_MODEL.getBookedDates()).get(getStringToDate(Common.BOOKING_MODEL.getBookedDates()).size() - 1).before(todayDate.getTime())) {
                if (Common.BOOKING_MODEL.getRatingStatus() == true) {
                    cardViewRating.setVisibility(View.GONE);
                }
            } else {
                cardViewRating.setVisibility(View.GONE);
            }
        }

        if (getStringToDate(Common.BOOKING_MODEL.getBookedDates()).get(0).before(todayDate.getTime())) {
            btnCancelBooking.setVisibility(View.GONE);
        } else {
            btnCancelBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int i;
                    DocumentReference docRef = database.collection("farm").document(Common.BOOKING_MODEL.getFarmId());
                    for (i = 1; i < Common.BOOKING_MODEL.getBookedDates().size(); i++) {
                        docRef.update("bookedDates", FieldValue.arrayRemove(Common.BOOKING_MODEL.getBookedDates().get(i).toString()));
                    }

                    docRef.update("noOfBooking",Common.FARM_MODEL.getNoOfBooking()-Common.BOOKING_MODEL.getBookedDates().size()+1);

                    database.collection("user").document(userId).collection("bookedFarms").document(docId)
                            .update("cancelStatus", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    Toast.makeText(BookingDetailsActivity.this, "Successfully Cancel your booking.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting document", e);
                                }
                            });
                }
            });
        }

        btnSubmitRatingDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference ratingRef = database.collection("farm").document(Common.BOOKING_MODEL.getFarmId());
                ratingRef.update("rating", FieldValue.arrayUnion(ratingBarDetails.getRating()));

                final DocumentReference docRef = database.collection("farm").document(Common.BOOKING_MODEL.getFarmId());
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
                cardViewRating.setVisibility(View.GONE);
            }
        });

    }

    private ArrayList<Date> getStringToDate(ArrayList<String> dateArrayListInString) {
        ArrayList<Date> dateArrayListInDate = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        int i;
        try {
            for (i = 0; i < dateArrayListInString.size(); i++) {
                Date date = format.parse(dateArrayListInString.get(i));
                dateArrayListInDate.add(i, date);
            }
        } catch (ParseException e) {

        }
        return dateArrayListInDate;
    }

    private String getDateInText(int dayInt, int monthInt, int date) {

        String day, month;
        switch (dayInt) {
            case 0:
                day = "Sun";
                break;
            case 1:
                day = "Mon";
                break;
            case 2:
                day = "Tue";
                break;
            case 3:
                day = "Wed";
                break;
            case 4:
                day = "Thu";
                break;
            case 5:
                day = "Fri";
                break;
            case 6:
                day = "Sat";
                break;
            default:
                day = null;
        }

        switch (monthInt) {
            case 0:
                month = "Jan";
                break;
            case 1:
                month = "Feb";
                break;
            case 2:
                month = "Mar";
                break;
            case 3:
                month = "Apr";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "Jun";
                break;
            case 6:
                month = "Jul";
                break;
            case 7:
                month = "Aug";
                break;
            case 8:
                month = "Sep";
                break;
            case 9:
                month = "Oct";
                break;
            case 10:
                month = "Nov";
                break;
            case 11:
                month = "Dec";
                break;
            default:
                month = null;
                break;
        }

        return day + ", " + date + " " + month;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                break;
        }
        return true;
    }
}
