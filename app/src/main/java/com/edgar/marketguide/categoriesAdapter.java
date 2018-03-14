package com.edgar.marketguide;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Edgar on 03-Mar-18.
 */

public class categoriesAdapter extends RecyclerView.Adapter<categoriesAdapter.myViewHolder> {

    private LayoutInflater inflater;
    ArrayList shops;

    public categoriesAdapter(Context context, ArrayList shops){
        inflater = LayoutInflater.from(context);
        this.shops = shops;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.listview_layout, parent, false);
        myViewHolder holder = new myViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        holder.shopNum.setText(shops.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView shopNum;
        public myViewHolder(View itemView) {
            super(itemView);
            shopNum = itemView.findViewById(R.id.shopNumber);
            cardView = itemView.findViewById(R.id.cv);

        }

    }


}
