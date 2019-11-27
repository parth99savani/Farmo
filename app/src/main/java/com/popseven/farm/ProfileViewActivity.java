package com.popseven.farm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.popseven.farm.Common.Common;

public class ProfileViewActivity extends AppCompatActivity {

    private TextView txtToolbarTitleProfileView;
    private TextView btnEditProfile;
    private Toolbar toolbarProfileView;
    private TextView txtNameProfile;
    private TextView txtMobileNoProfile;
    private TextView txtEmailProfile;
    private SharedPreferences sp;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        toolbarProfileView = findViewById(R.id.toolbar_profile_view);
        setSupportActionBar(toolbarProfileView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txtToolbarTitleProfileView = toolbarProfileView.findViewById(R.id.txtToolbarTitleProfileView);

        btnEditProfile = findViewById(R.id.btnEditProfile);
        txtNameProfile = findViewById(R.id.txtNameProfile);
        txtMobileNoProfile = findViewById(R.id.txtMobileNoProfile);
        txtEmailProfile = findViewById(R.id.txtEmailProfile);

        sp = this.getSharedPreferences("Login", MODE_PRIVATE);
        userId = sp.getString("Uid", null);

        if (userId != null && Common.USER_MODEL != null) {
            txtMobileNoProfile.setText(userId);
            txtNameProfile.setText(Common.USER_MODEL.getName());
            txtEmailProfile.setText(Common.USER_MODEL.getEmailId());
        }

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileViewActivity.this,EditProfileActivity.class));
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        if (userId != null && Common.USER_MODEL != null) {
            txtMobileNoProfile.setText(userId);
            txtNameProfile.setText(Common.USER_MODEL.getName());
            txtEmailProfile.setText(Common.USER_MODEL.getEmailId());
        }
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
