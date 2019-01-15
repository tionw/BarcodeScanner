package com.example.tion.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;

import javax.sql.DataSource;

public class DetailFragment1 extends Fragment{
    ArrayList<Product> offers;
    Product temp = new Product();
    private String UPC = null;
    private TextView name;
    private TextView viewDesc;
    private ImageView image;
    private ProgressBar progressBar;
    private Bitmap bitmap = null;
    View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.detail_frag1, container, false);
        setRetainInstance(true);
        name = (TextView) rootView.findViewById(R.id.title1);
        viewDesc = (TextView) rootView.findViewById(R.id.desc1);
        image = (ImageView) rootView.findViewById(R.id.imageView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);

        Bundle extras = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        if (extras != null) UPC = extras.getString("key");

        DownloadJSON task = new DownloadJSON();
        task.execute(UPC);
        return rootView;
    }

    public class DownloadJSON extends AsyncTask<String, Integer, String> {
        ProgressBar bar;
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

                int responseCode = urlConnection.getResponseCode();
                System.out.println(Integer.toString(responseCode));

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
                String code = jsonObject.getString("code");
                JSONArray itemsArray = jsonObject.getJSONArray("items"); //first array of book objects
                String title = null;
                String brand = null;
                String imagelink = null;
                String desc = null;
                String merchant = null;
                String domain = null;
                String otitle = null;
                String link = null;
                float price;
                JSONObject product = itemsArray.getJSONObject(0);

                try {
                    title = product.getString("title");
                    brand = product.getString("brand");
                    desc = product.getString("description");
                    JSONArray imagesArray = product.getJSONArray("images");
                    imagelink = (String) imagesArray.get(0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                // If both are found, display the result.
                if (title != null) {
                    name.setText(title);
                } else {
                    name.setText("PRODUCT NOT FOUND");
                }
//                if (brand != null){
//                    viewBrand.setText(brand);
//                } else{
//                    viewBrand.setText("BRAND NOT FOUND");
//                }
                if (desc != null){
                    viewDesc.setText(desc);
                } else{
                    viewDesc.setText("DESCRIPTION NOT FOUND");
                }
                if (imagelink != null){
                    new LoadImageFromURL(image).execute(imagelink);
                    temp.setImg(imagelink);
                } else{
                    progressBar.setVisibility(View.GONE);
                    image.setImageResource(R.drawable.notfound);
                    temp.setImg("");
                }
                temp.setName(title);

                CartActivity.listItems.add(temp);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private InputStream openHttpConnection(String urlStr) {
        InputStream in = null;
        int resCode;

        try {
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();

            if (!(urlConn instanceof HttpURLConnection)) {
                throw new IOException("URL is not an Http URL");
            }
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            resCode = httpConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }

        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    private class LoadImageFromURL extends AsyncTask<String, Integer, Bitmap> {
        ImageView bitmapImgView;

        public LoadImageFromURL(ImageView bmImgView){
            bitmapImgView = bmImgView;
        }

        // download image
        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            String urlStr = params[0];
            int count=0;
            InputStream in = null;
            try {
                in = openHttpConnection(urlStr);
                count+=33;
                publishProgress(count);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                count+=33;
                publishProgress(count);
                bitmap = BitmapFactory.decodeStream(in);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ee) {
                }
                count+=33;
                publishProgress(count);
                in.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute (Bitmap bitmap){
            progressBar.setVisibility(View.GONE);
            bitmapImgView.setImageBitmap(bitmap);
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            progressBar.setProgress(values[0]);
        }
    }

}
