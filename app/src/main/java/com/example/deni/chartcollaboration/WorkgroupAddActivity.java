package com.example.deni.chartcollaboration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deni.chartcollaboration.api.RegisterAPI;
import com.example.deni.chartcollaboration.model.Value;
import com.example.deni.chartcollaboration.model.ValueWorkgroups;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import org.json.JSONException;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkgroupAddActivity extends AppCompatActivity {
    public static final String URL = "http://dhenis.com/charts/";

    private ProgressDialog progress;
    String workgroupName_var,accessCode_var;
    Random rand = new Random();

//    int myRandomInt = new Random().nextInt(80 - 65) + 65;

    int randomNum = Math.abs(rand.nextInt((999999 - 0) + 1) + 1);
//    int randomNum = Math.abs(randomNum);
    @BindView(R.id.workgroupName) EditText workgroupName;


    @BindView(R.id.accessCode) TextView accessCode1;

    @OnClick(R.id.buttonAddWorkgroup) void addWorkgroup(){
        Log.d("coba : ", String.valueOf(randomNum));
        //Untuk menampilkan progress dialog
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading...");
        progress.show();

        workgroupName_var = workgroupName.getText().toString();
        accessCode_var = String.valueOf(randomNum);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        //Untuk menampilkan progress dialog

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<ValueWorkgroups> call = api.addWorkgroup1(workgroupName_var,accessCode_var);

        call.enqueue(new Callback<ValueWorkgroups>(){
            @Override
            public void onResponse(Call<ValueWorkgroups> call, Response<ValueWorkgroups> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                progress.dismiss();

                if(value.equals("1")){

                    Toast.makeText(WorkgroupAddActivity.this, "Workgroup is created", Toast.LENGTH_SHORT).show();
                    back();


                    //
//  removeLastEntry();
//                            addEntry(Integer.parseInt(y_var));
                }else{
                    Toast.makeText(WorkgroupAddActivity.this, "Failed to save data, Try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ValueWorkgroups> call, Throwable t) {
                t.printStackTrace();
                progress.dismiss();
                Toast.makeText(WorkgroupAddActivity.this,"Error Connection",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void back() {

        Intent pindah = new Intent(WorkgroupAddActivity.this, WorkgroupActivity.class);
        startActivityForResult(pindah, 1);

    }

    @OnClick(R.id.backButtonWorkgroup)
    public void join(){
//        Log.v("ini jalan","jajajaj");
        Intent pindah = new Intent(WorkgroupAddActivity.this, WorkgroupActivity.class);
        startActivityForResult(pindah,1);

    }
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workgroup_add);
        ButterKnife.bind(this);
//        accessCode_var = String.valueOf(randomNum);
        TextView textRandom = (TextView)findViewById(R.id.accessCode);
        textRandom.setText("Access Code: "+String.valueOf(randomNum));

    }

}
