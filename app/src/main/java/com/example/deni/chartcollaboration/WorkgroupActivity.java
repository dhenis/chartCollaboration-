package com.example.deni.chartcollaboration;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.deni.chartcollaboration.adapter.RecycleWorkgroupAdapter;
import com.example.deni.chartcollaboration.adapter.RecyclerViewAdapter;
import com.example.deni.chartcollaboration.api.RegisterAPI;
import com.example.deni.chartcollaboration.model.Value;
import com.example.deni.chartcollaboration.model.ValueWorkgroups;
import com.example.deni.chartcollaboration.model.Workgroups;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkgroupActivity extends AppCompatActivity {
    public static final String URL = "http://dhenis.com/charts/";
    private List<Workgroups> workgroups = new ArrayList<>();
    private RecycleWorkgroupAdapter workgroupAdapter;

    @BindView(R.id.recycleViewWorkgroup)RecyclerView recyclerViewWg;
    @BindView(R.id.progress_workgroup)ProgressBar progressBarWg;
    @BindView(R.id.addWorkgroupButton)Button addButton;
    @BindView(R.id.backButtonWorkgroup)Button backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workgroup);
        ButterKnife.bind(this);
        setTitle("Workgroup Lists");

        workgroupAdapter  = new  RecycleWorkgroupAdapter(this, workgroups);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewWg.setLayoutManager(mLayoutManager);
        recyclerViewWg.setItemAnimator(new DefaultItemAnimator());
        recyclerViewWg.setAdapter(workgroupAdapter);


        Intent intent = getIntent();


        String username = intent.getStringExtra("username");
        String id_account = intent.getStringExtra("id_account");
        String role = intent.getStringExtra("role");
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SessionPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        Log.d("@@ session rolenya :",pref.getString("role_session",null));

        loadWorkgroup();

    }

    @OnClick(R.id.addWorkgroupButton)
    public void create(){

        Intent intent = getIntent();



        String username = intent.getStringExtra("username");
        String id_account = intent.getStringExtra("id_account");
        String role = intent.getStringExtra("role");

        Intent pindah = new Intent(WorkgroupActivity.this, WorkgroupAddActivity.class);

        pindah.putExtra("username",username);
        pindah.putExtra("id_account",id_account);
        pindah.putExtra("role",role);

        startActivityForResult(pindah,1);
    }




    @OnClick(R.id.backButtonWorkgroup)
    public void back(){


        onBackPressed();

    }


    private void loadWorkgroup(){

        //session setting
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SessionPref", 0); // 0 - for private mode
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("role_session", "subscriber");  // Saving string
//
//        editor.commit();

        String role_session = pref.getString("role_session",null);
        Log.d("@@ session rolenya :",pref.getString("role_session",null));

        if(role_session.equals("subscriber")){

            addButton.setVisibility(View.INVISIBLE);

        }

        Intent intent = getIntent();

        String username = intent.getStringExtra("username");
        String id_account = intent.getStringExtra("id_account");
        String role = intent.getStringExtra("role");



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegisterAPI api  = retrofit.create(RegisterAPI.class); // panggil class di register adapter

        Call<ValueWorkgroups> call =  api.viewWorkgroup();

        Log.d( "@@role" , role);
        Log.d( "@@role" , id_account);

        if(role.equals("author")){
            Log.d( "@@role executed" , role);
            addButton.setVisibility(View.VISIBLE);
            call =  api.viewChartManagerByUsername(id_account);

        }



        call.enqueue(new Callback<ValueWorkgroups>() {

            @Override
            public void onResponse(Call<ValueWorkgroups> call, Response<ValueWorkgroups> response) {
                String value = response.body().getValue();
                Log.d( "@@onRespon" , String.valueOf(response.code()));
                String message = response.body().getMessage();
                String data = new Gson().toJson(response.body().getWorkgroupResult()).toString();
                Log.e("@@ value: ",value);
                Log.e("@@ message: ", String.valueOf(response.body().getMessage()));
                Log.e("@@ result: ", String.valueOf(response.body().getWorkgroupResult()));
                Log.e("@@ data: ", String.valueOf(data));

                if (value.equals("1")) {
                    progressBarWg.setVisibility(View.GONE);
                    workgroups = response.body().getWorkgroupResult();

                    workgroupAdapter = new RecycleWorkgroupAdapter(WorkgroupActivity.this, workgroups);

//                    Toast.makeText(WorkgroupActivity.this, message, Toast.LENGTH_SHORT).show();

                    recyclerViewWg.setAdapter(workgroupAdapter);
                }
            }

            @Override
            public void onFailure(Call<ValueWorkgroups> call, Throwable t) {
                Toast.makeText(WorkgroupActivity.this, "Workgroup is empty", Toast.LENGTH_SHORT).show();
                Log.d( "@@trow:" , String.valueOf(t));
                Log.d( "@@call:" , String.valueOf(call));
                progressBarWg.setVisibility(View.GONE);

            }
        });

    }


}

