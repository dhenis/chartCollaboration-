package com.example.deni.chartcollaboration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deni.chartcollaboration.adapter.RecyclerChartManagerAdapter;
import com.example.deni.chartcollaboration.api.RegisterAPI;
import com.example.deni.chartcollaboration.model.ChartManager;
import com.example.deni.chartcollaboration.model.ValueChartManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

public class ChartManagerActivity extends AppCompatActivity {

    public static final String URL = "http://dhenis.com/charts/";

    private Context context;
    private List<ChartManager> chartManagers = new ArrayList<>();
    private RecyclerChartManagerAdapter chartManagerAdapter;

    @BindView(R.id.recycleChartManager)RecyclerView recyclerViewCrm;
    @BindView(R.id.progressChartManager)ProgressBar progressBarCrm;
    @BindView(R.id.addChartManagerButton)Button addChartManagerButton;
    @BindView(R.id.manageAccountButton)Button manageAccountButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_manager);
        ButterKnife.bind(this);

        //get intent
        Intent intent = getIntent();

        String workgroup_name = intent.getStringExtra("workgroup_name");
        String workgroup_id = intent.getStringExtra("workgroup_id");
        String workgroup_access = intent.getStringExtra("workgroup_access");


        setTitle("Name: "+workgroup_name+" | Code: "+workgroup_access);

        chartManagerAdapter  = new RecyclerChartManagerAdapter(this, chartManagers);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewCrm.setLayoutManager(mLayoutManager);
        recyclerViewCrm.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCrm.setAdapter(chartManagerAdapter);
        loadChartManager();

    }

    @OnClick(R.id.addChartManagerButton)
    public void createNewChart(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegisterAPI api  = retrofit.create(RegisterAPI.class); // panggil class di register adapter

        Intent intent = getIntent();

        String workgroup_name = intent.getStringExtra("workgroup_name");
        String workgroup_id = intent.getStringExtra("workgroup_id");
        String workgroup_access = intent.getStringExtra("workgroup_access");

//        @Field("name") String name,
//        @Field("status") String status,
//        @Field("id_workgroup") String id_workgroup

        Call<ValueChartManager> call =  api.createNewChart(workgroup_name,"1",workgroup_id);

        Log.d("@@workgroup ",workgroup_id);

        call.enqueue(new Callback<ValueChartManager>() {

            @Override
            public void onResponse(Call<ValueChartManager> call, Response<ValueChartManager> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                String data = new Gson().toJson(response.body().getChartManagerResult()).toString();
//                String lastElement = new Gson().toJson(response.body().getLastElement()).toString();

//                Toast.makeText(ChartManagerActivity.this, lastElement, Toast.LENGTH_SHORT).show();

//                Log.e("@@ Last element : ", lastElement);
                Log.e("@@ data: ", String.valueOf(data));

                if (value.equals("1")) {
                    progressBarCrm.setVisibility(View.GONE);
                    chartManagers = response.body().getChartManagerResult();

                    try {

                        JSONArray jsonArr = new JSONArray(data);

                        JSONObject jsonObj = jsonArr.getJSONObject(0);

                        Toast.makeText(ChartManagerActivity.this, String.valueOf(jsonObj.getString("id_chart_manager")), Toast.LENGTH_SHORT).show();

                        Log.d("dari array@@: ", String.valueOf(jsonObj.getString("id_chart_manager")));


//
//                        Intent i = new Intent(context, CreateActivity.class);
//                        context.startActivity(i);
                        // fungsi pindah
                        Intent pindah = new Intent(ChartManagerActivity.this, CreateActivity.class);
//                        i.putExtra("id_chart_manager",jsonObj.getString("id_chart_manager"));
                        pindah.putExtra("chartName",jsonObj.getString("id_chart_manager"));
                        pindah.putExtra("chartId",jsonObj.getString("id_chart_manager"));
//                        i.putExtra("chartTimeCreated",jsonObj.getString("id_chart_manager"));



                        startActivityForResult(pindah,1);
                            Log.d("@@pindah: ", String.valueOf(pindah));

                        //                        array = new JSONArray(new Gson().toJson(response.body().getResult()));



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


//                    chartManagerAdapter = new RecyclerChartManagerAdapter(ChartManagerActivity.this, chartManagers);
//
//                    Toast.makeText(ChartManagerActivity.this, message, Toast.LENGTH_SHORT).show();
//
//                    recyclerViewCrm.setAdapter(chartManagerAdapter);
                }
            }


            @Override
            public void onFailure(Call<ValueChartManager> call, Throwable t) {
                Toast.makeText(ChartManagerActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                Log.d( "@@trow:" , String.valueOf(t));
                Log.d( "@@call:" , String.valueOf(call));
                progressBarCrm.setVisibility(View.GONE);

            }
        });




//        String NameChartManager_var = NameChartManager.getText().toString();
//        String statusChatManager_var = statusChatManager.getText().toString();
//        String idChartManager_id_var = idChartManager.getText().toString();
//        String createdTimeChatManaager_var = createdTimeChatManaager.getText().toString();

//        Intent i = new Intent(context, CreateActivity.class);
//        i.putExtra("chartName",NameChartManager_var);
//        i.putExtra("chartStatus",statusChatManager_var);
//        i.putExtra("chartId",idChartManager_id_var);
//        i.putExtra("chartTimeCreated",createdTimeChatManaager_var);
//        context.startActivity(i);

//        Intent pindah = new Intent(ChartManagerActivity.this, ChartManagerAddActivity.class);
//        startActivityForResult(pindah,1);
    }


    @OnClick(R.id.backChartManagerButton)
    public void back(){
//        Intent pindah = new Intent(ChartManagerActivity.this, MainActivity.class);
//
//        startActivityForResult(pindah,1);

        onBackPressed();

//        Intent intent = getIntent();
//
//        String username = intent.getStringExtra("username");
//        String id_account = intent.getStringExtra("id_account");
//        String role = intent.getStringExtra("role");
//
//        Log.d("rople@@", role);
//
//        Intent pindah = new Intent(ChartManagerActivity.this, WorkgroupActivity.class);
//
//        pindah.putExtra("username",username);
//        pindah.putExtra("id_account",id_account);
//        pindah.putExtra("role",role);
//
//        startActivityForResult(pindah,1);
    }

    @OnClick(R.id.manageAccountButton)
    public void toAccount(){

        Intent intent = getIntent();

        String workgroup_name = intent.getStringExtra("workgroup_name");
        String workgroup_id = intent.getStringExtra("workgroup_id");
        String workgroup_access = intent.getStringExtra("workgroup_access");

//
//        Intent i = new Intent(context, AccountManagerActivity.class);
//        i.putExtra("workgroup_name",workgroup_name);
//        i.putExtra("workgroup_access",workgroup_access);
//        i.putExtra("workgroup_id",workgroup_id);
////        i.putExtra("update_time",update_time_var);
//        context.startActivity(i);

//
        Intent i = new Intent(ChartManagerActivity.this, AccountManagerActivity.class);
        i.putExtra("workgroup_name",workgroup_name);
        i.putExtra("workgroup_access",workgroup_access);
        i.putExtra("workgroup_id",workgroup_id);
        startActivityForResult(i,1);
    }


    private void loadChartManager(){

        //session setting
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SessionPref", 0); // 0 - for private mode
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("role_session", "subscriber");  // Saving string
//
//        editor.commit();

        String role_session = pref.getString("role_session",null);
        Log.d("@@ session rolenya :",pref.getString("role_session",null));

        if(role_session.equals("subscriber")){

            addChartManagerButton.setVisibility(View.INVISIBLE);
            manageAccountButton.setVisibility(View.INVISIBLE);

        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Intent intent = getIntent();

        String workgroup_name = intent.getStringExtra("workgroup_name");
        String workgroup_id = intent.getStringExtra("workgroup_id");
        String workgroup_access = intent.getStringExtra("workgroup_access");

        RegisterAPI api  = retrofit.create(RegisterAPI.class); // panggil class di register adapter
        Call<ValueChartManager> call =  api.viewChartManagerById(workgroup_id);

        call.enqueue(new Callback<ValueChartManager>() {

            @Override
            public void onResponse(Call<ValueChartManager> call, Response<ValueChartManager> response) {
                String value = response.body().getValue();
                Log.d( "@@onRespon" , String.valueOf(response.code()));
                String message = response.body().getMessage();
//                String data = new Gson().toJson(response.body().getChartManagerResult()).toString();
                Log.e("@@ value: ",value);
                Log.e("@@ message: ", String.valueOf(response.body().getMessage()));
                Log.e("@@ result: ", String.valueOf(response.body().getChartManagerResult()));


//                Log.e("@@ data: ", String.valueOf(data));

                if (value.equals("1")) {
                    String lastElement = new Gson().toJson(response.body().getLastElement()).toString();

                    Log.e("@@ Last element : ", lastElement);


                    progressBarCrm.setVisibility(View.GONE);
                    chartManagers = response.body().getChartManagerResult();

                    chartManagerAdapter = new RecyclerChartManagerAdapter(ChartManagerActivity.this, chartManagers);

//                    Toast.makeText(ChartManagerActivity.this, message, Toast.LENGTH_SHORT).show();

                    recyclerViewCrm.setAdapter(chartManagerAdapter);
                }
            }


            @Override
            public void onFailure(Call<ValueChartManager> call, Throwable t) {
                Toast.makeText(ChartManagerActivity.this, "Data empty", Toast.LENGTH_SHORT).show();
                Log.d( "@@trow:" , String.valueOf(t));
                Log.d( "@@call:" , String.valueOf(call));
                progressBarCrm.setVisibility(View.GONE);

            }
        });

    }





}
