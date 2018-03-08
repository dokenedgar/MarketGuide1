package com.edgar.marketguide;

import android.content.Context;
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

public class categoriesAdapter extends BaseAdapter {
    Context context;
    ArrayList shops;
    private static LayoutInflater inflater = null;

    public categoriesAdapter(Context context, ArrayList shops) {
        this.context = context;
        this.shops = shops;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return shops.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public static class Holder{
        //TextView tv;
        //ImageView image;
        TextView shopNum;

    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null){
            holder = new Holder();
            view = inflater.inflate(R.layout.listview_layout,null);
            // holder.AccName = (TextView) view.findViewById(R.id.AccoName);
            holder.shopNum = (TextView) view.findViewById(R.id.shopNumber);
            //holder.image =  (ImageView) view.findViewById(R.id.accoImage);
            view.setTag(holder);
        }
        else {
            holder = (Holder)view.getTag();
        }

   /*     Picasso.with(context)
                .load(images.get(i).toString())
                .fit()
                .into(holder.image);
       */ //** FOR WHEN WORKIMG WITH IMAGES,
        holder.shopNum.setText(shops.get(i).toString());

        return view;
    }
}
