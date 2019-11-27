package com.popseven.farm;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alespero.expandablecardview.ExpandableCardView;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.popseven.farm.Common.Common;

import java.util.ArrayList;
import java.util.List;

public class FilterDialog extends DialogFragment {

    public static String TAG = "FullScreenFilterDialog";
    private ExpandableCardView priceRange, amenities, discount, rating;
    private CheckBox checkBoxWifi, checkBoxAC, checkBoxTV, checkBoxParking, checkBoxGeyser, checkBoxCCTV, checkBoxGames, checkBoxKitchen, checkBoxRefrigerator;
    private RadioGroup radioDiscountGroup, radioRatingGroup;
    private RadioButton radioDiscountButton, radioRatingButton;
    private CrystalRangeSeekbar rangeSeekbar;
    private Button btnApplyFilter;
    private List<String> amenitiesFilter;
    private TextView btnReset;
    private String minPrice,maxPrice;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.filter_dialog, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar_filter);
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.setTitle("Filter");

        priceRange = view.findViewById(R.id.priceRange);
        amenities = view.findViewById(R.id.amenities);
        discount = view.findViewById(R.id.discount);
        rating = view.findViewById(R.id.rating);
        btnApplyFilter = view.findViewById(R.id.btn_applyFilter);
        btnReset = view.findViewById(R.id.btnReset);

        checkBoxWifi = (CheckBox) view.findViewById(R.id.checkBoxWifi);
        checkBoxAC = (CheckBox) view.findViewById(R.id.checkBoxAC);
        checkBoxTV = (CheckBox) view.findViewById(R.id.checkBoxTV);
        checkBoxParking = (CheckBox) view.findViewById(R.id.checkBoxParking);
        checkBoxGeyser = (CheckBox) view.findViewById(R.id.checkBoxGeyser);
        checkBoxCCTV = (CheckBox) view.findViewById(R.id.checkBoxCCTV);
        checkBoxGames = (CheckBox) view.findViewById(R.id.checkBoxGames);
        checkBoxKitchen = (CheckBox) view.findViewById(R.id.checkBoxKitchen);
        checkBoxRefrigerator = (CheckBox) view.findViewById(R.id.checkBoxRefrigerator);

        radioDiscountGroup = (RadioGroup) view.findViewById(R.id.radioDiscount);
        radioRatingGroup = (RadioGroup) view.findViewById(R.id.radioRating);

        priceRange.findViewById(R.id.card).setBackgroundColor(Color.parseColor("#ffffff"));
        amenities.findViewById(R.id.card).setBackgroundColor(Color.parseColor("#ffffff"));
        discount.findViewById(R.id.card).setBackgroundColor(Color.parseColor("#ffffff"));
        rating.findViewById(R.id.card).setBackgroundColor(Color.parseColor("#ffffff"));

        rangeSeekbar = (CrystalRangeSeekbar) view.findViewById(R.id.rangeSeekbar);
        // get min and max text view
        final TextView tvMin = (TextView) view.findViewById(R.id.textMin1);
        final TextView tvMax = (TextView) view.findViewById(R.id.textMax1);

        // set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                minPrice=String.valueOf(minValue);
                maxPrice=String.valueOf(maxValue);
                tvMin.setText("₹" + String.valueOf(minValue));
                tvMax.setText("₹" + String.valueOf(maxValue));
            }
        });

        if(Common.MIN_PRICE_FILTER!=null&&Common.MAX_PRICE_FILTER!=null){
            rangeSeekbar.setMinValue(Common.MIN_PRICE_FILTER);
            rangeSeekbar.setMaxValue(Common.MAX_PRICE_FILTER);
        }
        int i;
        if(Common.AMENITIES_LIST_FILTER!=null){
            for (i=0;i<Common.AMENITIES_LIST_FILTER.size();i++) {
                switch (Common.AMENITIES_LIST_FILTER.get(i)) {
                    case "Free Wifi":
                        checkBoxWifi.setChecked(true);
                        break;
                    case "AC":
                        checkBoxAC.setChecked(true);
                        break;
                    case "TV":
                        checkBoxTV.setChecked(true);
                        break;
                    case "Parking":
                        checkBoxParking.setChecked(true);
                        break;
                    case "Geyser":
                        checkBoxGeyser.setChecked(true);
                        break;
                    case "CCTV Cameras":
                        checkBoxCCTV.setChecked(true);
                        break;
                    case "Games":
                        checkBoxGames.setChecked(true);
                        break;
                    case "Kitchen":
                        checkBoxKitchen.setChecked(true);
                        break;
                    case "Refrigerator":
                        checkBoxRefrigerator.setChecked(true);
                        break;
                }
            }
        }
        if(Common.DISCOUNT_FILTER!=null){
            switch (Common.DISCOUNT_FILTER) {
                case 10:
                    RadioButton radio10Discount = radioDiscountGroup.findViewById(R.id.radio10Discount);
                    radio10Discount.setChecked(true);
                    break;
                case 25:
                    RadioButton radio25Discount = radioDiscountGroup.findViewById(R.id.radio25Discount);
                    radio25Discount.setChecked(true);
                    break;
                case 35:
                    RadioButton radio35Discount = radioDiscountGroup.findViewById(R.id.radio35Discount);
                    radio35Discount.setChecked(true);
                    break;
                case 50:
                    RadioButton radio50Discount = radioDiscountGroup.findViewById(R.id.radio50Discount);
                    radio50Discount.setChecked(true);
                    break;
            }
        }
        if(Common.RATING_FILTER!=null){
            switch (Common.RATING_FILTER) {
                case 4:
                    RadioButton radio4Rating = radioRatingGroup.findViewById(R.id.radio4Rating);
                    radio4Rating.setChecked(true);
                    break;
                case 3:
                    RadioButton radio3Rating = radioRatingGroup.findViewById(R.id.radio3Rating);
                    radio3Rating.setChecked(true);
                    break;
                case 2:
                    RadioButton radio2Rating = radioRatingGroup.findViewById(R.id.radio2Rating);
                    radio2Rating.setChecked(true);
                    break;
                case 1:
                    RadioButton radio1Rating = radioRatingGroup.findViewById(R.id.radio1Rating);
                    radio1Rating.setChecked(true);
                    break;
            }
        }


        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.AMENITIES_LIST_FILTER = null;
                Common.DISCOUNT_FILTER = null;
                Common.RATING_FILTER = null;
                Common.MIN_PRICE_FILTER = null;
                Common.MAX_PRICE_FILTER = null;
                radioDiscountGroup.clearCheck();
                radioRatingGroup.clearCheck();
                checkBoxWifi.setChecked(false);
                checkBoxAC.setChecked(false);
                checkBoxTV.setChecked(false);
                checkBoxParking.setChecked(false);
                checkBoxGeyser.setChecked(false);
                checkBoxCCTV.setChecked(false);
                checkBoxGames.setChecked(false);
                checkBoxKitchen.setChecked(false);
                checkBoxRefrigerator.setChecked(false);
                rangeSeekbar.setMinValue(1000);
                rangeSeekbar.setMaxValue(15000);
            }
        });

        btnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amenitiesFilter = new ArrayList<>();

                if (checkBoxWifi.isChecked()) {
                    amenitiesFilter.add("Free Wifi");
                }
                if (checkBoxAC.isChecked()) {
                    amenitiesFilter.add("AC");
                }
                if (checkBoxTV.isChecked()) {
                    amenitiesFilter.add("TV");
                }
                if (checkBoxParking.isChecked()) {
                    amenitiesFilter.add("Parking");
                }
                if (checkBoxGeyser.isChecked()) {
                    amenitiesFilter.add("Geyser");
                }
                if (checkBoxCCTV.isChecked()) {
                    amenitiesFilter.add("CCTV Cameras");
                }
                if (checkBoxGames.isChecked()) {
                    amenitiesFilter.add("Games");
                }
                if (checkBoxKitchen.isChecked()) {
                    amenitiesFilter.add("Kitchen");
                }
                if (checkBoxRefrigerator.isChecked()) {
                    amenitiesFilter.add("Refrigerator");
                }

                Common.AMENITIES_LIST_FILTER = amenitiesFilter;

                int selectedIdDiscount = radioDiscountGroup.getCheckedRadioButtonId();
                //radioDiscountButton = (RadioButton) radioDiscountGroup.findViewById(selectedId);
                //Toast.makeText(getActivity(), radioDiscountButton.getText(), Toast.LENGTH_SHORT).show();

                switch (selectedIdDiscount) {
                    case R.id.radio10Discount:
                        Common.DISCOUNT_FILTER = 10;
                        break;
                    case R.id.radio25Discount:
                        Common.DISCOUNT_FILTER = 25;
                        break;
                    case R.id.radio35Discount:
                        Common.DISCOUNT_FILTER = 35;
                        break;
                    case R.id.radio50Discount:
                        Common.DISCOUNT_FILTER = 50;
                        break;
                }

                int selectedIdRating = radioRatingGroup.getCheckedRadioButtonId();

                switch (selectedIdRating) {
                    case R.id.radio4Rating:
                        Common.RATING_FILTER = 4;
                        break;
                    case R.id.radio3Rating:
                        Common.RATING_FILTER = 3;
                        break;
                    case R.id.radio2Rating:
                        Common.RATING_FILTER = 2;
                        break;
                    case R.id.radio1Rating:
                        Common.RATING_FILTER = 1;
                        break;
                }

                Common.MIN_PRICE_FILTER = Integer.valueOf(minPrice);
                Common.MAX_PRICE_FILTER = Integer.valueOf(maxPrice);

                ((FarmListActivity) getActivity()).filterFarm();

                dismiss();
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
