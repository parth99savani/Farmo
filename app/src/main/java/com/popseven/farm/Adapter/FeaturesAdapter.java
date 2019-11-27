package com.popseven.farm.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popseven.farm.Model.FeaturesModel;
import com.popseven.farm.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FeaturesAdapter extends RecyclerView.Adapter<FeaturesAdapter.MyViewHolder> {

    private List<FeaturesModel> featuresList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView iconView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txtFeatures);
            iconView = (ImageView)view.findViewById(R.id.iconFeatures);
        }
    }

    public FeaturesAdapter(List<FeaturesModel> featuresList) {
        this.featuresList = featuresList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.features_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FeaturesModel features = featuresList.get(position);
        holder.title.setText(features.getName());
        Picasso.get().load(features.getIconUrl()).into(holder.iconView);
    }

    @Override
    public int getItemCount() {
        return featuresList.size();
    }
}
