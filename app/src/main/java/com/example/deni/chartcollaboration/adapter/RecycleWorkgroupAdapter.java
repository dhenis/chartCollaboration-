package com.example.deni.chartcollaboration.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.deni.chartcollaboration.ChartManagerActivity;
import com.example.deni.chartcollaboration.CreateActivity;
import com.example.deni.chartcollaboration.R;

import com.example.deni.chartcollaboration.model.Charts;
import com.example.deni.chartcollaboration.model.Workgroups;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by deni on 18/07/2018.
 */
public class RecycleWorkgroupAdapter extends RecyclerView.Adapter<RecycleWorkgroupAdapter.ViewHolder> {


    private Context context;
    private List<Workgroups> workgroups;

    public RecycleWorkgroupAdapter(Context context, List<Workgroups> workgroups) {
        this.context = context;
        this.workgroups = workgroups;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_workgroup_list_view, parent, false);
        RecycleWorkgroupAdapter.ViewHolder holder = new RecycleWorkgroupAdapter.ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecycleWorkgroupAdapter.ViewHolder holder, int position) { // tinggal menggunakan yang telah di deklarasikan
        // menggunakan
        Workgroups wg = workgroups.get(position);
        Log.d("@@position ; ", String.valueOf(position));

        holder.workgroup_name.setText(wg.getWorkgroupName()); // untuk men set text. getNim() dari kelas mahasiswa / model
        holder.workgroup_access.setText(wg.getWorkgroupAccess());
        holder.workgroup_id.setText(wg.getIdWorkgroup());
        holder.update_time.setText(wg.getUpdateTime());

    }

    @Override
    public int getItemCount() {
        return workgroups.size();
//        return workgroups.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // memasukkan komponen di tampilan ke variable menggunakan butterknife
        @BindView(R.id.workgroup_name)TextView workgroup_name;
        @BindView(R.id.workgroup_access)TextView workgroup_access;
        @BindView(R.id.workgroup_id)TextView workgroup_id;
        @BindView(R.id.update_time)TextView update_time;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String workgroup_name_var = workgroup_name.getText().toString();
            String workgroup_access_var = workgroup_access.getText().toString();
            String workgroup_id_var = workgroup_id.getText().toString();
            String update_time_var = update_time.getText().toString();

            Intent i = new Intent(context, ChartManagerActivity.class);
            i.putExtra("workgroup_name",workgroup_name_var);
            i.putExtra("workgroup_access",workgroup_access_var);
            i.putExtra("workgroup_id",workgroup_id_var);
            i.putExtra("update_time",update_time_var);
            context.startActivity(i);

//            Intent pindah = new Intent(RecyclerChartManagerAdapter.this, CreateActivity.class);
////                        i.putExtra("id_chart_manager",jsonObj.getString("id_chart_manager"));
//            pindah.putExtra("chartName",jsonObj.getString("id_chart_manager"));
//            pindah.putExtra("chartId",jsonObj.getString("id_chart_manager"));
////                        i.putExtra("chartTimeCreated",jsonObj.getString("id_chart_manager"));

        }

    }
}
