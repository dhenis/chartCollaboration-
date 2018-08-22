package com.example.deni.chartcollaboration;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

    SharedPreferences sharedpreferences;

    public static final String mypreference = "mypref";

    @BindView(R.id.username) EditText username_var;
    @BindView(R.id.password) EditText password_var;
    private ProgressDialog progress;
//    final String user_var = username_var.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("SessionPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("role_session", "subscriber");  // Saving string

        editor.commit();

        String role_session = pref.getString("role_session",null);
        Log.d("@@ session rolenya :",pref.getString("role_session",null));




        // acction when press enter
        password_var.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == android.view.KeyEvent.ACTION_DOWN) && (keyCode == android.view.KeyEvent.KEYCODE_ENTER)) {
                    //Toast.makeText(CreateActivity.this, editY.getText(), Toast.LENGTH_SHORT).show();
                    // same with press button


                    loadAccountManager();

                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(password_var.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);


                    return true;
                }
                return false;
            }
        });

    }

// retrofit onclick
    @OnClick(R.id.button_login)
    public void login(){
       Log.v("@@Button Login","button login works");

        loadAccountManager();

    }



    // retrofit onclick
    @OnClick(R.id.button_back)
    public void back(){


        onBackPressed();

    }
    // retrofit onclick
    @OnClick(R.id.button_register)
    public void register(){
        Log.v("@@Button Login","button login works");

        Intent pindah = new Intent(LoginActivity.this, RegisterActivity.class);

        pindah.putExtra("username",String.valueOf(" "));
        pindah.putExtra("id_account",String.valueOf(" "));
        pindah.putExtra("role","author");


        pindah.putExtra("chartId","0");
        pindah.putExtra("id_workgroup","0");
        pindah.putExtra("name","0");
        pindah.putExtra("access","0");
        pindah.putExtra("update_time","0");

        startActivityForResult(pindah,1);

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

                        //success
                        Toast.makeText(LoginActivity.this, "Welcome, access granted ", Toast.LENGTH_SHORT).show();



//                    move to another page
                        Intent pindah = new Intent(LoginActivity.this, WorkgroupActivity.class);
                        pindah.putExtra("username",jsonObj.getString("username"));
                        pindah.putExtra("id_account",jsonObj.getString("id_account"));
                        pindah.putExtra("role","author");

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("SessionPref", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("role_session", "author");  // Saving string

                        editor.commit();



                        startActivityForResult(pindah,1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (value.equals("0")){ // not working

//                    Toast.makeText(LoginActivity.this, "0", Toast.LENGTH_SHORT).show();
                        Log.d("gagal@@", "Login");
                }
            }

            @Override
            public void onFailure(Call<ValueAccountManager> call, Throwable t) {

                Toast.makeText(LoginActivity.this, "Username Invalid", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
