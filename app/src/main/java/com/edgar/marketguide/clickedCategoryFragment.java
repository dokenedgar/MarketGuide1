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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class clickedCategoryFragment extends Fragment {

    String categoryName;
    View view;
    Context context;
    TextView TxtCategory, txtError;
    ListView listView;
    ArrayList shopName;
    Button btnReload;

    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("shopscategories/menshoes");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_clicked_category, container, false);
        view = inflater.inflate(R.layout.fragment_clicked_category,container,false);
        //createAlert();
        context = getActivity();
        listView =  view.findViewById(R.id.list);
        TxtCategory = view.findViewById(R.id.txtCategory);
        txtError = view.findViewById(R.id.txtError);
        btnReload = view.findViewById(R.id.btnError);
        categoryName = getArguments().getString("categoryName","nothing");
        //TxtCategory.setText(getArguments().getString("categoryName","nothing"));
        TxtCategory.setText(categoryName);

        mDocRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String shop = documentSnapshot.getString("shopnum");

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
                    listView.setAdapter(adapter);
                    //Add listview item click listener
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Toast.makeText(getActivity(),"pos= "+i+", id= "+l,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //txtError.setVisibility(View.VISIBLE);
                //txtError.setText(shop);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                btnReload.setVisibility(View.VISIBLE);
                btnReload.setText("FAILED NIGGER");
            }
        });

        if (isOnline()){
           // reload(); USES ASYNCTASK
        }
        else{
            Toast.makeText(getActivity(), "Please enable data connection", Toast.LENGTH_LONG).show();
            btnReload.setVisibility(View.VISIBLE);
            btnReload.setText("Reload..");
        }

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()){
                    reload();
                }
                else{
                    Toast.makeText(getActivity(), "Please enable data connection", Toast.LENGTH_LONG).show();
                    //btnReload.setVisibility(View.VISIBLE);
                    //btnReload.setText("Reload..");
                }
            }
        });
        return view;
    }

    public boolean isOnline(){
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void btnReloadClick(View view){

    }
    private void reload() {
        getShops shops = new getShops();
        shops.execute();
    }



    public class getShops extends AsyncTask <Void,Void,Void>{

        ProgressDialog pd = new ProgressDialog(getActivity());
        String json_string,RAW_Json;
        int responseCode;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("Loading " + categoryName+ " shops, Please Wait..");
            pd.setIndeterminate(true);
            pd.setCanceledOnTouchOutside(false);
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            URL url;
            HttpURLConnection urlConnection;
            String urlParams;

            try {
                url = new URL("http://192.168.43.228/market_guide/getShops.php"); //wireless mtn correct ip add
                urlConnection = (HttpURLConnection)url.openConnection();
                //SETTING CONNECTION TIMEOUT
                urlConnection.setConnectTimeout(5000); //15000
                urlConnection.setReadTimeout(5000);

                    urlParams = "category="+categoryName;

                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                DataOutputStream ds = new DataOutputStream(urlConnection.getOutputStream());
                ds.writeBytes(urlParams);
                ds.flush();
                ds.close();
                responseCode = urlConnection.getResponseCode();
                shopName = new ArrayList();
                //if response code isnt in 200 or 2xx, i.e successful, do not continue
                if (200 <= responseCode && responseCode <=299){
                    InputStream is = urlConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();

                    while ((json_string=br.readLine()) != null){
                        sb.append(json_string + "\n");
                    }
                    br.close();
                    is.close();
                    // urlConnection.disconnect();
                    RAW_Json = sb.toString();
                    JSONObject jsonObject = new JSONObject(RAW_Json);
                    JSONArray jsonArray = jsonObject.getJSONArray("shops");
                    //houses = new ArrayList();

                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        shopName.add(jObject.getString("shop_number"));
                        //images.add(jObject.getString("image"));
                        //imageDetails.add(jObject.getString("imageUrl"));
                    }

                }//end of response 2XX
                else{
                    //urlConnection.getResponseMessage();
                }
                urlConnection.disconnect();
            } //close of try block
            catch (java.io.IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);

            Toast.makeText(getActivity(), " Response code : " + responseCode, Toast.LENGTH_LONG).show();
            pd.dismiss();

            if (200 <= responseCode && responseCode <=299){
                //Response OK proceed
                if(shopName.isEmpty()){
                    txtError.setVisibility(View.VISIBLE);
                    txtError.setText("No data for selected category, would be added very soon!");
                   // txtError.setError("No data for selected category, would be added very soon!");
                }
                else{
                    categoriesAdapter adapter = new categoriesAdapter(getActivity(), shopName);
                    //old version //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,values);
                    listView.setAdapter(adapter);

                    //Add listview item click listener
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            // i = position, l = id
                            //use position to get the individual contents. Remember its an array, so its index starts at zero, 0
                            Toast.makeText(getActivity(),"pos= "+i+", id= "+l,Toast.LENGTH_SHORT).show();

                        }
                    });
                }


            }
            else{
                Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_LONG).show();
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Error!")
                        .setCancelable(false)
                        .setMessage("Problem loading content. Try again?")
                        .setPositiveButton("Reload", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //RELOAD . should not be that bcos news can't load other tabs could't be clicked. allow other tabs 2 b clicked. simple english
                                reload();

                            }
                        })
                        .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Exit App
                                System.exit(0);
                                //getActivity().finish();

                            }
                        });
                AlertDialog showAlert = alert.create();
                showAlert.show();
            }

        }


    }

}
