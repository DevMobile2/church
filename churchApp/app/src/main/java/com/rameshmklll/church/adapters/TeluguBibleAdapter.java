package com.rameshmklll.church.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rameshmklll.church.R;
import com.rameshmklll.church.pojos.TeluguBiblePojo;

import java.util.ArrayList;

/**
 * Created by MRamesh on 18-12-2017.
 */

public class TeluguBibleAdapter extends RecyclerView.Adapter<TeluguBibleAdapter.MyViewHolder> {

    private ArrayList<TeluguBiblePojo> dataSet;

    public void clearDataSet() {
        dataSet=new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);

        }
    }

    public TeluguBibleAdapter(ArrayList<TeluguBiblePojo> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.telugu_bible_layout, parent, false);



        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        textViewName.setText(dataSet.get(listPosition).getVersion()+" . "+dataSet.get(listPosition).getName());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
