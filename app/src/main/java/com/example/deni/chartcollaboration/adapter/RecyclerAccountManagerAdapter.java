package com.example.deni.chartcollaboration.adapter;

import android.accounts.Account;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.deni.chartcollaboration.AccountManagerActivity;
import com.example.deni.chartcollaboration.ChartManagerActivity;
import com.example.deni.chartcollaboration.MainActivity;
import com.example.deni.chartcollaboration.R;
import com.example.deni.chartcollaboration.WorkgroupActivity;
import com.example.deni.chartcollaboration.api.RegisterAPI;
import com.example.deni.chartcollaboration.model.AccountManager;
import com.example.deni.chartcollaboration.model.Value;
import com.example.deni.chartcollaboration.model.ValueAccountManager;
import com.example.deni.chartcollaboration.model.Workgroups;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by deni on 28/07/2018.
 */

public class RecyclerAccountManagerAdapter extends RecyclerView.Adapter<RecyclerAccountManagerAdapter.ViewHolder> {

    public static final String URL = "http://dhenis.com/charts/";

    private Context context;
    private List<AccountManager> account;

    public RecyclerAccountManagerAdapter(Context context, List<AccountManager> account) {
        this.context = context;
        this.account = account;
    }


    @Override
    public RecyclerAccountManagerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_account_manager_list_view, parent, false);
        RecyclerAccountManagerAdapter.ViewHolder holder = new RecyclerAccountManagerAdapter.ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerAccountManagerAdapter.ViewHolder holder, int position) {
        // menggunakan
        AccountManager acc = account.get(position);


        holder.account_id.setText(acc.getIdAccount());
        holder.account_username.setText(acc.getUsername());
//        holder.workgroup_id.setText(acc.getIdWorkgroup());
//        holder.update_time.setText(acc.getUpdateTime());

    }

    @Override
    public int getItemCount() {
        return account.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // memasukkan komponen di tampilan ke variable menggunakan butterknife
        @BindView(R.id.AccountId)TextView account_id;
        @BindView(R.id.AccountUsername)TextView account_username;
//        @BindView(R.id.workgroup_id)TextView workgroup_id;
//        @BindView(R.id.update_time)TextView update_time;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            final String account_id_var = account_id.getText().toString();
            String AccountUsername_var = account_username.getText().toString();

            Log.d("@@aa::",account_id_var);
            Log.d("@@bb::",AccountUsername_var);

//            Toast.makeText(RecyclerAccountManagerAdapter.this, AccountUsername, Toast.LENGTH_SHORT).show();

//            String workgroup_id_var = workgroup_id.getText().toString();
//            String update_time_var = update_time.getText().toString();
//
//            Intent i = new Intent(context, ChartManagerActivity.class);
//            i.putExtra("workgroup_name",workgroup_name_var);
//            i.putExtra("workgroup_access",workgroup_access_var);
//            i.putExtra("workgroup_id",workgroup_id_var);
//            i.putExtra("update_time",update_time_var);
//            context.startActivity(i);

//            Intent pindah = new Intent(RecyclerChartManagerAdapter.this, CreateActivity.class);
////                        i.putExtra("id_chart_manager",jsonObj.getString("id_chart_manager"));
//            pindah.putExtra("chartName",jsonObj.getString("id_chart_manager"));
//            pindah.putExtra("chartId",jsonObj.getString("id_chart_manager"));
////                        i.putExtra("chartTimeCreated",jsonObj.getString("id_chart_manager"));


            AlertDialog.Builder alertDialogBuilder;
            alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Warning");
            alertDialogBuilder.setMessage("Are you sure delete this data?");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

//                            String nim = editTextNIM.getText().toString();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    RegisterAPI api = retrofit.create(RegisterAPI.class);
                    Call<ValueAccountManager> call = api.deleteAccountManagerById(account_id_var);
                    call.enqueue(new Callback<ValueAccountManager>() {
                        @Override
                        public void onResponse(Call<ValueAccountManager> call, Response<ValueAccountManager> response) {
                            String value = response.body().getValue();
                            String message = response.body().getMessage();

                            Intent i = new Intent(context, ChartManagerActivity.class);
                            
                            context.startActivity(i);

//                            Intent pindah = new Intent(MainActivity.this, WorkgroupActivity.class);
//                            startActivityForResult(pindah,1);

                            //                            Toast.makeText(AccountManagerActivity.class, "Data Deleted", Toast.LENGTH_SHORT).show();
                            //                                    if (value.equals("1")) {
//                                        Toast.makeText(UpdateActivity.this, message, Toast.LENGTH_SHORT).show();
//                                        finish();
//                                    } else {
//                                        Toast.makeText(UpdateActivity.this, message, Toast.LENGTH_SHORT).show();
//                                    }
                        }

                        @Override
                        public void onFailure(Call<ValueAccountManager> call, Throwable t) {
                            t.printStackTrace();
//                                    Toast.makeText(UpdateActivity.this, "Jaringan Error!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();


        }
    }
}
