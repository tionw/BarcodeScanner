package com.example.tion.finalproject;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SavedActivity extends AppCompatActivity {
    public static ArrayList<Product> items = new ArrayList<>();
    public static List<Product> listItems = new ArrayList<>();
    public static RecyclerView RecyclerView;
    public static RecyclerView.Adapter Adapter;
    public static TextView total;
    View rootView;
    public AppDatabase db;
    public static Context context;
    public static float temp;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_activity);
        SavedActivity.context = getApplicationContext();
        RecyclerView = (RecyclerView) findViewById(R.id.recycler_view1);
        RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        total = (TextView) findViewById(R.id.total1);
        Adapter = new ProductsAdapter(listItems, context);

        if (listItems.size()!=0) {
            for (Product p : listItems) {
                temp+=p.getPrice();
            }
            DecimalFormat numberFormat = new DecimalFormat("#.00");
            total.setText("Total: $"+numberFormat.format(temp));
        }
        else{
            temp=(float)0.00;
            total.setText("Total: $0.00");
        }
        RecyclerView.setAdapter(Adapter);
    }

    @Override
    protected void onStop(){
        super.onStop();
        //Toast.makeText(context, "stopped", Toast.LENGTH_SHORT).show();
        temp=(float)0.00;
        finish();
    }

    @Override
    protected void onPause(){
        super.onPause();
        //Toast.makeText(context, "paused", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuQuit:
                finish();
                break;

            case R.id.menuDeleteAll:
                new MainActivity.DatabaseAsync(context).execute(null, -2, null, null, null, null, null);
                toast("All items deleted!");
                Intent intent2 = getIntent();
                finish();
                startActivity(intent2);
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
}