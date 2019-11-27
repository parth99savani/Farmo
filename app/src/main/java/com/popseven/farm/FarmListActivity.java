package com.popseven.farm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.popseven.farm.Common.Common;
import com.popseven.farm.Model.FarmModel;
import com.popseven.farm.Model.UserModel;
import com.popseven.farm.ViewHolder.FarmViewHolder;
import com.savvi.rangedatepicker.CalendarPickerView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FarmListActivity extends AppCompatActivity{

    public static final String TAG = "FarmListActivity";
    private Toolbar toolbar;
    private LinearLayout btnSearchbarFarmList,layoutCheckInFarmList,layoutCheckOutFarmList;
    private TextView txtGuestCountFarmList,txtDateCheckInFarmList,txtDateCheckOutFarmList,txtNightCountFarmList,txtSearchLocationFarmList;
    private Calendar tommorowDate,todayDate;
    private List<Date> selectedDateRange;
    private RecyclerView recyclerViewFarmList;
    private ProgressBar progressBarFarmList;
    private FirebaseFirestore database;
    private FirestoreRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private Query queryFarmList;
    private ImageView btnSort,btnFilter;
    private int idFilter = 0,selectedId;
    private String selectedSort="Popularity";
    private String userId;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_list);

        btnSearchbarFarmList = (LinearLayout)findViewById(R.id.btnSearchbarFarmList);
        layoutCheckInFarmList = (LinearLayout)findViewById(R.id.layoutCheckInFarmList);
        layoutCheckOutFarmList = (LinearLayout)findViewById(R.id.layoutCheckOutFarmList);
        txtGuestCountFarmList = (TextView) findViewById(R.id.txtGuestCountFarmList);
        txtDateCheckInFarmList = (TextView) findViewById(R.id.txtDateCheckInFarmList);
        txtDateCheckOutFarmList = (TextView) findViewById(R.id.txtDateCheckOutFarmList);
        txtNightCountFarmList = (TextView) findViewById(R.id.txtNightCountFarmList);
        txtSearchLocationFarmList = (TextView) findViewById(R.id.txtSearchLocationFarmList);
        recyclerViewFarmList = (RecyclerView)findViewById(R.id.recyclerview_farmlist);
        progressBarFarmList = (ProgressBar)findViewById(R.id.progressbarFarmList);
        btnSort = (ImageView)findViewById(R.id.btnSort);
        btnFilter = (ImageView)findViewById(R.id.btnFilter);

        // toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_search_farm_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sp = getSharedPreferences("Login",MODE_PRIVATE);
        userId=sp.getString("Uid",null);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewFarmList.setLayoutManager(linearLayoutManager);
        database = FirebaseFirestore.getInstance();

        sortFarm(selectedSort);



        todayDate = Calendar.getInstance();
        tommorowDate = Calendar.getInstance();
        tommorowDate.add(Calendar.DATE,1);

        txtSearchLocationFarmList.setText(Common.LOCATION_CITY/*+","+Common.LOCATION_STATE*/);
        setDateText(Common.DATE_RANGE);
        txtGuestCountFarmList.setText(Common.NO_OF_GUEST+" Guests");

        btnSearchbarFarmList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(FarmListActivity.this,SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        layoutCheckInFarmList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickDialog();
            }
        });

        layoutCheckOutFarmList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickDialog();
            }
        });

        txtGuestCountFarmList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialogNumberPicker = new Dialog(FarmListActivity.this);
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
                        txtGuestCountFarmList.setText(String.valueOf(np.getValue())+" Guests");
                        Common.NO_OF_GUEST = String.valueOf(np.getValue());
                        dialogNumberPicker.dismiss();
                    }
                });
                dialogNumberPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogNumberPicker.show();

            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortBottomSheet();
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterDialog dialog = new FilterDialog();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                dialog.show(ft,FilterDialog.TAG);
            }
        });



    }

    public void filterFarm(){
        sortFarm(selectedSort);
        adapter.startListening();
    }

    private void sortFarm(String selectedSort) {

        switch (selectedSort) {
            case "Popularity":
                queryFarmList = database.collection("farm").whereEqualTo("city",Common.LOCATION_CITY).orderBy("noOfBooking", Query.Direction.DESCENDING);
                getFarmList(queryFarmList);
                adapter.startListening();
                break;
            case "Rating":
                queryFarmList = database.collection("farm").whereEqualTo("city",Common.LOCATION_CITY).orderBy("ratingTotal", Query.Direction.DESCENDING);
                getFarmList(queryFarmList);
                adapter.startListening();
                break;
            case "Low to High Price":
                queryFarmList = database.collection("farm").whereEqualTo("city",Common.LOCATION_CITY).orderBy("finalPrice");
                getFarmList(queryFarmList);
                adapter.startListening();
                break;
            case "High to Low Price":
                queryFarmList = database.collection("farm").whereEqualTo("city",Common.LOCATION_CITY).orderBy("finalPrice", Query.Direction.DESCENDING);
                getFarmList(queryFarmList);
                adapter.startListening();
                break;
            default:
                queryFarmList = database.collection("farm").whereEqualTo("city",Common.LOCATION_CITY).orderBy("noOfBooking", Query.Direction.DESCENDING);
                getFarmList(queryFarmList);
                adapter.startListening();
                break;
        }

    }

    private void datePickDialog(){

        final Dialog dialogDatePick = new Dialog(FarmListActivity.this);
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
                Common.DATE_RANGE = selectedDateRange;
                getFarmList(queryFarmList);
                adapter.startListening();
                dialogDatePick.dismiss();

            }
        });
    }

    private void setDateText(List<Date> dateRange){

        if(dateRange.get(0).equals(todayDate.getTime())){
            txtDateCheckInFarmList.setText("Today");
        }else {
            txtDateCheckInFarmList.setText(getDateInText(dateRange.get(0).getDay(),dateRange.get(0).getMonth(),dateRange.get(0).getDate()));
        }

        if (dateRange.get(dateRange.size()-1).equals(tommorowDate.getTime())){
            txtDateCheckOutFarmList.setText("Tommorow");
        }else {
            txtDateCheckOutFarmList.setText(getDateInText(dateRange.get(dateRange.size()-1).getDay(),dateRange.get(dateRange.size()-1).getMonth(),dateRange.get(dateRange.size()-1).getDate()));
        }

        Common.NO_OF_NIGHTS = dateRange.size()-1;
        txtNightCountFarmList.setText(dateRange.size()-1+"N");

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


    void getFarmList(Query query){

        FirestoreRecyclerOptions<FarmModel> response = new FirestoreRecyclerOptions.Builder<FarmModel>()
                .setQuery(query, FarmModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<FarmModel, FarmViewHolder>(response) {
            @Override
            public void onBindViewHolder(final FarmViewHolder holder, final int position, final FarmModel model) {
                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                final String docId = snapshot.getId();
                progressBarFarmList.setVisibility(View.GONE);
                if(Common.RATING_FILTER!=null){
                    if(Common.RATING_FILTER>model.getRatingTotal()){
                        holder.itemView.setVisibility(View.GONE);
                        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    }
                }
                if(Common.DISCOUNT_FILTER!=null){
                    if(Common.DISCOUNT_FILTER>model.getDiscount()){
                        holder.itemView.setVisibility(View.GONE);
                        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    }
                }
                if(Common.MIN_PRICE_FILTER!=null&&Common.MAX_PRICE_FILTER!=null){
                    if(Common.MIN_PRICE_FILTER>model.getFinalPrice()||Common.MAX_PRICE_FILTER<model.getFinalPrice()){
                        holder.itemView.setVisibility(View.GONE);
                        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    }
                }

                int i,j;
                if(Common.AMENITIES_LIST_FILTER!=null){
                    for (i=0;i<Common.AMENITIES_LIST_FILTER.size();i++) {
                        if(!model.getFeaturesName().contains(Common.AMENITIES_LIST_FILTER.get(i))){
                            holder.itemView.setVisibility(View.GONE);
                            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                        }
                    }
                }

                for (i=1;i<Common.DATE_RANGE.size();i++){
                    for(j=0;j<model.getBookedDates().size();j++){
                        if(getStringToDate(model.getBookedDates()).get(j).getYear() == Common.DATE_RANGE.get(i).getYear()){
                            if(getStringToDate(model.getBookedDates()).get(j).getMonth() == Common.DATE_RANGE.get(i).getMonth()){
                                if(getStringToDate(model.getBookedDates()).get(j).getDate() == Common.DATE_RANGE.get(i).getDate()){
                                    //holder.itemView.setVisibility(View.GONE);
                                    //holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                                    holder.txtBookedTitle.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                }

                holder.txtNameFarm.setText(model.getName());
                holder.txtAddressFarm.setText(model.getStreetAddress()+","+model.getCity()+"-"+model.getZip()+","+model.getState());
                holder.txtPriceFarm.setText("₹"+model.getPrice()*Common.NO_OF_NIGHTS);
                holder.txtDisscountFarm.setText(model.getDiscount()+"% OFF");
                holder.txtFinalPriceFarm.setText("₹"+model.getFinalPrice()*Common.NO_OF_NIGHTS);
                Picasso.get().load(model.getImageUrl().get(0)).into(holder.imageFarm);
                float rating=0;
                for(i=0;i<model.getRating().size();i++){
                    rating=rating+model.getRating().get(i);
                }
                holder.txtRate.setText(rating/model.getRating().size()+"/5");
                holder.txtRateCount.setText("("+model.getRating().size()+")");
                if (userId != null) {
                    // User is signed in
                    if(Common.USER_MODEL.getFavoriteFarms().contains(docId)){
                        holder.btn_favorite.setChecked(true);
                    }else {
                        holder.btn_favorite.setChecked(false);
                    }
                }
                holder.btn_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                                holder.btn_favorite.setChecked(false);
                                startActivity(new Intent(FarmListActivity.this, LoginActivity.class));
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
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.NO_OF_NIGHTS=Common.DATE_RANGE.size()-1;
                        Common.FARM_MODEL=model;
                        Intent intentFramView = new Intent(FarmListActivity.this,FarmViewActivity.class);
                        intentFramView.putExtra("docId", docId);
                        startActivity(intentFramView);
                    }
                });
            }

            @Override
            public FarmViewHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.farm_item, group, false);

                return new FarmViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        recyclerViewFarmList.setAdapter(adapter);
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

    private void sortBottomSheet(){
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = FarmListActivity.this.getLayoutInflater().inflate(R.layout.sort_bottom_sheet, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();

        final RadioGroup radioGroup;
        radioGroup = (RadioGroup)sheetView.findViewById(R.id.radioGroupSort);
        switch (selectedSort) {
            case "Popularity":
                ((RadioButton) radioGroup.findViewById(R.id.radioBtnPopularity)).setChecked(true);
                break;
            case "Rating":
                ((RadioButton) radioGroup.findViewById(R.id.radioBtnRating)).setChecked(true);
                break;
            case "Low to High Price":
                ((RadioButton) radioGroup.findViewById(R.id.radioBtnLowtoHighPrice)).setChecked(true);
                break;
            case "High to Low Price":
                ((RadioButton) radioGroup.findViewById(R.id.radioBtnHightoLowPrice)).setChecked(true);
                break;
            default:
                ((RadioButton) radioGroup.findViewById(R.id.radioBtnPopularity)).setChecked(true);
                break;
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton sortRadioButton = (RadioButton) group.findViewById(checkedId);
                selectedSort = (String) sortRadioButton.getText();
                sortFarm(selectedSort);
                mBottomSheetDialog.dismiss();
            }
        });

        /*mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Do something
            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();

        if(Common.LOCATION_CITY != null){
            txtSearchLocationFarmList.setText(Common.LOCATION_CITY/*+","+Common.LOCATION_STATE*/);
        }
    }
}
