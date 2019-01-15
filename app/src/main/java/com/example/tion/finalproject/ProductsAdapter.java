package com.example.tion.finalproject;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.tion.finalproject.SavedActivity.Adapter;
//import static com.example.signoril.earthquakesroomrecyclerview.MainActivity.context;
import static com.example.tion.finalproject.SavedActivity.RecyclerView;
import static com.example.tion.finalproject.SavedActivity.total;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    Context mContext;  //get the parent context for use in startActivity and to pass to deleteEQID and Room.databaseBuilders
    List<Product> items;
    private Bitmap bitmap = null;

    public ProductsAdapter(List<Product> items, Context context) {
        this.items = items;
        this.mContext = context;
    }

    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowitem2,parent,false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ProductsAdapter.ViewHolder holder, final int position) {
        holder.time.setText(items.get(position).getName());
        if (!items.get(position).getImg().equals(""))
            new LoadImageFromURL(holder.img1).execute(items.get(position).getImg());
        if (items.get(position).getPrice()==0.0){
            holder.mag2.setText("N/A");
        }
        else {
            holder.mag2.setText("$"+Float.toString(items.get(position).getPrice()));
        }
        holder.but1.setText("View at " + items.get(position).getDomain());
        holder.but1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = items.get(position).getLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                mContext.startActivity(i);
            }
        });

        final int index = position;
        holder.lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent map = new Intent(mContext, MapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("city",items.get(index));
                map.putExtras(bundle);
                DownloadJSON task = new DownloadJSON();
                task.execute(items.get(index).getDomain().replace(".com",""));
                //Toast.makeText(mContext, items.get(index).getName(), Toast.LENGTH_SHORT).show();
                mContext.startActivity(map);
            }
        });

        holder.lay.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                new MainActivity.DatabaseAsync(mContext).execute(null, position, null, null, null, null, null);
                Toast.makeText(mContext, "Item Deleted!", Toast.LENGTH_SHORT).show();
                SavedActivity.temp-=items.get(position).getPrice();
                DecimalFormat numberFormat = new DecimalFormat("#.00");
                SavedActivity.total.setText("Total: $"+numberFormat.format(SavedActivity.temp));
                items.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, items.size());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        else
            return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView time;
        public TextView mag2;
        public Button but1;
        public ImageView img1;
        public LinearLayout lay;

        public ViewHolder(View itemView) {
            super(itemView);
            time= itemView.findViewById(R.id.ptitle1);
            mag2 = itemView.findViewById(R.id.pprice1);
            but1 = itemView.findViewById(R.id.plink1);
            img1 = itemView.findViewById(R.id.imageView2);
            lay = itemView.findViewById(R.id.play1);
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
            InputStream in = null;
            try {
                in = openHttpConnection(urlStr);
                publishProgress();
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                }
                bitmap = BitmapFactory.decodeStream(in);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException ee) {
//                }
                publishProgress();
                in.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute (Bitmap bitmap){
            bitmapImgView.setImageBitmap(bitmap);
        }
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
                final String BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

                final String QUERY_PARAM = "upc";

                String location = Double.toString(MapActivity.lat)+","+ Double.toString(MapActivity.lng);

                Uri builtURI = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter("location", "42.3389989,-71.1684668")
                        .appendQueryParameter("radius", "5000")
                        .appendQueryParameter("keyword", queryString)
                        .appendQueryParameter("key", "AIzaSyCJbvP7Cck4LxR_XQH4Eti68HKPhdZHrWE")
                        .build();

                URL requestURL = new URL(builtURI.toString());

                // Open the network connection.
                urlConnection = (HttpURLConnection) requestURL.openConnection();
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
            ArrayList<Location2> locations = new ArrayList<>();

            try {
                // Convert the response into a JSON object.
                JSONObject jsonObject = new JSONObject(s); //get top level object
                JSONArray itemsArray = jsonObject.getJSONArray("results"); //first array of book objects
                Double lat;
                Double lng;
                String name = null;
                Boolean opennow = null;
                int j=0;
                try {
                    while (j < itemsArray.length()) {
                        JSONObject store = itemsArray.getJSONObject(j);
                        JSONObject geo = store.getJSONObject("geometry");
                        JSONObject loca = geo.getJSONObject("location");
                        lat = (Double) loca.getDouble("lat");
                        lng = (Double) loca.getDouble("lng");
                        name = store.getString("name");
                        //Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(mContext, Double.toString(lat), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(mContext, Double.toString(lng), Toast.LENGTH_SHORT).show();
                        JSONObject open = store.getJSONObject("opening_hours");
                        opennow = open.getBoolean("open_now");
                        Location2 locat = new Location2();
                        locat.setLat(lat);
                        locat.setLon(lng);
                        locat.setName(name);
                        locat.setLink(opennow);
                        locations.add(locat);
                        j++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            MapActivity.locas = locations;
        }

    }
}
