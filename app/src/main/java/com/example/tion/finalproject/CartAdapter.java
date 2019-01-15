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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.tion.finalproject.SavedActivity.Adapter;
//import static com.example.signoril.earthquakesroomrecyclerview.MainActivity.context;
import static com.example.tion.finalproject.SavedActivity.RecyclerView;
import static com.example.tion.finalproject.SavedActivity.total;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context mContext;  //get the parent context for use in startActivity and to pass to deleteEQID and Room.databaseBuilders
    List<Product> items;
    private Bitmap bitmap = null;

    public CartAdapter(List<Product> items, Context context) {
        this.items = items;
        this.mContext = context;
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowitem3,parent,false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(CartAdapter.ViewHolder holder, int position) {
        final int index = position;
        position = Math.abs(items.size()-position-1);
        holder.time.setText(items.get(position).getName());
        if (!items.get(position).getImg().equals(""))
            new LoadImageFromURL(holder.img1).execute(items.get(position).getImg());
        final int pos = position;
        holder.lay.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                //new MainActivity.DatabaseAsync(mContext).execute(null, index, null, null, null, null, null);
                Toast.makeText(mContext, "Item Deleted!", Toast.LENGTH_SHORT).show();
                CartActivity.listItems.remove(pos);
                //items.remove(index);
                //notifyItemRemoved(pos);
                //notifyItemRangeChanged(pos, items.size());
                notifyDataSetChanged();
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
        public ImageView img1;
        public LinearLayout lay;

        public ViewHolder(View itemView) {
            super(itemView);
            time= itemView.findViewById(R.id.ptitle2);
            img1 = itemView.findViewById(R.id.imageView3);
            lay = itemView.findViewById(R.id.play2);
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
}
