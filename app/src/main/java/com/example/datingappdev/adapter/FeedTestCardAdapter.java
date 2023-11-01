package com.example.datingappdev.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datingappdev.R;
import com.example.datingappdev.model.ItemModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FeedTestCardAdapter extends RecyclerView.Adapter<FeedTestCardAdapter.ViewHolder> {

    private List<ItemModel> items;

    public FeedTestCardAdapter(List<ItemModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card,parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setData(items.get(position));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image, imageTall, imageSex, imageHouse, imageMoney, imageDate;
        TextView nama, usia, kota;
         ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
            imageDate = itemView.findViewById(R.id.imageDate);
            imageTall = itemView.findViewById(R.id.imageTall);
            imageSex = itemView.findViewById(R.id.imageSex);
            imageHouse = itemView.findViewById(R.id.imageHouse);
            imageMoney = itemView.findViewById(R.id.imageMoney);

            nama = itemView.findViewById(R.id.item_name);
            usia = itemView.findViewById(R.id.item_age);
            kota = itemView.findViewById(R.id.item_city);
        }

         void setData(ItemModel data) {

             Picasso.get()
                     .load(data.getImage())
                     .fit()
                     .centerCrop()
                     .into(image);

             if(data.getUser().getDateFirst()) {
                 imageDate.setVisibility(View.VISIBLE);
             }
             if(data.getUser().getTall()) {
                 imageTall.setVisibility(View.VISIBLE);
             }
             if(data.getUser().getMoney()) {
                 imageMoney.setVisibility(View.VISIBLE);
             }
             if(data.getUser().getSexNow()) {
                 imageSex.setVisibility(View.VISIBLE);
             }
             if(data.getUser().getHouse()) {
                 imageHouse.setVisibility(View.VISIBLE);
             }


             nama.setText(data.getUser().getName());
             usia.setText(data.getUser().getAge());
             kota.setText(data.getUser().getEmail());

        }
    }

    public List<ItemModel> getItems() {
        return items;
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
    }
}
