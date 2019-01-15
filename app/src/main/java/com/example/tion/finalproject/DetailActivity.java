package com.example.tion.finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tion.finalproject.R;

import java.util.ArrayList;
import java.util.List;


public class DetailActivity extends AppCompatActivity{
    private String UPC = null;
    List<Product> Products = new ArrayList<>();
    RecyclerView recyclerView;
    TextView noEventsFound;
    ProductsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Offers"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(ContextCompat.getColor(DetailActivity.this,
                            R.color.black));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(ContextCompat.getColor(DetailActivity.this,
                            R.color.colorPrimary));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menuQuit:
                finish();
                break;

            case R.id.menuViewSaved:
                Intent i = new Intent(DetailActivity.this, SavedActivity.class);
                startActivity(i);
                break;

            default:
                toast("Hit Default! Should not be here!!");
                break;
        }
        return true;
    }
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

//    private class DatabaseAsync extends AsyncTask<Object, Void, List<Product>> {
//        public ProductsAdapter.ClickListener c;  //need to get main
//
//        // Constructor providing a reference to the views in MainActivity
//        public DatabaseAsync(ProductsAdapter.ClickListener  c) {
//            this.c = c;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }

//        @Override
//        protected List<Product> doInBackground(Object... params) {
//
//            Boolean shouldUpdate = (Boolean) params[0];
//            int position = (int) params[1];
//            String title = (String) params[2];
//            String detail = (String) params[3];
//            Float cost = (Float) params[4];
//            String pub = (String) params[5];
//            String desc = (String) params[6];
//
//            //check whether to add add or update Product based on if shouldUpdate is null
//            if (shouldUpdate != null) {
//                //update Product if shouldUpdate is true
//                //add Product if shouldUpdate is false
//                Product Product = new Product();
//                Product.setName(title);
//                Product.setBrand(detail);
//                Product.setPrice(cost);
//                Product.setDomain(pub);
//                Product.setLink(desc);
//
//                //add Product into the database
//                AppDatabase.getAppDatabase(getApplicationContext()).databaseInterface().addProduct(Product);
//
//
//            } else { //so no update since shouldUpdate == null
//                //delete all if postion is = -2, really bad, i should fix this
//                if (position == -2)
//                    //delete all Products  from database
//                    AppDatabase.getAppDatabase(getApplicationContext()).databaseInterface().dropTheTable();
//
//                    //delete Product
//                else if (position != -1) { //-1 means delete a specific Product
//                    Product Product = Products.get(position);
//
//                    //delete Product from database
//                    AppDatabase.getAppDatabase(getApplicationContext()).databaseInterface().deleteProduct(Product);
//                }
//            }
//
//            //get Products from database, also not a great way to do this
//            List<Product> Products = AppDatabase.getAppDatabase(getApplicationContext()).databaseInterface().getAllItems();
//            return Products;
//
//        }
//
//        @Override
//        protected void onPostExecute(List<Product> items) {
//
//            //get list of Products from doInBackground()
//            Products = items;
//
//            adapter = new ProductsAdapter(Products, getApplicationContext());
//            adapter.setClickListener(c); //this is important since need MainActivity.this
//
//            recyclerView.setAdapter(adapter);
//
//            //shows NO ProductS FOUND when list is empty
//            checkListEmptyOrNot();
//        }

//        public void checkListEmptyOrNot() {
//            if (Products.isEmpty())
//                noEventsFound.setVisibility(View.VISIBLE);
//            else
//                noEventsFound.setVisibility(View.GONE);
//        }
//    }
}
