package com.example.tion.finalproject;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
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

public class CartActivity extends AppCompatActivity {
    public static List<Product> listItems = new ArrayList<>();
    public static RecyclerView RecyclerView;
    public static RecyclerView.Adapter Adapter;
    public AppDatabase db;
    public static Context context;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);
        SavedActivity.context = getApplicationContext();
        RecyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        Adapter = new CartAdapter(listItems, context);

        RecyclerView.setAdapter(Adapter);
    }

    @Override
    protected void onStop(){
        super.onStop();
        finish();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }
}