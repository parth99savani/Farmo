package com.popseven.farm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.popseven.farm.Common.Common;
import com.popseven.farm.Model.BookingModel;
import com.popseven.farm.Model.IdModel;
import com.popseven.farm.Model.UserModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookingSummaryActivity extends AppCompatActivity {

    private Toolbar toolbarSearchFarmList;
    private TextView txtNameBook;
    private TextView txtAddressBook;
    private TextView txtDateCheckInBook;
    private TextView txtTimeCheckInBook;
    private LinearLayout layoutCheckInBook;
    private TextView txtNightCountBook;
    private TextView txtDateCheckOutBook;
    private TextView txtTimeCheckOutBook;
    private LinearLayout layoutCheckOutBook;
    private TextView txtNoOfGuestBook;
    private TextView txtFinalPriceBook;
    private TextView txtPriceFarmBook;
    private TextView txtDisscountBook;
    private TextView txtGuestCapacityBook;
    private TextView txtPriceBook;
    private TextView txtPriceForNightBook;
    private TextView txtDisscountTextPerBook;
    private TextView txtDisscountRupeesBook;
    private TextView txtExtraGuestBook;
    private TextView txtExtraGuestRupeesBook;
    private TextView txtGSTRupeesBook;
    private TextView txtAmountPayableBook;
    private TextView txtPriceButtonBook;
    private TextView btnContinueBook;
    private Calendar tommorowDate,todayDate;
    private int extraGuest=0;
    private TextView mTitle;
    private String userId;
    private SharedPreferences sp;
    private FirebaseFirestore database;
    private String docId;
    private IdModel idModel;
    public static final String TAG = "BookingSummaryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);

        // toolbar
        toolbarSearchFarmList = findViewById(R.id.toolbar_booking_summary);
        setSupportActionBar(toolbarSearchFarmList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mTitle = (TextView) toolbarSearchFarmList.findViewById(R.id.txtToolbarTitle);

        txtNameBook = findViewById(R.id.txtNameBook);
        txtAddressBook = findViewById(R.id.txtAddressBook);
        txtDateCheckInBook = findViewById(R.id.txtDateCheckInBook);
        txtTimeCheckInBook = findViewById(R.id.txtTimeCheckInBook);
        layoutCheckInBook = findViewById(R.id.layoutCheckInBook);
        txtNightCountBook = findViewById(R.id.txtNightCountBook);
        txtDateCheckOutBook = findViewById(R.id.txtDateCheckOutBook);
        txtTimeCheckOutBook = findViewById(R.id.txtTimeCheckOutBook);
        layoutCheckOutBook = findViewById(R.id.layoutCheckOutBook);
        txtNoOfGuestBook = findViewById(R.id.txtNoOfGuestBook);
        txtFinalPriceBook = findViewById(R.id.txtFinalPriceBook);
        txtPriceFarmBook = findViewById(R.id.txtPriceFarmBook);
        txtDisscountBook = findViewById(R.id.txtDisscountBook);
        txtGuestCapacityBook = findViewById(R.id.txtGuestCapacityBook);
        txtPriceBook = findViewById(R.id.txtPriceBook);
        txtPriceForNightBook = findViewById(R.id.txtPriceForNightBook);
        txtDisscountTextPerBook = findViewById(R.id.txtDisscountTextPerBook);
        txtDisscountRupeesBook = findViewById(R.id.txtDisscountRupeesBook);
        txtExtraGuestBook = findViewById(R.id.txtExtraGuestBook);
        txtExtraGuestRupeesBook = findViewById(R.id.txtExtraGuestRupeesBook);
        txtGSTRupeesBook = findViewById(R.id.txtGSTRupeesBook);
        txtAmountPayableBook = findViewById(R.id.txtAmountPayableBook);
        txtPriceButtonBook = findViewById(R.id.txtPriceButtonBook);
        btnContinueBook = findViewById(R.id.btnContinueBook);

        todayDate = Calendar.getInstance();

        sp = getSharedPreferences("Login",MODE_PRIVATE);
        userId=sp.getString("Uid",null);

        Intent intent = getIntent();
        docId = intent.getStringExtra("docId");

        database = FirebaseFirestore.getInstance();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Progress");
        progressDialog.setMessage("Booking...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        if(Common.FARM_MODEL.getGuestCapacity()<Integer.parseInt(Common.NO_OF_GUEST)){
            extraGuest=Integer.parseInt(Common.NO_OF_GUEST)-Common.FARM_MODEL.getGuestCapacity();
        }

        DocumentReference docRef = database.collection("id").document("id");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        idModel = document.toObject(IdModel.class);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        txtNameBook.setText(Common.FARM_MODEL.getName());
        txtAddressBook.setText(Common.FARM_MODEL.getStreetAddress()+", "+Common.FARM_MODEL.getCity()+", "+Common.FARM_MODEL.getState());
        setDateText(Common.DATE_RANGE);
        txtTimeCheckInBook.setText(Common.FARM_MODEL.getCheckInTime());
        txtTimeCheckOutBook.setText(Common.FARM_MODEL.getCheckOutTime());
        txtNoOfGuestBook.setText("No. Of Guests : "+Common.NO_OF_GUEST);
        txtFinalPriceBook.setText("₹"+Common.FARM_MODEL.getFinalPrice()*Common.NO_OF_NIGHTS);
        txtPriceFarmBook.setText("₹"+Common.FARM_MODEL.getPrice()*Common.NO_OF_NIGHTS);
        txtDisscountBook.setText(Common.FARM_MODEL.getDiscount()+"% OFF");
        txtPriceBook.setText("₹"+Common.FARM_MODEL.getPrice()*Common.NO_OF_NIGHTS);
        txtGuestCapacityBook.setText("For "+Common.FARM_MODEL.getGuestCapacity()+" Guests");
        txtPriceForNightBook.setText("₹"+Common.FARM_MODEL.getPrice()+" X "+Common.NO_OF_NIGHTS+" nights");
        txtDisscountTextPerBook.setText("Disscount ("+Common.FARM_MODEL.getDiscount()+"% Off)");
        txtDisscountRupeesBook.setText("-₹"+(Common.FARM_MODEL.getPrice()*Common.NO_OF_NIGHTS*Common.FARM_MODEL.getDiscount())/100);
        txtExtraGuestBook.setText("₹200 x "+extraGuest+" Guest");
        txtExtraGuestRupeesBook.setText("₹"+200*extraGuest);
        txtGSTRupeesBook.setText("₹"+(((Common.FARM_MODEL.getFinalPrice()*Common.NO_OF_NIGHTS)+(200*extraGuest))*18)/100);
        final int amountPayable = ((Common.FARM_MODEL.getFinalPrice()*Common.NO_OF_NIGHTS)+(200*extraGuest)+((((Common.FARM_MODEL.getFinalPrice()*Common.NO_OF_NIGHTS)+(200*extraGuest))*18)/100));
        txtAmountPayableBook.setText("₹"+amountPayable);
        txtPriceButtonBook.setText("₹"+amountPayable);
        btnContinueBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userId != null) {
                    // User is signed in
                    progressDialog.show();
                    int i;
                    ArrayList<String> bookedDates = new ArrayList<String>();
                    DocumentReference docRef = database.collection("farm").document(docId);
                    for(i=1;i<Common.DATE_RANGE.size();i++){
                        docRef.update("bookedDates", FieldValue.arrayUnion(Common.DATE_RANGE.get(i).toString()));
                    }
                    for(i=0;i<Common.DATE_RANGE.size();i++){
                        bookedDates.add(i,Common.DATE_RANGE.get(i).toString());
                    }

                    docRef.update("noOfBooking",Common.FARM_MODEL.getNoOfBooking()+Common.DATE_RANGE.size()-1);

                    BookingModel bookingModel = new BookingModel(docId,bookedDates,amountPayable,Integer.parseInt(Common.NO_OF_GUEST),todayDate.getTime().toString(),false,false);
                    database.collection("user").document(userId).collection("bookedFarms").document("FB00"+idModel.getBookingId()).set(bookingModel)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    database.collection("id").document("id")
                                            .update("bookingId",idModel.getBookingId()+1)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully written!");

                                                    Intent intent = new Intent(BookingSummaryActivity.this, HomeActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    progressDialog.dismiss();
                                                    Toast.makeText(BookingSummaryActivity.this, "Your Farm Successfully Booked.", Toast.LENGTH_SHORT).show();
                                                    startActivity(intent);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error writing document", e);
                                                }
                                            });

                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                    progressDialog.dismiss();
                                    Toast.makeText(BookingSummaryActivity.this, "Sorry, Try Again!", Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    // No user is signed in
                    startActivity(new Intent(BookingSummaryActivity.this, LoginActivity.class));
                }
            }
        });
    }

    private void setDateText(List<Date> dateRange){

        todayDate = Calendar.getInstance();
        tommorowDate = Calendar.getInstance();
        tommorowDate.add(Calendar.DATE,1);

        if(dateRange.get(0).equals(todayDate.getTime())){
            txtDateCheckInBook.setText("Today");
        }else {
            txtDateCheckInBook.setText(getDateInText(dateRange.get(0).getDay(),dateRange.get(0).getMonth(),dateRange.get(0).getDate()));
        }

        if (dateRange.get(dateRange.size()-1).equals(tommorowDate.getTime())){
            txtDateCheckOutBook.setText("Tommorow");
        }else {
            txtDateCheckOutBook.setText(getDateInText(dateRange.get(dateRange.size()-1).getDay(),dateRange.get(dateRange.size()-1).getMonth(),dateRange.get(dateRange.size()-1).getDate()));
        }

        txtNightCountBook.setText(dateRange.size()-1+"N");

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
