package com.example.deni.chartcollaboration.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.deni.chartcollaboration.CreateActivity;
import com.example.deni.chartcollaboration.R;
import com.example.deni.chartcollaboration.model.ChartManager;
import com.example.deni.chartcollaboration.model.Charts;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by deni on 20/07/2018.
 */

public class RecyclerChartManagerAdapter extends RecyclerView.Adapter<RecyclerChartManagerAdapter.ViewHolder>{
    private Context context;
    private List<ChartManager> chartmanagers; // list nanti yang diambil

    public RecyclerChartManagerAdapter(Context context, List<ChartManager> chartmanagers) {
        this.context = context;
        this.chartmanagers = chartmanagers;
    }

    @Override
    public RecyclerChartManagerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chart_manager_list_view, parent, false);
        RecyclerChartManagerAdapter.ViewHolder holder = new RecyclerChartManagerAdapter.ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerChartManagerAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return chartmanagers.size();
    }

    public class ViewHolder extends RecyclerChartManagerAdapter.ViewHolder implements View.OnClickListener{

        // memasukkan komponen di tampilan ke variable menggunakan butterknife
        @BindView(R.id.textX)TextView textViewX;
        @BindView(R.id.textY)TextView textViewY;
        @BindView(R.id.textChartId)TextView textViewChartId;
        @BindView(R.id.textCategory)TextView textViewCategory;

        public ViewHolder(View itemView) { // main nya
            super(itemView);
            ButterKnife.bind(this, itemView); // harus di deklarasikan butter knifenya
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String x = textViewX.getText().toString();
            String y = textViewY.getText().toString();
            String chartId = textViewChartId.getText().toString();
            String category = textViewCategory.getText().toString();

//            Intent i = new Intent(context, CreateActivity.class);
//            i.putExtra("x",x);
//            i.putExtra("y",y);
//            i.putExtra("chartId",chartId);
//            i.putExtra("category",category);
//            context.startActivity(i);

        }

    }
}
