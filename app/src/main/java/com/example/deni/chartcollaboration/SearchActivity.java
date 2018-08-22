package com.example.deni.chartcollaboration;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deni.chartcollaboration.adapter.RecyclerChartManagerAdapter;
import com.example.deni.chartcollaboration.api.RegisterAPI;
import com.example.deni.chartcollaboration.model.ValueChartManager;
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

public class SearchActivity extends AppCompatActivity {
    public static final String URL = "http://dhenis.com/charts/";

    @BindView(R.id.searchForm)  EditText searchForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setTitle("Search page");

        // acction when press enter
        searchForm.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == android.view.KeyEvent.ACTION_DOWN) && (keyCode == android.view.KeyEvent.KEYCODE_ENTER)) {
                    //Toast.makeText(CreateActivity.this, editY.getText(), Toast.LENGTH_SHORT).show();
                    // same with press button

                    loadChartManager();

                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(searchForm.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);


                    return true;
                }
                return false;
            }
        });

    }

    @OnClick(R.id.searchButton)
    public void search(){

        loadChartManager();

    }
    @OnClick(R.id.workgroupButton)
    public void join(){
//        Log.v("ini jalan","jajajaj");
        Intent pindah = new Intent(SearchActivity.this, WorkgroupActivity.class);
        pindah.putExtra("username",String.valueOf(" "));
        pindah.putExtra("id_account",String.valueOf(" "));
        pindah.putExtra("role","subscriber");


        startActivityForResult(pindah,1);

    }

    @OnClick(R.id.backButton)
    public void create(){
//        Log.v("ini jalan","jajajaj");
        Intent pindah = new Intent(SearchActivity.this, MainActivity.class);
        startActivityForResult(pindah,1);
    }


    private void loadChartManager(){

        String searchForm_var = searchForm.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegisterAPI api  = retrofit.create(RegisterAPI.class); // panggil class di register adapter
        Call<ValueChartManager> call =  api.searchChartById(searchForm_var);

        call.enqueue(new Callback<ValueChartManager>() {


            @Override
            public void onResponse(Call<ValueChartManager> call, Response<ValueChartManager> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                String data = new Gson().toJson(response.body().getChartManagerResult()).toString();
                String lastElement = new Gson().toJson(response.body().getLastElement()).toString();

//                Toast.makeText(ChartManagerActivity.this, lastElement, Toast.LENGTH_SHORT).show();

                Log.e("@@ Last element : ", lastElement);
//                Log.e("@@ data: ", String.valueOf(data));

                if (value.equals("1")) {

                    try {

                        JSONArray jsonArr = new JSONArray(data);

//
//                        for (int i = 0; i < jsonArr.length(); i++) {
//
//                            JSONObject jsonObj = jsonArr.getJSONObject(i);
//
//                            Toast.makeText(ChartManagerActivity.this, String.valueOf(jsonObj), Toast.LENGTH_SHORT).show();
//
//                        }

                        JSONObject jsonObj = jsonArr.getJSONObject(0);

//                        Toast.makeText(ChartManagerActivity.this, String.valueOf(jsonObj.getString("id")), Toast.LENGTH_SHORT).show();

                        Log.d("dari array: ", String.valueOf(jsonObj.getString("id_chart_manager")));

                        // fungsi pindah
                        Intent pindah = new Intent(SearchActivity.this, JoinActivity.class);

                        pindah.putExtra("username",String.valueOf(" "));
                        pindah.putExtra("id_account",String.valueOf(" "));
                        pindah.putExtra("role","subscriber");



                        pindah.putExtra("chartName",jsonObj.getString("id_chart_manager"));
                        pindah.putExtra("chartId",jsonObj.getString("id_chart_manager"));
                        pindah.putExtra("chartStatus",jsonObj.getString("status"));

                        pindah.putExtra("chartTimeCreated",jsonObj.getString("created_time"));



                        startActivityForResult(pindah,1);
                        Log.d("@@pindah: ", String.valueOf(pindah));

                        //                        array = new JSONArray(new Gson().toJson(response.body().getResult()));



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


//                    chartManagerAdapter = new RecyclerChartManagerAdapter(ChartManagerActivity.this, chartManagers);
//
//                    Toast.makeText(ChartManagerActivity.this, message, Toast.LENGTH_SHORT).show();
//
//                    recyclerViewCrm.setAdapter(chartManagerAdapter);
                }
            }


            @Override
            public void onFailure(Call<ValueChartManager> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Data Not found", Toast.LENGTH_SHORT).show();
                Log.d( "@@trow:" , String.valueOf(t));
                Log.d( "@@call:" , String.valueOf(call));
//                progressBarCrm.setVisibility(View.GONE);

            }

        });

    }


}
