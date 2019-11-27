package com.popseven.farm.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.popseven.farm.R;

public class FarmViewHolder extends RecyclerView.ViewHolder {

    public TextView txtNameFarm,txtAddressFarm,txtFinalPriceFarm,txtDisscountFarm,txtPriceFarm,txtRate,txtRateCount,txtBookedTitle;
    public ImageView imageFarm;
    public ToggleButton btn_favorite;

    public FarmViewHolder(View itemView) {
        super(itemView);
        txtNameFarm = (TextView) itemView.findViewById(R.id.txtNameFarm);
        txtAddressFarm = (TextView) itemView.findViewById(R.id.txtAddressFarm);
        txtFinalPriceFarm = (TextView) itemView.findViewById(R.id.txtFinalPriceFarm);
        txtDisscountFarm = (TextView) itemView.findViewById(R.id.txtDisscountFarm);
        txtPriceFarm = (TextView) itemView.findViewById(R.id.txtPriceFarm);
        txtRate = (TextView) itemView.findViewById(R.id.txtRate);
        txtRateCount = (TextView) itemView.findViewById(R.id.txtRateCount);
        txtBookedTitle = (TextView) itemView.findViewById(R.id.txtBookedTitle);
        imageFarm = (ImageView) itemView.findViewById(R.id.imageFarm);
        btn_favorite = (ToggleButton) itemView.findViewById(R.id.btn_favorite);
    }

}
