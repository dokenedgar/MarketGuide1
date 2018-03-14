package com.edgar.marketguide;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class clickedCategoryFragment extends Fragment {

    String categoryName, shortCategoryName;
    View view;
    Context context;
    TextView TxtCategory, txtError;
    ListView listView;
    ArrayList shopName;
    Button btnReload;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;


    private DocumentReference mDocRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_clicked_category, container, false);
        view = inflater.inflate(R.layout.fragment_clicked_category,container,false);
        //createAlert();
        context = getActivity();
        listView =  view.findViewById(R.id.list);
        recyclerView = view.findViewById(R.id.recycleView);

        TxtCategory = view.findViewById(R.id.txtCategory);
        txtError = view.findViewById(R.id.txtError);
        btnReload = view.findViewById(R.id.btnError);
        categoryName = getArguments().getString("categoryName","nothing");
        //TxtCategory.setText(getArguments().getString("categoryName","nothing"));
        TxtCategory.setText(categoryName);

        shortCategoryName = getShortCategoryName(categoryName);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Fetching Data");
        progressDialog.show();
        mDocRef = FirebaseFirestore.getInstance().document("shopscategories/"+shortCategoryName);

        mDocRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               // String shop = documentSnapshot.getString("shopnum");

                if(documentSnapshot.exists()){
                    Map<String, Object> listShops = documentSnapshot.getData();
                    //li.forEach( (k,v) -> [do something with key and value] );
                    shopName = new ArrayList();
                    //listShops.forEach((k, v) -> shopName.add(v.toString()););
                    for (Map.Entry<String, Object> entry : listShops.entrySet()){
                        //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                        shopName.add(entry.getValue().toString());
                    }
                    if(shopName.isEmpty()){
                        txtError.setVisibility(View.VISIBLE);
                        txtError.setText("No data for selected category, would be added very soon!");
                    }
                    else{
                        categoriesAdapter adapter = new categoriesAdapter(getActivity(), shopName);
                        //listView.setAdapter(adapter);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                }
                progressDialog.dismiss();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                btnReload.setVisibility(View.VISIBLE);
                btnReload.setText("Filed to load data");
                progressDialog.dismiss();
            }
        });


        return view;
    }

    private String getShortCategoryName(String LongcategoryName) {
        String shortName = "menshoes";
        switch (LongcategoryName){
            case "Children Clothes":
                shortName = "childrenclothes";
                break;
            case "Women Clothes":
                shortName = "womenclothes";
                break;
            case "Men Clothes":
                shortName = "menclothes";
                break;
            case "Laces / Wrappers":
                shortName = "wrappers";
                break;
            case "Children Shoes":
                shortName = "childrenshoes";
                break;
            case "Women Shoes":
                shortName = "womenshoes";
                break;
            case "Men Shoes":
                shortName = "menshoes";
                break;
            case "Bags - Wedding/Travelling/School":
                shortName = "bags";
                break;
            case "Bookshops":
                shortName = "bookshops";
                break;
            case "Fairly Used Clothes":
                shortName = "failyusedclothes";
                break;

            case "Palm / Vegetable Oils":
                shortName = "palmvegoils";
                break;

            case "Curtains":
                shortName = "curtains";
                break;

            case "General Provisions":
                shortName = "provisions";
                break;

            case "Make up / Perfumes / Creams":
                shortName = "cosmetics";
                break;
            case "Sport Items":
                shortName = "sportitems";
                break;
            case "Household Electronics":
                shortName = "electronics";
                break;
            case "Furniture":
                shortName = "furniture";
                break;
            case "Foreign Rice / Flours":
                shortName = "riceflours";
                break;
        }
        return shortName;
    }

    public boolean isOnline(){
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

}
