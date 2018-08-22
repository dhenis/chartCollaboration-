package com.example.deni.chartcollaboration;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.deni.chartcollaboration.adapter.RecyclerAccountManagerAdapter;
import com.example.deni.chartcollaboration.adapter.RecyclerChartManagerAdapter;
import com.example.deni.chartcollaboration.api.RegisterAPI;
import com.example.deni.chartcollaboration.model.AccountManager;
import com.example.deni.chartcollaboration.model.ChartManager;
import com.example.deni.chartcollaboration.model.ValueAccountManager;
import com.example.deni.chartcollaboration.model.ValueChartManager;
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

public class AccountManagerActivity extends AppCompatActivity {

    public static final String URL = "http://dhenis.com/charts/";

    private Context context;
    private List<AccountManager> accountManagers = new ArrayList<>();
    private RecyclerAccountManagerAdapter accountManagerAdapter;

    @BindView(R.id.recycleAccountManager)RecyclerView recyclerViewAcm;
    @BindView(R.id.progressAccmManager)ProgressBar progressBarAcm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        String workgroup_name = intent.getStringExtra("workgroup_name");
        String workgroup_id = intent.getStringExtra("workgroup_id");
        String workgroup_access = intent.getStringExtra("workgroup_access");

        Log.e("@@workgroup name :",workgroup_name);
        Log.e("@@workgroup id :",workgroup_id);

        setTitle("Account Management");

        accountManagerAdapter  = new RecyclerAccountManagerAdapter(this, accountManagers);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewAcm.setLayoutManager(mLayoutManager);
        recyclerViewAcm.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAcm.setAdapter(accountManagerAdapter);
      loadAccountManager();

    }


    @OnClick(R.id.backButton)
    public void Back(){

        onBackPressed();

    }

    private void loadAccountManager(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Intent intent = getIntent();

        String workgroup_name = intent.getStringExtra("workgroup_name");
        String workgroup_id = intent.getStringExtra("workgroup_id");
        String workgroup_access = intent.getStringExtra("workgroup_access");

        RegisterAPI api  = retrofit.create(RegisterAPI.class); // panggil class di register adapter
        Call<ValueAccountManager> call =  api.viewAccountManagerById(workgroup_id);

        call.enqueue(new Callback<ValueAccountManager>() {
            @Override
            public void onResponse(Call<ValueAccountManager> call, Response<ValueAccountManager> response) {


                String value = response.body().getValue();
                Log.d( "@@onRespon" , String.valueOf(response.code()));
                String message = response.body().getMessage();
                Log.e("@@ value: ",value);
                Log.e("@@ message: ", String.valueOf(response.body().getMessage()));
                Log.e("@@ result: ", String.valueOf(response.body().getChartAccountResult()));
                String lastElement = new Gson().toJson(response.body().getLastElement()).toString();

                Log.e("@@ Last element : ", lastElement);
                Log.e("@@ Last element 22: ", response.body().getLastElement().toString());
                if (value.equals("1")) {
                    progressBarAcm.setVisibility(View.GONE);
                    accountManagers = response.body().getChartAccountResult();

                    accountManagerAdapter = new RecyclerAccountManagerAdapter(AccountManagerActivity.this, accountManagers);


                    recyclerViewAcm.setAdapter(accountManagerAdapter);
                }
            }

            @Override
            public void onFailure(Call<ValueAccountManager> call, Throwable t) {

            }
        });

    }


}
