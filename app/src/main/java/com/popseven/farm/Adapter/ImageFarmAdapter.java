package com.popseven.farm.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.popseven.farm.Model.ImageFarmModel;
import com.popseven.farm.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageFarmAdapter extends RecyclerView.Adapter<ImageFarmAdapter.MyViewHolder> {

    private List<ImageFarmModel> imageList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewFarm;

        public MyViewHolder(View view) {
            super(view);
            imageViewFarm = (ImageView) view.findViewById(R.id.imageViewFarm);

        }
    }


    public ImageFarmAdapter(List<ImageFarmModel> imageList) {
        this.imageList = imageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_farm_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ImageFarmModel imageFarmModel = imageList.get(position);
        Picasso.get().load(imageFarmModel.getImageUrl()).into(holder.imageViewFarm);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
