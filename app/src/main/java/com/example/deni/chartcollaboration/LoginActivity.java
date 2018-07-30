package com.example.deni.chartcollaboration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deni.chartcollaboration.adapter.RecyclerAccountManagerAdapter;
import com.example.deni.chartcollaboration.adapter.RecyclerViewAdapter;
import com.example.deni.chartcollaboration.api.RegisterAPI;
import com.example.deni.chartcollaboration.model.Value;
import com.example.deni.chartcollaboration.model.ValueAccountManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    public static final String URL = "http://dhenis.com/charts/";


    @BindView(R.id.username) EditText username_var;
    @BindView(R.id.password) EditText password_var;
    private ProgressDialog progress;
//    final String user_var = username_var.getText().toString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

// retrofit onclick
    @OnClick(R.id.button_login)
    public void login(){
       Log.v("@@Button Login","button login works");
//        Intent pindah = new Intent(LoginActivity.this, CreateActivity.class);
//        startActivityForResult(pindah,1);


        loadAccountManager();





    }


    private void loadAccountManager(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegisterAPI api  = retrofit.create(RegisterAPI.class); // panggil class di register adapter
        Call<ValueAccountManager> call =  api.getUsername(username_var.getText().toString(),password_var.getText().toString());

        call.enqueue(new Callback<ValueAccountManager>() {
            @Override
            public void onResponse(Call<ValueAccountManager> call, Response<ValueAccountManager> response) {


                String value = response.body().getValue();
                String message = response.body().getMessage();
                String lastElement = new Gson().toJson(response.body().getLastElement()).toString();
                String data = new Gson().toJson(response.body().getChartAccountResult()).toString();


                if (value.equals("1")) {
                    try {

                        JSONArray jsonArr = new JSONArray(data);


                        JSONObject jsonObj = jsonArr.getJSONObject(0);



//                    progressBarAcm.setVisibility(View.GONE);
//                    accountManagers = response.body().getChartAccountResult();
//
//                    accountManagerAdapter = new RecyclerAccountManagerAdapter(AccountManagerActivity.this, accountManagers);

                        Toast.makeText(LoginActivity.this, "1", Toast.LENGTH_SHORT).show();

//                    move to another page
                        Intent pindah = new Intent(LoginActivity.this, WorkgroupActivity.class);
                        pindah.putExtra("username",jsonObj.getString("username"));
                        pindah.putExtra("id_account",jsonObj.getString("id_account"));
                        pindah.putExtra("role","author");

                        startActivityForResult(pindah,1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (value.equals("0")){ // not working

                    Toast.makeText(LoginActivity.this, "0", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ValueAccountManager> call, Throwable t) {

                Toast.makeText(LoginActivity.this, "Username Invalid", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
