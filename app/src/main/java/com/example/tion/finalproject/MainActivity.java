package com.example.tion.finalproject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private Button scan;
    private Button saved;
    private Button cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new DatabaseAsync(getApplicationContext()).execute(null, -1, null, null, null, null, null);
        //new DatabaseAsync(getApplicationContext()).execute(null, -2, null, null, null, null, null);

        scan = (Button) findViewById(R.id.button);
        scan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(i);
            }
        });
        saved = (Button) findViewById(R.id.button2);
        saved.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SavedActivity.class);
                startActivity(i);
            }
        });
        cart = (Button) findViewById(R.id.button3);
        cart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CartActivity.class);
                startActivity(i);
            }
        });
    }

    static class DatabaseAsync extends AsyncTask<Object, Void, List<Product>> {//need to get main
        Context con;
        // Constructor providing a reference to the views in MainActivity
        public DatabaseAsync(Context con) {
            this.con = con;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Product> doInBackground(Object... params) {

            Boolean shouldUpdate = (Boolean) params[0];
            int position = (int) params[1];
            String title = (String) params[2];
            String img = (String) params[3];
            String price = (String) params[4];
            String domain = (String) params[5];
            String link = (String) params[6];

            //check whether to add add or update Product based on if shouldUpdate is null
            if (shouldUpdate != null) {
                //update Product if shouldUpdate is true
                //add Product if shouldUpdate is false
                Product Product = new Product();
                    Product.setName(title);
                    Product.setImg(img);
                    Product.setPrice(Float.valueOf(price));
                    Product.setDomain(domain);
                    Product.setLink(link);

                //add Product into the database
                AppDatabase.getAppDatabase(con).databaseInterface().addProduct(Product);

            } else { //so no update since shouldUpdate == null
                //delete all if postion is = -2, really bad, i should fix this
                if (position == -2)
                    //delete all Products  from database
                    AppDatabase.getAppDatabase(con).databaseInterface().dropTheTable();

                    //delete Product
                else if (position != -1) { //-1 means delete a specific Product
                    Product Product = SavedActivity.listItems.get(position);

                    //delete Product from database
                    AppDatabase.getAppDatabase(con).databaseInterface().deleteProduct(Product);
                }
            }
            //get Products from database, also not a great way to do this
            List<Product> Products = AppDatabase.getAppDatabase(con).databaseInterface().getAllItems();
            return Products;
        }

        @Override
        protected void onPostExecute(List<Product> items) {
            //get list of Products from doInBackground()
            SavedActivity.listItems = items;
        }
    }


}
