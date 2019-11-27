package com.popseven.farm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.popseven.farm.Common.Common;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

public class LoginActivity extends AppCompatActivity {

    private CountryCodePicker ccp;
    private EditText editTextMobileNo;
    private AppCompatButton btnLetsGo;
    private ImageView btnBackLogin;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ccp = findViewById(R.id.ccp);
        editTextMobileNo = findViewById(R.id.editTextMobileNo);
        btnLetsGo = findViewById(R.id.btn_LetsGo);

        ccp.registerPhoneNumberTextView(editTextMobileNo);

        btnLetsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = ccp.getFullNumberWithPlus();

                if(mobile.isEmpty() || mobile.length() < 10){
                    Toast.makeText(LoginActivity.this, "Enter valid mobile number!", Toast.LENGTH_SHORT).show();
                    editTextMobileNo.requestFocus();
                    return;
                }
                Intent intent = new Intent(LoginActivity.this, OTPVerifyActivity.class);
                //intent.putExtra("mobile", mobile);
                Common.UserMobileNo=mobile;
                startActivity(intent);
            }
        });

        btnBackLogin = findViewById(R.id.btnBackLogin);
        btnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
