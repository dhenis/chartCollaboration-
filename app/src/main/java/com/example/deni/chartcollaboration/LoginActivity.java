package com.example.deni.chartcollaboration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deni.chartcollaboration.adapter.RecyclerViewAdapter;
import com.example.deni.chartcollaboration.api.RegisterAPI;
import com.example.deni.chartcollaboration.model.Value;
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
    @BindView(R.id.username) EditText username_var;
    @BindView(R.id.password) EditText password_var;
    private ProgressDialog progress;
    public static final String URL = "http://dhenis.com/charts/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

// retrofit onclick
    @OnClick(R.id.button_login)
    public void login(){
       Log.v("Button Login","button login works");
//        Intent pindah = new Intent(LoginActivity.this, CreateActivity.class);
//        startActivityForResult(pindah,1);



        //Untuk menampilkan progress dialog
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading...");
        progress.show();

        final String user_var, pass_var;


        user_var = username_var.getText().toString();
        pass_var = password_var.getText().toString();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);

        Call<Value> call = api.getLastValueFromChart(user_var);
        Log.d("errornya : @@: ", String.valueOf(call));
        Log.d("variable passing : @@: ", String.valueOf(user_var));
        call.enqueue(new Callback<Value>(){
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();

                progress.dismiss();

                if(value.equals("1")){
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
//                    removeLastEntry();
//                            addEntry(Integer.parseInt(y_var));
                }else{
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                t.printStackTrace();
                progress.dismiss();
                Toast.makeText(LoginActivity.this,"Error Connection",Toast.LENGTH_SHORT).show();
            }
        });




    }

}
