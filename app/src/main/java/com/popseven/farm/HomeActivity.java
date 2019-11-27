package com.popseven.farm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.popseven.farm.Common.Common;
import com.popseven.farm.Fragment.AccountFragment;
import com.popseven.farm.Fragment.BookingFragment;
import com.popseven.farm.Fragment.FavoriteFragment;
import com.popseven.farm.Fragment.HomeFragment;
import com.popseven.farm.Model.UserModel;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private TextView mTitle;
    private String userId;
    private SharedPreferences sp;
    private FirebaseFirestore database;
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database = FirebaseFirestore.getInstance();

        sp = getSharedPreferences("Login",MODE_PRIVATE);
        userId=sp.getString("Uid",null);

        if (userId != null) {
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

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        //loading the default fragment
        loadFragment(new HomeFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;

            case R.id.navigation_booking:
                //get current user
                if (userId != null) {
                    fragment = new BookingFragment();
                    break;
                    // User is signed in
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    break;
                    // No user is signed in
                }

            case R.id.navigation_favorite:
                if (userId != null) {
                    fragment = new FavoriteFragment();
                    break;
                    // User is signed in
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    break;
                    // No user is signed in
                }

            case R.id.navigation_account:
                if (userId != null) {
                    fragment = new AccountFragment();
                    break;
                    // User is signed in
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    break;
                    // No user is signed in
                }
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
