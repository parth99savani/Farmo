package com.popseven.farm.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import com.popseven.farm.R;

public class OfferViewHolder extends RecyclerView.ViewHolder{

    public ImageView imageOffer;

    public OfferViewHolder(View itemView) {
        super(itemView);
        imageOffer = (ImageView) itemView.findViewById(R.id.imageOffer);
    }
}
