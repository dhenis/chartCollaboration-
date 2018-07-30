package com.example.deni.chartcollaboration;

import android.content.Intent;
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
    }



}
