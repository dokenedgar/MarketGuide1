package com.edgar.marketguide;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class mainActivityFrag extends Fragment {
    GridView gridView;

    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_activity, container, false);
        perform(view);
        return view;
    }

    private void perform(View view) {
        gridView = (GridView) view.findViewById(R.id.gridView);

        final String[] products = getResources().getStringArray(R.array.products);
        int storeImages[] = {R.drawable.shop, R.drawable.store, R.drawable.storeblue, R.drawable.market,
                            R.drawable.store, R.drawable.shop, R.drawable.market, R.drawable.storeblue,
                            R.drawable.shop, R.drawable.store, R.drawable.storeblue, R.drawable.market,
                            R.drawable.shop, R.drawable.store, R.drawable.storeblue, R.drawable.market,
                            R.drawable.store, R.drawable.shop};
        marketItemsAdapter adapter = new marketItemsAdapter(getActivity(), products, storeImages);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), products[i], Toast.LENGTH_SHORT).show();

                bundle = new Bundle();
                bundle.putString("categoryName",products[i]);
                clickedCategoryFragment clickedCategory = new clickedCategoryFragment();
                clickedCategory.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, clickedCategory)
                        .addToBackStack(null)
                        .commit();

            }
        });
    }

    class marketItemsAdapter extends BaseAdapter {
        Context context;
        LayoutInflater layoutInflater = null;
        String[] products;
        int[] storeImages;
        marketItemsAdapter(Context innerContext, String[] innerProducts, int[] innerImages){
            context = innerContext;
            products = innerProducts;
            storeImages = innerImages;
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return products.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        public class Holder{
            TextView txt;
            ImageView img;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Holder holder;
            if(view == null){
                holder = new Holder();
                view = layoutInflater.inflate(R.layout.gridview_items_layout,null);
                holder.txt = (TextView) view.findViewById(R.id.txt_products);
                holder.img = view.findViewById(R.id.img);

                view.setTag(holder);
            }
            else{
                holder = (Holder)view.getTag();
            }

            holder.txt.setText(products[i]);//.get(i).toString());
            holder.img.setImageResource(storeImages[i]);
            return view;
        }
    }
}
