package com.example.tion.finalproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CardViewDataAdapter extends RecyclerView.Adapter<CardViewDataAdapter.ViewHolder> {

    private List<Product> stList;
    private Context mContext;  //need an activity context go over this in class

    public CardViewDataAdapter(List<Product> phrases) {
        this.stList = phrases;
    }

    // Create new views
    @Override
    public CardViewDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rowitem, null);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        mContext = parent.getContext();  //need an activity context go over this in class

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final int pos = position;
        viewHolder.tvDesc.setText(stList.get(position).getBrand());
        if (stList.get(position).getPrice()==0.0){
            viewHolder.tvTitle.setText("N/A");
        }
        else {
            viewHolder.tvTitle.setText("$"+Float.toString(stList.get(position).getPrice()));
        }
        viewHolder.tvName.setText(stList.get(position).getName());
        viewHolder.gotoLink.setText("VIEW AT "+stList.get(position).getDomain());
        viewHolder.gotoLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = stList.get(position).getLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                mContext.startActivity(i);
            }
        });
        viewHolder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                //SavedActivity.listItems.add(stList.get(position));
                new MainActivity.DatabaseAsync(mContext).execute(true, -1, stList.get(position).getName(), stList.get(position).getImg(), Float.toString(stList.get(position).getPrice()), stList.get(position).getDomain(), stList.get(position).getLink());
                //SavedActivity.temp+=stList.get(position).getPrice();
                Toast.makeText(mContext, "Item Added to Cart!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {  // Return the size arraylist
        return stList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {//implements View.OnClickListener{

        public TextView tvName;
        public TextView tvTitle;
        public TextView tvDesc;
        public Button gotoLink;
        public LinearLayout layout;
        public TextView expanded_text;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tvName = (TextView) itemLayoutView.findViewById(R.id.ptitle);
            tvTitle = (TextView) itemLayoutView.findViewById(R.id.pprice);
            tvDesc = (TextView) itemLayoutView.findViewById(R.id.pdesc);
            gotoLink = (Button) itemLayoutView.findViewById(R.id.plink);
            layout = (LinearLayout) itemLayoutView.findViewById(R.id.plin);
        }
    }


    // convenience method for getting data at click position
    public String getItem(int id) {
        return stList.get(id).getName();
    }

    // method to access in activity after updating selection
    public List<Product> getPhraseList() {
        return stList;
    }
}