package com.popseven.farm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.popseven.farm.Adapter.FeaturesAdapter;
import com.popseven.farm.Adapter.ImageFarmAdapter;
import com.popseven.farm.Common.Common;
import com.popseven.farm.Model.FeaturesModel;
import com.popseven.farm.Model.ImageFarmModel;
import com.popseven.farm.Model.UserModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FarmViewActivity extends AppCompatActivity {

    //private ViewFlipper viewFlipper;
    //private GestureDetector mGestureDetector;
    private RecyclerView recyclerViewFeatures, recyclerviewImageFarmView;
    ;
    private ImageFarmAdapter imageFarmAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<ImageFarmModel> imageList = new ArrayList<>();
    private FeaturesAdapter featuresAdapter;
    private List<FeaturesModel> featuresList = new ArrayList<>();
    private TextView txtFarmPolicy, txtFarmDescription, txtFarmName, txtFarmAddress, txtRateFarm, txtRateCountFarm, txtGuestCapacityFarm, txtDisscountFarm, txtFarmCheckInTime, txtFarmCheckOutTime, txtfarmPrice, txtfarmNightGuest, btnBookFarm;
    private ImageView btnShowMap, btnBackFarm;
    private ToggleButton btnFavoriteFarm;
    private String docId;
    private String userId;
    private SharedPreferences sp;
    private FirebaseFirestore database;
    public static final String TAG = "FarmViewActivity";
    private CardView cardViewBookNowBottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_view);
        Window window = getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        window.setAttributes(winParams);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        txtFarmName = findViewById(R.id.txtFarmName);
        btnShowMap = findViewById(R.id.btnShowMap);
        txtFarmAddress = findViewById(R.id.txtFarmAddress);
        txtRateFarm = findViewById(R.id.txtRateFarm);
        txtRateCountFarm = findViewById(R.id.txtRateCountFarm);
        txtGuestCapacityFarm = findViewById(R.id.txtGuestCapacityFarm);
        txtDisscountFarm = findViewById(R.id.txtDisscountFarm);
        txtFarmCheckInTime = findViewById(R.id.txtFarmCheckInTime);
        txtFarmCheckOutTime = findViewById(R.id.txtFarmCheckOutTime);
        btnBackFarm = findViewById(R.id.btnBackFarm);
        btnFavoriteFarm = findViewById(R.id.btnFavoriteFarm);
        txtfarmPrice = findViewById(R.id.txtfarmPrice);
        txtfarmNightGuest = findViewById(R.id.txtfarmNightGuest);
        btnBookFarm = findViewById(R.id.btnBookFarm);
        txtFarmDescription = findViewById(R.id.txtFarmDescription);
        txtFarmPolicy = findViewById(R.id.txtFarmPolicy);
        recyclerviewImageFarmView = findViewById(R.id.recyclerview_imageFarmView);
        cardViewBookNowBottom = findViewById(R.id.cardViewBookNowBottom);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerviewImageFarmView.setLayoutManager(linearLayoutManager);

        for (int i = 0; i < Common.FARM_MODEL.getImageUrl().size(); i++) {
            ImageFarmModel imageFarmModel = new ImageFarmModel(Common.FARM_MODEL.getImageUrl().get(i));
            imageList.add(imageFarmModel);
        }

        int i,j;
        for (i=1;i<Common.DATE_RANGE.size();i++){
            for(j=0;j<Common.FARM_MODEL.getBookedDates().size();j++){
                if(getStringToDate(Common.FARM_MODEL.getBookedDates()).get(j).getYear() == Common.DATE_RANGE.get(i).getYear()){
                    if(getStringToDate(Common.FARM_MODEL.getBookedDates()).get(j).getMonth() == Common.DATE_RANGE.get(i).getMonth()){
                        if(getStringToDate(Common.FARM_MODEL.getBookedDates()).get(j).getDate() == Common.DATE_RANGE.get(i).getDate()){
                            cardViewBookNowBottom.setCardBackgroundColor(Color.RED);
                            btnBookFarm.setText("Booked");
                            btnBookFarm.setEnabled(false);
                        }
                    }
                }
            }
        }

        imageFarmAdapter = new ImageFarmAdapter(imageList);
        recyclerviewImageFarmView.setHasFixedSize(true);

        recyclerviewImageFarmView.setAdapter(imageFarmAdapter);
        imageFarmAdapter.notifyDataSetChanged();

        database = FirebaseFirestore.getInstance();

        sp = getSharedPreferences("Login", MODE_PRIVATE);
        userId = sp.getString("Uid", null);

        Intent intentFarmView = getIntent();
        docId = intentFarmView.getStringExtra("docId");

        if (userId != null) {
            // User is signed in
            if (Common.USER_MODEL.getFavoriteFarms().contains(docId)) {
                btnFavoriteFarm.setChecked(true);
            } else {
                btnFavoriteFarm.setChecked(false);
            }
        }
        btnFavoriteFarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    if (userId != null) {
                        // User is signed in
                        DocumentReference favRef = database.collection("user").document(userId);
                        favRef.update("favoriteFarms", FieldValue.arrayUnion(docId));
                        DocumentReference docRef = database.collection("user").document(userId);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Common.USER_MODEL = document.toObject(UserModel.class);
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                    } else {
                        // No user is signed in
                        btnFavoriteFarm.setChecked(false);
                        startActivity(new Intent(FarmViewActivity.this, LoginActivity.class));
                    }

                } else {
                    // The toggle is disabled
                    if (userId != null) {
                        DocumentReference favRef = database.collection("user").document(userId);
                        favRef.update("favoriteFarms", FieldValue.arrayRemove(docId));
                        DocumentReference docRef = database.collection("user").document(userId);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Common.USER_MODEL = document.toObject(UserModel.class);
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                }
            }
        });

        for (i = 0; i < Common.FARM_MODEL.getFeaturesName().size(); i++) {
            FeaturesModel featuresModel = new FeaturesModel(Common.FARM_MODEL.getFeaturesName().get(i), Common.FARM_MODEL.getFeaturesIcon().get(i));
            featuresList.add(featuresModel);
        }

        recyclerViewFeatures = (RecyclerView) findViewById(R.id.recyclerview_features);
        featuresAdapter = new FeaturesAdapter(featuresList);
        recyclerViewFeatures.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 5);
        recyclerViewFeatures.setLayoutManager(mLayoutManager);

        recyclerViewFeatures.setAdapter(featuresAdapter);
        featuresAdapter.notifyDataSetChanged();


        txtFarmName.setText(Common.FARM_MODEL.getName());
        txtFarmAddress.setText(Common.FARM_MODEL.getStreetAddress() + ", " + Common.FARM_MODEL.getCity() + ", " + Common.FARM_MODEL.getState());
        txtDisscountFarm.setText(Common.FARM_MODEL.getDiscount() + "% OFF");
        txtfarmPrice.setText("₹" + Common.FARM_MODEL.getFinalPrice() * Common.NO_OF_NIGHTS);
        txtfarmNightGuest.setText("For " + Common.NO_OF_NIGHTS + " Night & " + Common.NO_OF_GUEST + " Guests");
        txtGuestCapacityFarm.setText("Guest Capacity " + Common.FARM_MODEL.getGuestCapacity());
        txtFarmCheckInTime.setText("Check in time : " + Common.FARM_MODEL.getCheckInTime());
        txtFarmCheckOutTime.setText("Check out time : " + Common.FARM_MODEL.getCheckOutTime());
        txtFarmDescription.setText(Common.FARM_MODEL.getDescription().replace("\\n", "\n"));
        txtFarmPolicy.setText(Common.FARM_MODEL.getPolicy().replace("\\n", "\n"));
        float rating = 0;
        for (i = 0; i < Common.FARM_MODEL.getRating().size(); i++) {
            rating = rating + Common.FARM_MODEL.getRating().get(i);
        }
        txtRateFarm.setText(rating / Common.FARM_MODEL.getRating().size() + "/5");
        txtRateCountFarm.setText("(" + Common.FARM_MODEL.getRating().size() + ")");

        btnBackFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnBookFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmViewActivity.this, BookingSummaryActivity.class);
                intent.putExtra("docId", docId);
                startActivity(intent);
            }
        });

        btnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmViewActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });


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
}
