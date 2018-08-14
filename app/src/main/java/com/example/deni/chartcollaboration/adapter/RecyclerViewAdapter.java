package com.example.deni.chartcollaboration.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.deni.chartcollaboration.R;
import com.example.deni.chartcollaboration.CreateActivity;
import com.example.deni.chartcollaboration.model.Charts;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by sulistiyanto on 07/12/16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Charts> charts; // list nanti yang diambil

    public RecyclerViewAdapter(Context context, List<Charts> charts) {
        this.context = context;
        this.charts = charts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) { // tinggal menggunakan yang telah di deklarasikan
        // menggunakan
        Charts crt = charts.get(position);
        holder.titleX.setText(String.valueOf(position+1)); // untuk men set text. getNim() dari kelas mahasiswa / model
        holder.textViewX.setText(crt.getX()); // untuk men set text. getNim() dari kelas mahasiswa / model
        holder.textViewY.setText(crt.getY());
        holder.textViewChartId.setText(crt.getChartId());
        holder.textViewCategory.setText(crt.getCategory());

    }

    @Override
    public int getItemCount() {
        return charts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // memasukkan komponen di tampilan ke variable menggunakan butterknife
        @BindView(R.id.textX)TextView textViewX;
        @BindView(R.id.titleX)TextView titleX;
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







