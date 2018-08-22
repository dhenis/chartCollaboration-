package com.example.deni.chartcollaboration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deni.chartcollaboration.api.RegisterAPI;
import com.example.deni.chartcollaboration.model.ValueChartManager;
import com.example.deni.chartcollaboration.model.ValueWorkgroups;
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

public class AccessCodeActivity extends AppCompatActivity {
    public static final String URL = "http://dhenis.com/charts/";

    @BindView(R.id.accessCodeForm)  EditText accessCodeForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_code);
        ButterKnife.bind(this);

        Toast.makeText(AccessCodeActivity.this, "Insert access code to edit the chart", Toast.LENGTH_SHORT).show();

    }

    @OnClick(R.id.accessButton)
    public void search(){

        loadChartManager();

    }

    @OnClick(R.id.backButton)
    public void create(){

        Intent intent = getIntent();

        String role = intent.getStringExtra("role");
        String chartIdBack = intent.getStringExtra("chartId");



        Intent pindah = new Intent(AccessCodeActivity.this, JoinActivity.class);


        pindah.putExtra("username",String.valueOf(" "));
        pindah.putExtra("id_account",String.valueOf(" "));
        pindah.putExtra("role","subscriber");


        pindah.putExtra("chartId",chartIdBack);

        startActivityForResult(pindah,1);
    }


    private void loadChartManager(){

        Intent intent = getIntent();

        String role = intent.getStringExtra("role");
        final String chartId = intent.getStringExtra("chartId");

        String accessCodeForm_var = accessCodeForm.getText().toString();
        Log.d("@@chartID : ", chartId);
        Log.d("@@accessCodeForm_var : ", accessCodeForm_var);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegisterAPI api  = retrofit.create(RegisterAPI.class); // panggil class di register adapter
        Call<ValueWorkgroups> call =  api.checkAccessCode(chartId,accessCodeForm_var);

        call.enqueue(new Callback<ValueWorkgroups>() {


            @Override
            public void onResponse(Call<ValueWorkgroups> call, Response<ValueWorkgroups> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                String data = new Gson().toJson(response.body().getWorkgroupResult()).toString();
                Log.e("@@ Last element : ", data);

                if (value.equals("1")) {

                    try {

                        JSONArray jsonArr = new JSONArray(data);

                        JSONObject jsonObj = jsonArr.getJSONObject(0);

                        // fungsi pindah
                        Intent pindah = new Intent(AccessCodeActivity.this, RegisterActivity.class);

                        pindah.putExtra("username",String.valueOf(" "));
                        pindah.putExtra("id_account",String.valueOf(" "));
                        pindah.putExtra("role","change");


                        pindah.putExtra("chartId",chartId);
                        pindah.putExtra("id_workgroup",jsonObj.getString("id_workgroup"));
                        pindah.putExtra("name",jsonObj.getString("name"));
                        pindah.putExtra("access",jsonObj.getString("access"));
                        pindah.putExtra("update_time",jsonObj.getString("update_time"));
                        Log.d("@@pindah: ", String.valueOf(pindah));

                        startActivityForResult(pindah,1);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }


            @Override
            public void onFailure(Call<ValueWorkgroups> call, Throwable t) {
                Toast.makeText(AccessCodeActivity.this, "Data Not found", Toast.LENGTH_SHORT).show();
                Log.d( "@@trow:" , String.valueOf(t));
                Log.d( "@@call:" , String.valueOf(call));
//                progressBarCrm.setVisibility(View.GONE);

            }

        });

    }
}
