package com.popseven.farm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.popseven.farm.Common.Common;
import com.popseven.farm.Model.BookingModel;
import com.popseven.farm.Model.UserModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private AppCompatButton btnLetsGo;
    private ImageView btnBackReg;
    private String mobile;
    private FirebaseFirestore database;
    private static final String TAG = "RegActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        btnLetsGo = findViewById(R.id.btn_LetsGo);
        btnBackReg = findViewById(R.id.btnBackReg);

        database = FirebaseFirestore.getInstance();

        //Intent intent = getIntent();
        //mobile = intent.getStringExtra("mobile");
        mobile= Common.UserMobileNo;

        btnLetsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        btnBackReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Submit");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        btnLetsGo.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registration");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();


        UserModel userModel = new UserModel(mobile, name, email, new ArrayList<String>());
        database.collection("user").document(mobile).set(userModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Common.USER_LOGIN=mobile;
                        SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
                        SharedPreferences.Editor Ed=sp.edit();
                        Ed.putString("Uid",mobile);
                        Ed.commit();
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        // On complete call either onSignupSuccess or onSignupFailed
                                        // depending on success
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
        btnLetsGo.setEnabled(true);
        setResult(RESULT_OK, null);
        //finish();
        Intent intentHome = new Intent(RegistrationActivity.this, HomeActivity.class);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentHome);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Submit failed! Try Again", Toast.LENGTH_LONG).show();

        btnLetsGo.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();

        if (name.isEmpty() || name.length() < 2) {
            editTextName.setError("at least 2 characters");
            valid = false;
        } else {
            editTextName.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter valid email!");
            valid = false;
        } else {
            editTextEmail.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
