package com.popseven.farm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.popseven.farm.Common.Common;

import java.util.concurrent.TimeUnit;

public class OTPVerifyActivity extends AppCompatActivity {

    private TextView txtHint;
    private PinView pinView;
    private TextView txtResendOTP;
    private AppCompatButton btnVerify;
    private ImageView btnBackOtp;
    private FirebaseAuth mAuth;
    private String mobile;
    private String mVerificationId;
    private FirebaseFirestore database;
    private static final String TAG = "OTPVerifyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        //Intent intent = getIntent();
        //mobile = intent.getStringExtra("mobile");
        mobile= Common.UserMobileNo;
        sendVerificationCode(mobile);

        txtHint = findViewById(R.id.txtHint);
        pinView = findViewById(R.id.pinView);
        txtResendOTP = findViewById(R.id.txtResendOTP);
        btnVerify = findViewById(R.id.btn_Verify);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = pinView.getText().toString();
                /*if (OTP.equals("345678")) {
                    pinView.setLineColor(getResources().getColor(R.color.green));
                    txtHint.setText("OTP Verified");
                    txtHint.setTextColor(getResources().getColor(R.color.green));
                    btnVerify.setText("Next");
                } else {
                    pinView.setLineColor(Color.RED);
                    txtHint.setText("X Incorrect OTP");
                    txtHint.setTextColor(Color.RED);
                    btnVerify.setText("Retry");
                }*/
                if (code.isEmpty() || code.length() < 6) {
                    Toast.makeText(OTPVerifyActivity.this, "Enter OTP!", Toast.LENGTH_SHORT).show();
                    pinView.requestFocus();
                    //return;
                }else {
                    verifyVerificationCode(code);
                }

                //verifying the code entered manually
                //verifyVerificationCode(code);
            }
        });

        btnBackOtp = findViewById(R.id.btnBackOtp);
        btnBackOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                pinView.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OTPVerifyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OTPVerifyActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            pinView.setLineColor(getResources().getColor(R.color.green));
                            txtHint.setText("OTP Verified");
                            txtHint.setTextColor(getResources().getColor(R.color.green));
                            btnVerify.setText("Next");
                            //verification successful we will start the profile activity
                            btnVerify.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DocumentReference docRef = database.collection("user").document(mobile);
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
                                                    SharedPreferences.Editor Ed=sp.edit();
                                                    Ed.putString("Uid",mobile);
                                                    Ed.commit();
                                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                    Intent intentHome = new Intent(OTPVerifyActivity.this, HomeActivity.class);
                                                    intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intentHome);
                                                } else {
                                                    Log.d(TAG, "No such document");
                                                    Intent intentReg = new Intent(OTPVerifyActivity.this, RegistrationActivity.class);
                                                    intentReg.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intentReg);
                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });

                                }
                            });


                        } else {

                            pinView.setLineColor(Color.RED);
                            txtHint.setText("X Incorrect OTP");
                            txtHint.setTextColor(Color.RED);
                            btnVerify.setText("Retry");

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            /*Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();*/
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
