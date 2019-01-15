package com.example.tion.finalproject;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class DetailFragment2 extends Fragment{
    ArrayList<Product> offers;
    private RecyclerView RecyclerView;
    private CardViewDataAdapter Adapter;
    private String UPC = null;
    private String msg = null;
    View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.detail_frag2, container, false);
        setRetainInstance(true);

        RecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        RecyclerView.setHasFixedSize(true);

        RecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); //note this need/

        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        if (extras != null) UPC = extras.getString("key");

        DownloadJSON task = new DownloadJSON();
        task.execute(UPC);
        return rootView;
    }

    public class DownloadJSON extends AsyncTask<String, Integer, String> {
        String str;

        public DownloadJSON() {
        }

        @Override
        protected String doInBackground(String... params) {
            String queryString = params[0];

            // Set up variables for the try block that need to be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String bookJSONString = null;

            try {
                final String BASE_URL = "https://api.upcitemdb.com/prod/trial/lookup?";

                final String QUERY_PARAM = "upc";

                Uri builtURI = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, queryString)
                        .build();

                URL requestURL = new URL(builtURI.toString());

                // Open the network connection.
                urlConnection = (HttpURLConnection) requestURL.openConnection();

                urlConnection.setReadTimeout(60 * 1000);
                urlConnection.setConnectTimeout(60 * 1000);

//                urlConnection.setRequestProperty("Content-Type", "application/json");
//                urlConnection.setRequestProperty("user_key", "4b19ac23720b02146b0ec7f6cfe0319b");
//                urlConnection.setRequestProperty("key_type", "3scale");

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Get the InputStream.
                InputStream inputStream = urlConnection.getInputStream();

                // Read the response string into a StringBuilder.
                StringBuilder builder = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                if (builder.length() == 0) {
                    return null;
                }
                bookJSONString = builder.toString();

                // Catch errors.
            } catch (IOException e) {
                e.printStackTrace();

                // Close the connections.
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            str = bookJSONString;
            return str;
        }
        // put results in ListView
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            offers = new ArrayList<>();

            try {
                // Convert the response into a JSON object.
                JSONObject jsonObject = new JSONObject(s); //get top level object
                // Get the JSONArray of book items.
                JSONArray itemsArray = jsonObject.getJSONArray("items"); //first array of book objects
                String merchant = null;
                String domain = null;
                String otitle = null;
                String link = null;
                String imagelink = null;
                float price;

                JSONObject product = itemsArray.getJSONObject(0);

                try {
                    JSONArray offersArray = product.getJSONArray("offers");
                    JSONArray imagesArray = product.getJSONArray("images");
                    imagelink = (String) imagesArray.get(0);
                    int j = 0;
                    while (j < offersArray.length()) {
                        JSONObject offer = offersArray.getJSONObject(j);
                        otitle = offer.getString("title");
                        merchant = offer.getString("merchant");
                        domain = offer.getString("domain");
                        price = BigDecimal.valueOf(offer.getDouble("price")).floatValue();
                        link = offer.getString("link");
                        Product p = new Product();
                        p.setName(otitle);
                        p.setBrand(merchant);
                        p.setPrice(price);
                        //Toast.makeText(getActivity(), "price: "+price, Toast.LENGTH_SHORT).show();
                        p.setDomain(domain);
                        p.setLink(link);
                        p.setImg(imagelink);
                        offers.add(p);
                        j++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // create an Object for Adapter
                Adapter = new CardViewDataAdapter(offers);

                // set the adapter object to the Recyclerview
                RecyclerView.setAdapter(Adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
