package com.popseven.farm.Fragment;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.popseven.farm.AboutActivity;
import com.popseven.farm.Common.Common;
import com.popseven.farm.HomeActivity;
import com.popseven.farm.ProfileViewActivity;
import com.popseven.farm.R;

import static android.content.Context.MODE_PRIVATE;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private TextView txtNameUser;
    private TextView txtMobileNoUser;
    private CardView btnRate;
    private CardView btnShare;
    private CardView btnPrivacy;
    private CardView btnAbout;
    private SharedPreferences sp;
    private String userId;
    private FirebaseFirestore database;
    private static final String TAG = "AccountFragment";
    private CardView btnLogout;
    private CardView btnProfile;

    public AccountFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        txtNameUser = view.findViewById(R.id.txtNameUser);
        txtMobileNoUser = view.findViewById(R.id.txtMobileNoUser);
        btnProfile = view.findViewById(R.id.btnProfile);
        btnRate = view.findViewById(R.id.btnRate);
        btnShare = view.findViewById(R.id.btnShare);
        btnPrivacy = view.findViewById(R.id.btnPrivacy);
        btnAbout = view.findViewById(R.id.btnAbout);
        btnLogout = view.findViewById(R.id.btnLogout);

        sp = getActivity().getSharedPreferences("Login", MODE_PRIVATE);
        userId = sp.getString("Uid", null);

        //database = FirebaseFirestore.getInstance();

        if (userId != null && Common.USER_MODEL != null) {
            txtMobileNoUser.setText(userId);
            txtNameUser.setText(Common.USER_MODEL.getName());
            /*DocumentReference docRef = database.collection("user").document(userId);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            txtMobileNoUser.setText(userId);
                            txtNameUser.setText(document.getString("name"));
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        } else {
                            Log.d(TAG, "No such document");

                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });*/
        }

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAbout = new Intent(getActivity(), AboutActivity.class);
                startActivity(intentAbout);
            }
        });

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + getActivity().getPackageName())));
                }catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Intent.ACTION_SEND);

                //this is to get the app link in the playstore without launching your app.
                final String appPackageName = getActivity().getPackageName();
                String strAppLink = "";

                try
                {
                    strAppLink = "https://play.google.com/store/apps/details?id=" + appPackageName;
                }
                catch (android.content.ActivityNotFoundException anfe)
                {
                    strAppLink = "https://play.google.com/store/apps/details?id=" + appPackageName;
                }
                // this is the sharing part
                a.setType("text/link");
                String shareBody = "Farm\n\nHey! Download this app for free & book farm online for your holiday." +
                        "\n"+""+strAppLink;
                String shareSub = "Farm";
                a.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                a.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(a, "Share 'Farm App' Using"));
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProfile = new Intent(getActivity(), ProfileViewActivity.class);
                startActivity(intentProfile);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                sp.edit().clear().commit();
                Intent intentHome = new Intent(getActivity(), HomeActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentHome);
            }
        });

        return view;
    }

}
