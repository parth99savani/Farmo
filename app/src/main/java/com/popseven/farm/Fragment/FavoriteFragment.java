package com.popseven.farm.Fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;

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
import com.popseven.farm.FarmViewActivity;
import com.popseven.farm.LoginActivity;
import com.popseven.farm.Model.FarmModel;
import com.popseven.farm.Model.UserModel;
import com.popseven.farm.R;
import com.popseven.farm.ViewHolder.FarmViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerviewFavorite;
    private ProgressBar progressbarFavorite;
    private FirebaseFirestore database;
    private FirestoreRecyclerAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private Query queryFavorite;
    private String userId;
    private SharedPreferences sp;
    private Calendar tommorowDate,todayDate;
    private List<Date> selectedDateRange;
    public static final String TAG = "FavoriteActivity";

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerviewFavorite = view.findViewById(R.id.recyclerview_favorite);
        progressbarFavorite = view.findViewById(R.id.progressbarFavorite);

        sp = getActivity().getSharedPreferences("Login",MODE_PRIVATE);
        userId=sp.getString("Uid",null);

        todayDate = Calendar.getInstance();
        tommorowDate = Calendar.getInstance();
        tommorowDate.add(Calendar.DATE,1);

        selectedDateRange = new ArrayList<>();
        selectedDateRange.add(todayDate.getTime());
        selectedDateRange.add(tommorowDate.getTime());

        Common.DATE_RANGE=selectedDateRange;

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerviewFavorite.setLayoutManager(linearLayoutManager);
        database = FirebaseFirestore.getInstance();
        queryFavorite = database.collection("farm");
        getFarmList(queryFavorite);

        return view;
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
                progressbarFavorite.setVisibility(View.GONE);

                if(!Common.USER_MODEL.getFavoriteFarms().contains(docId)){
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                }else {
                    holder.txtNameFarm.setText(model.getName());
                    holder.txtAddressFarm.setText(model.getStreetAddress()+","+model.getCity()+"-"+model.getZip()+","+model.getState());
                    holder.txtPriceFarm.setText("₹"+model.getPrice());
                    holder.txtDisscountFarm.setText(model.getDiscount()+"% OFF");
                    holder.txtFinalPriceFarm.setText("₹"+model.getFinalPrice());
                    Picasso.get().load(model.getImageUrl().get(0)).into(holder.imageFarm);
                    float rating=0;
                    int i;
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
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
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
                                                    getFarmList(queryFavorite);
                                                    adapter.startListening();
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
                            Intent intentFramView = new Intent(getActivity(), FarmViewActivity.class);
                            intentFramView.putExtra("docId", docId);
                            startActivity(intentFramView);
                        }
                    });
                }


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
        recyclerviewFavorite.setAdapter(adapter);
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

}
