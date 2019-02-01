package com.example.deni.chartcollaboration;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deni.chartcollaboration.adapter.RecyclerViewAdapter;
import com.example.deni.chartcollaboration.api.RegisterAPI;
import com.example.deni.chartcollaboration.model.Charts;
import com.example.deni.chartcollaboration.model.Value;
import com.example.deni.chartcollaboration.model.ValueWorkgroups;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    public static final String URL = "http://dhenis.com/charts/";

    public static int iteration = 1; // make sure hanya 1 iteration

    String x_var, y_var, chart_id_var, category_var;

    private ProgressDialog progress;
    private List<Charts> chart_list  = new ArrayList<>(); // masukkan ke kelas chart dari API
    private RecyclerViewAdapter viewAdapter;


    @BindView(R.id.recycleView)RecyclerView recyclerView;
    @BindView(R.id.progress_bar)ProgressBar progressBar;

    Button btnAddData;
    Button btnviewAll;
    Button btnDelete;
    SoundPool mySound;

    int raygunID;

    MediaPlayer mp;

    // karena udah pake butter knife --> onclick mendjadi lebih simple
    @BindView(R.id.AccessCode) TextView AccessCode;
    @BindView(R.id.editX) EditText editX;
    @BindView(R.id.editY) EditText editY;
    @BindView(R.id.chart_id) EditText chart_id;
    @BindView(R.id.category) EditText category;



//    @BindView(R.id.button_add)Button btnviewAdd;


