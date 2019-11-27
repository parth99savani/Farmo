package com.popseven.farm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.popseven.farm.Common.Common;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView txtTitleMap;
    private Toolbar toolbarMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        txtTitleMap = findViewById(R.id.txtTitleMap);

        toolbarMap = findViewById(R.id.toolbar_map);
        setSupportActionBar(toolbarMap);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txtTitleMap = (TextView) toolbarMap.findViewById(R.id.txtTitleMap);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(Common.FARM_MODEL.getMapLocation().getLatitude(), Common.FARM_MODEL.getMapLocation().getLongitude())).title(Common.FARM_MODEL.getName()));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Common.FARM_MODEL.getMapLocation().getLatitude(), Common.FARM_MODEL.getMapLocation().getLongitude()),15));
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
