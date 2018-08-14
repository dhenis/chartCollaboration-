package com.example.deni.chartcollaboration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deni.chartcollaboration.api.RegisterAPI;
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

public class RegisterActivity extends AppCompatActivity {
    public static final String URL = "http://dhenis.com/charts/";


    @BindView(R.id.username) EditText username_var;
    @BindView(R.id.password) EditText password_var;
    @BindView(R.id.email) EditText email;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }
    // retrofit onclick
    @OnClick(R.id.button_back)
    public void back(){

//        Intent pindah = new Intent(RegisterActivity.this, MainActivity.class);
//
//        startActivityForResult(pindah,1);
        onBackPressed();
//        loadAccountManager();

    }
    // retrofit onclick
    @OnClick(R.id.button_register)
    public void register(){

//
        loadAccountManager();





    }


    private void loadAccountManager(){

        // paket session
        Intent intent = getIntent();
        // from access code activity
        final String role = intent.getStringExtra("role");
        final String chartId = intent.getStringExtra("chartId");
        final String id_workgroup = intent.getStringExtra("id_workgroup");
        final String name = intent.getStringExtra("name");
        final String access = intent.getStringExtra("access");
        final String update_time = intent.getStringExtra("update_time");


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegisterAPI api  = retrofit.create(RegisterAPI.class); // panggil class di register adapter

        if(role.equals("change")){

        Call<ValueAccountManager> call =  api.RegisterChangningNewUser(username_var.getText().toString(), password_var.getText().toString(), email.getText().toString(),chartId);
            call.enqueue(new Callback<ValueAccountManager>() {
                @Override
                public void onResponse(Call<ValueAccountManager> call, Response<ValueAccountManager> response) {


                    String value = response.body().getValue();

                    String data = new Gson().toJson(response.body().getChartAccountResult()).toString();


                    if (value.equals("1")) {
                        try {

                            JSONArray jsonArr = new JSONArray(data);

                            JSONObject jsonObj = jsonArr.getJSONObject(0);

//                    move to another page
                            Intent pindah = new Intent(RegisterActivity.this, CreateActivity.class);

                            SharedPreferences pref = getApplicationContext().getSharedPreferences("SessionPref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("role_session", "author");  // Saving string

                            editor.commit();

                            // from login
                            pindah.putExtra("username",jsonObj.getString("username"));
                            pindah.putExtra("id_account",jsonObj.getString("id_account"));
                            pindah.putExtra("role","author");

                            //from chart manager
                            pindah.putExtra("chartName",chartId);
                            pindah.putExtra("chartStatus",chartId);
                            pindah.putExtra("chartId",chartId);
                            pindah.putExtra("chartTimeCreated",update_time);
//                            pindah.putExtra("workgroup_access",update_time);

                            startActivityForResult(pindah,1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    if (value.equals("0")){ // not working

                        Toast.makeText(RegisterActivity.this, "01", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ValueAccountManager> call, Throwable t) {

                    Toast.makeText(RegisterActivity.this, "Username Invalid1", Toast.LENGTH_SHORT).show();

                }
            });
        }else{

            Call<ValueAccountManager> call =  api.RegisterNewUser(username_var.getText().toString(), password_var.getText().toString(), email.getText().toString());
            call.enqueue(new Callback<ValueAccountManager>() {
                @Override
                public void onResponse(Call<ValueAccountManager> call, Response<ValueAccountManager> response) {


                    String value = response.body().getValue();
                    String message = response.body().getMessage();

                    String data = new Gson().toJson(response.body().getChartAccountResult()).toString();


                    if (value.equals("1")) {
                        try {

                            JSONArray jsonArr = new JSONArray(data);

                            JSONObject jsonObj = jsonArr.getJSONObject(0);

//                    move to another page
                            Log.d("@@ROLE : ",role);
                            Intent pindah = new Intent(RegisterActivity.this, WorkgroupActivity.class);


                            SharedPreferences pref = getApplicationContext().getSharedPreferences("SessionPref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("role_session", "author");  // Saving string

                            editor.commit();

                            // from login
                            pindah.putExtra("username",jsonObj.getString("username"));
                            pindah.putExtra("id_account",jsonObj.getString("id_account"));
                            pindah.putExtra("role","author");

                            //from chart manager
                            pindah.putExtra("chartName",chartId);
                            pindah.putExtra("chartStatus",chartId);
                            pindah.putExtra("chartId",chartId);
                            pindah.putExtra("chartTimeCreated",update_time);

                            startActivityForResult(pindah,1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    if (value.equals("0")){ // not working

                        Toast.makeText(RegisterActivity.this, "02", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ValueAccountManager> call, Throwable t) {

                    Toast.makeText(RegisterActivity.this, "Username Invalid2", Toast.LENGTH_SHORT).show();
                    Log.d("error on register2 @@:",String.valueOf(t));
                }
            });
        }

    }
}
