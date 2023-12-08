package com.example.project_2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class Recycler_Adapter extends RecyclerView.Adapter<Recycler_Adapter.RecyclerViewHolder> {
    private ArrayList<Recycler_Item> mItemsList;
    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    public Recycler_Adapter(ArrayList<Recycler_Item> itemslist){
        //pass data here in this constructor function
        mItemsList = itemslist;
    }
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mDescription;
        public TextView mQuantity;
        public TextView mPrice;
        public TextView mID;
        public RecyclerViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            //references to views in xml file
            mImageView = itemView.findViewById(R.id.widget);
            mDescription = itemView.findViewById(R.id.Description);
            mQuantity = itemView.findViewById(R.id.Quantity);
            mPrice = itemView.findViewById(R.id.Price);
            mID = itemView.findViewById(R.id.ID);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate custom layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_elements,parent,false);
        Log.d("ADAPTER", "Parent is " + v.getParent());
        RecyclerViewHolder rvh = new RecyclerViewHolder(v, mListener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Recycler_Item currentItem = mItemsList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mDescription.setText(currentItem.getDescription());
        holder.mQuantity.setText("Quantity : " + currentItem.getQuantity());
        holder.mPrice.setText("Price : $" + currentItem.getPrice());
        holder.mID.setText("ID : " + currentItem.getID());
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }
}