//    btnviewAll = (Button)findViewById(R.id.button);
//    public LineData data = mChart.getData();

    private LineChart mChart;

    private LineData data;

    private ArrayList<Entry> entries = new ArrayList<Entry>();

    // method for mpa -------------------


    public LineData getData(){
        return this.data;
    }



    private void playmp(float a) {
        float volume = ((a / (mChart.getYChartMax() - mChart.getYChartMin()))*5);
        mySound.play(raygunID, 1, 1, 1, 0, volume);

    }

    @OnClick(R.id.button_back)
    public void create(){
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        ButterKnife.bind(this);

        writeTable();
        //get intent
        Intent intent = getIntent();
        String chartName = intent.getStringExtra("chartName");
        String chartId = intent.getStringExtra("chartId");

        chart_id.setText(chartId); // set chart id

        setAccessCode(chartId); // show access code


        Log.d("##chart id##", chartId);
        setTitle("Author Page : chart"+chartId);
        loadData(); // panggil fungsi yang dibawah

        btnAddData = (Button)findViewById(R.id.button_add);
        btnviewAll = (Button)findViewById(R.id.button_Author);
        btnDelete= (Button)findViewById(R.id.button_delete);
        mySound = new SoundPool(6, AudioManager.STREAM_NOTIFICATION, 0);
        raygunID = mySound.load(this, R.raw.p1, 1);

        //mpa method

        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);
        mChart.setDescription("");

        // add an empty data object
        mChart.setData(new LineData());
        mChart.setScaleEnabled(false);

        mChart.invalidate();

        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Untuk menampilkan progress dialog
                progress = new ProgressDialog(v.getContext());
                progress.setCancelable(false);
                progress.setMessage("Loading...");
                progress.show();

                final String x_var, y_var, chart_id_var, category_var;


                x_var = editX.getText().toString();
                y_var = editY.getText().toString();
                chart_id_var = chart_id.getText().toString();
                category_var = category.getText().toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RegisterAPI api = retrofit.create(RegisterAPI.class);
                Call<Value> call = api.daftar(x_var,y_var,chart_id_var,chart_id_var,category_var);

                call.enqueue(new Callback<Value>(){
                    @Override
                    public void onResponse(Call<Value> call, Response<Value> response) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();
                        progress.dismiss();

                        if(value.equals("1")){
                            Toast.makeText(CreateActivity.this, "Data Added", Toast.LENGTH_SHORT).show();

                            addEntry(Integer.parseInt(y_var));

                            editY.setText(""); // set after submit the internet


                        }else{

                            Log.i("errornya: ", value);
                            Toast.makeText(CreateActivity.this, "failed to add data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Value> call, Throwable t) {
                        t.printStackTrace();
                        progress.dismiss();
                        Toast.makeText(CreateActivity.this,"Error Connection",Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() { // untuk delete nanti
            @Override
            public void onClick(View v) {

                //Untuk menampilkan progress dialog
                progress = new ProgressDialog(v.getContext());
                progress.setCancelable(false);
                progress.setMessage("Loading...");
                progress.show();

                final String x_var, y_var, chart_id_var, category_var;


                x_var = editX.getText().toString();
                y_var = editY.getText().toString();
                chart_id_var = chart_id.getText().toString();
                category_var = category.getText().toString();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RegisterAPI api = retrofit.create(RegisterAPI.class);

                Call<Value> call = api.getLastValueFromChart(chart_id_var);

                call.enqueue(new Callback<Value>(){
                    @Override
                    public void onResponse(Call<Value> call, Response<Value> response) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();
                        progress.dismiss();

                        if(value.equals("1")){
                            Toast.makeText(CreateActivity.this, "Data removed", Toast.LENGTH_SHORT).show();
                            removeLastEntry();
//                            addEntry(Integer.parseInt(y_var));
                        }else{
                            Toast.makeText(CreateActivity.this, "Failed to remove add", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Value> call, Throwable t) {
                        t.printStackTrace();
                        progress.dismiss();
                        Toast.makeText(CreateActivity.this,"Error Connection",Toast.LENGTH_SHORT).show();
                    }
                });



            }

//            @Override
//            public void onClick(View v) {
//                removeLastEntry();
//            }
        });


        // acction when press enter
        editY.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == android.view.KeyEvent.ACTION_DOWN) && (keyCode == android.view.KeyEvent.KEYCODE_ENTER)) {
                    //Toast.makeText(CreateActivity.this, editY.getText(), Toast.LENGTH_SHORT).show();
                    // same with press button

                    //Untuk menampilkan progress dialog
                    progress = new ProgressDialog(v.getContext());
                    progress.setCancelable(false);
                    progress.setMessage("Loading...");
                    progress.show();

                    final String x_var, y_var, chart_id_var, category_var;


                    x_var = editX.getText().toString();
                    y_var = editY.getText().toString();
                    chart_id_var = chart_id.getText().toString();
                    category_var = category.getText().toString();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    RegisterAPI api = retrofit.create(RegisterAPI.class);
                    Call<Value> call = api.daftar(x_var,y_var,chart_id_var,chart_id_var,category_var);

                    call.enqueue(new Callback<Value>(){
                        @Override
                        public void onResponse(Call<Value> call, Response<Value> response) {
                            String value = response.body().getValue();
                            String message = response.body().getMessage();
                            progress.dismiss();

                            if(value.equals("1")){
                                Toast.makeText(CreateActivity.this, "Data Added", Toast.LENGTH_SHORT).show();

                                addEntry(Integer.parseInt(y_var));

                                editY.setText(""); // set after submit the internet


                            }else{
                                Toast.makeText(CreateActivity.this, "failed to add data", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Value> call, Throwable t) {
                            t.printStackTrace();
                            progress.dismiss();
                            Toast.makeText(CreateActivity.this,"Error Connection",Toast.LENGTH_SHORT).show();
                        }
                    });



                    //------

                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(editY.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);


                    return true;
                }
                return false;
            }
        });


        mp = MediaPlayer.create(this, R.raw.p1);
        btnviewAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if(data != null){

                    entries.clear();
                    for(int i=0;i<data.getDataSetByIndex(0).getEntryCount();i++){
                        entries.add(data.getDataSetByIndex(0).getEntryForIndex(i));
                    }

                }
                // TODO Auto-generated method stub
                final Timer timer = new Timer();

                // Body Of Timer
                TimerTask time = new TimerTask() {

                    private int v = 0;

                    @Override
                    public void run() {

                        //Perform background work here
                        if (!mp.isPlaying()) {
//Log.i("entires", String.valueOf(entries.get(0)));
                            playmp(entries.get(v++).getVal());
//                              playmp(data.getDataSetByIndex(0).getEntryForIndex(v++).getVal());


                            if (v >= entries.size())
                                timer.cancel();
                        }


                    }
                };
                //Starting Timer
                timer.scheduleAtFixedRate(time, 0, 500);



            }
        });
        // end of mpa

        Log.d("start : ","OnCreate");

    }


    // untuk meload data mahasiswa
    private void loadData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        chart_id_var = chart_id.getText().toString();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<Value> call = api.view(chart_id_var);

        call.enqueue(new Callback<Value>(){
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {

                if (iteration == 1) { // make sure only once response

                    String value = response.body().getValue();


                    progressBar.setVisibility(View.GONE);

                    if (value.equals("1")) { // nilai satu means bisa menghubungi server

                        String data = new Gson().toJson(response.body().getResult()).toString();
                        Log.v("data nya @@ : ", data);

                        chart_list = response.body().getResult();
                        //                    Log.d("dari value: ",response.body().toString());

                        viewAdapter = new RecyclerViewAdapter(CreateActivity.this, chart_list);
                        recyclerView.setAdapter(viewAdapter);

                        try {

                            JSONArray jsonArr = new JSONArray(data);


                            for (int i = 0; i < jsonArr.length(); i++) {

                                JSONObject jsonObj = jsonArr.getJSONObject(i);

                                addEntry(Integer.parseInt(jsonObj.getString("y")));

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        iteration++;
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {

                Toast.makeText(CreateActivity.this,"Error Network", Toast.LENGTH_SHORT).show();
            }


        });

        Log.d("mahasiswa", "summon hahahah");

        iteration = 1;
    }


    private void setAccessCode(String CHART_ID){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegisterAPI api  = retrofit.create(RegisterAPI.class); // panggil class di register adapter
        Call<ValueWorkgroups> call =  api.getAccessCodeByIdChart(CHART_ID);

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

                        AccessCode.setText("Code:"+ jsonObj.getString("access")); // set chart id


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }


            @Override
            public void onFailure(Call<ValueWorkgroups> call, Throwable t) {
                Log.d( "@@access code fail:" , String.valueOf(call));
            }

        });




    }


    // mpa lagi

    int[] mColors = ColorTemplate.VORDIPLOM_COLORS;

    private void addEntry(int masukan) {


        data = mChart.getData();
        if(data != null) {
//            int test = Integer.parseInt(editY.getText().toString()); masih off
            ILineDataSet set = data.getDataSetByIndex(0);

            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet(); //masih off
                data.addDataSet(set);
            }

            // add a new x-value first
            data.addXValue(set.getEntryCount() + "");
            // choose a random dataSet

            int randomDataSetIndex = (int) (Math.random() * data.getDataSetCount());

            System.out.println("set.getEntryCount(): "+set.getEntryCount());

            // tambah dari disini dari db
            data.addEntry(new Entry((float) masukan, set.getEntryCount()) , 0); //masih off

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            mChart.setVisibleXRangeMaximum(220);
            mChart.setVisibleYRangeMaximum(220, YAxis.AxisDependency.LEFT);
//
//            // this automatically refreshes the chart (calls invalidate())
            mChart.moveViewTo(data.getXValCount()-7, 50f, YAxis.AxisDependency.LEFT);

            mChart.notifyDataSetChanged();
            mChart.invalidate();

        }
    }

    private void removeLastEntry() {

        LineData data = mChart.getData();

        if(data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);

            if (set != null) {

                Entry e = set.getEntryForXIndex(set.getEntryCount() - 1);

                data.removeEntry(e, 0);

                mChart.notifyDataSetChanged();
                mChart.invalidate();
            }
        }
    }

    private void addDataSet() {

        data = mChart.getData();

        if(data != null) {

            int count = (data.getDataSetCount() + 1);

            // create 10 y-vals
            ArrayList<Entry> yVals = new ArrayList<Entry>();

            if(data.getXValCount() == 0) {
                // add 10 x-entries
                for (int i = 0; i < 10; i++) {
                    data.addXValue("" + (i+1));
                }
            }

            for (int i = 0; i < data.getXValCount(); i++) {
                yVals.add(new Entry((float) (Math.random() * 50f) + 50f * count, i));
            }

            LineDataSet set = new LineDataSet(yVals, "DataSet " + count);
            set.setLineWidth(2.5f);
            set.setCircleRadius(4.5f);

            int color = mColors[count % mColors.length];

            set.setColor(color);
            set.setCircleColor(color);
            set.setHighLightColor(color);
            set.setValueTextSize(10f);
            set.setValueTextColor(color);

            data.addDataSet(set);
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
    }

    private void removeDataSet() {

        LineData data = mChart.getData();

        if(data != null) {

            data.removeDataSet(data.getDataSetByIndex(data.getDataSetCount() - 1));

            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
    }

    private void writeTable(){

        viewAdapter = new RecyclerViewAdapter(this, chart_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(viewAdapter);


    }


    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "DataSet 1");
        set.setLineWidth(2.5f);
        set.setCircleRadius(4.5f);
        set.setColor(Color.rgb(240, 99, 99));
        set.setCircleColor(Color.rgb(240, 99, 99));
        set.setHighLightColor(Color.rgb(190, 190, 190));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setValueTextSize(10f);

        return set;
    }


    // end mpa


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search, menu);
//        final MenuItem item = menu.findItem(R.id.action_search);
//
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
//        searchView.setQueryHint("Cari Nama Mahasiswa");
//        searchView.setIconified(false);
//        searchView.setOnQueryTextListener(this);
        return true;
    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        float volume= ((e.getVal()/(mChart.getYChartMax()-mChart.getYChartMin()))*5);


        mySound.play(raygunID, 1, 1, 1, 0, volume);


    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("action", "OnResume");

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d("action", "OnRestart");
//        writeTable();
//        loadData();
//
//        /// code to update data then notify Adapter
//        viewAdapter.notifyDataSetChanged();
    }

}
