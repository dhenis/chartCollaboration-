package com.example.deni.chartcollaboration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @OnClick(R.id.button_join)
    public void join(){
//        Log.v("ini jalan","jajajaj");
        Intent pindah = new Intent(MainActivity.this, SearchActivity.class);
        pindah.putExtra("username",String.valueOf(" "));
        pindah.putExtra("id_account",String.valueOf(" "));
        pindah.putExtra("role","subscriber");


        startActivityForResult(pindah,1);

    }


    @OnClick(R.id.button_create)
    public void create(){
//        Log.v("ini jalan","jajajaj");
        Intent pindah = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(pindah,1);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //session setting
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SessionPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit(); // commit changes

        editor.putString("role_session", "subscriber");  // Saving string

        editor.commit();

        String role_session = pref.getString("role_session",null);
        Log.d("@@ session rolenya :",pref.getString("role_session",null));

    }

    @Override
    protected void onResume() {
        super.onResume();
        ButterKnife.bind(this);
        //session setting
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SessionPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit(); // commit changes

        editor.putString("role_session", "subscriber");  // Saving string

        editor.commit();

        String role_session = pref.getString("role_session",null);


    }
}
