package com.popseven.farm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.popseven.farm.Common.Common;

import java.util.ArrayList;

public class EditProfileActivity extends AppCompatActivity {

    private TextView txtToolbarTitleEditProfile;
    private Toolbar toolbarEditProfile;
    private EditText editTextNameProfile;
    private EditText editTextEmailProfile;
    private AppCompatButton btnSaveProfile;
    private SharedPreferences sp;
    private String userId;
    private FirebaseFirestore database;
    private static final String TAG = "EditProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbarEditProfile = findViewById(R.id.toolbar_edit_profile);
        setSupportActionBar(toolbarEditProfile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txtToolbarTitleEditProfile = toolbarEditProfile.findViewById(R.id.txtToolbarTitleEditProfile);

        sp = this.getSharedPreferences("Login", MODE_PRIVATE);
        userId = sp.getString("Uid", null);

        database = FirebaseFirestore.getInstance();

        editTextNameProfile = findViewById(R.id.editTextNameProfile);
        editTextEmailProfile = findViewById(R.id.editTextEmailProfile);
        btnSaveProfile = findViewById(R.id.btn_saveProfile);

        if (userId != null && Common.USER_MODEL != null) {
            editTextNameProfile.setText(Common.USER_MODEL.getName());
            editTextEmailProfile.setText(Common.USER_MODEL.getEmailId());
        }

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    public void saveProfile() {

        Log.d(TAG, "Update Profile");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        btnSaveProfile.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final String name = editTextNameProfile.getText().toString();
        final String email = editTextEmailProfile.getText().toString();


        database.collection("user").document(userId)
                .update("name", name,"emailId",email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        // On complete call either onSignupSuccess or onSignupFailed
                                        // depending on success
                                        Common.USER_MODEL.setEmailId(email);
                                        Common.USER_MODEL.setName(name);
                                        onSignupSuccess();
                                        // onSignupFailed();
                                        progressDialog.dismiss();
                                    }
                                }, 3000);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        onSignupFailed();
                    }
                });

    }

    public void onSignupSuccess() {
        btnSaveProfile.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Update failed! Try Again", Toast.LENGTH_LONG).show();

        btnSaveProfile.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = editTextNameProfile.getText().toString();
        String email = editTextEmailProfile.getText().toString();

        if (name.isEmpty() || name.length() < 2) {
            editTextNameProfile.setError("at least 2 characters");
            valid = false;
        } else {
            editTextNameProfile.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmailProfile.setError("Enter valid email!");
            valid = false;
        } else {
            editTextEmailProfile.setError(null);
        }

        return valid;
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
