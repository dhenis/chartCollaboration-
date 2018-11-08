package com.example.deni.chartcollaboration;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.deni.chartcollaboration.adapter.RecyclerViewAdapter;
import com.example.deni.chartcollaboration.api.RegisterAPI;
import com.example.deni.chartcollaboration.model.Charts;
import com.example.deni.chartcollaboration.model.Value;
import com.example.deni.chartcollaboration.model.ValueAccountManager;
import com.example.deni.chartcollaboration.model.ValueBookmarks;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;


import com.github.mikephil.charting.utils.PointD;
import com.github.mikephil.charting.utils.ViewPortHandler;
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

public class JoinActivity extends AppCompatActivity implements OnChartValueSelectedListener, OnChartGestureListener {

    public static final String URL = "http://dhenis.com/charts/";

    public static int iteration = 1; // make sure hanya 1 iteration

    Thread t1 = new Thread(new Task1());

    public static String realtimeCount = "0"; // make sure hanya 1 iteration

    public static ArrayList<Entry> yVals = new ArrayList<Entry>();

    public static ArrayList<Integer> tempContainers = new ArrayList<Integer>();
    // vibration

    Vibrator vibrator;


    // end of vibration


    volatile boolean stop = false;

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


    // simple karna pake butter knife
//    @OnClick(R.id.button_add) void daftar(){
//        Log.d("klik","saya coba");
//
//    }

    @OnClick(R.id.button_refresh)
    public void refresh(){

        Intent intent = getIntent();
        String chartName = intent.getStringExtra("chartName");
        String chartId = intent.getStringExtra("chartId");

        String chart_id_var = chart_id.getText().toString();

        Intent pindah = new Intent(JoinActivity.this, JoinActivity.class);

        pindah.putExtra("chartName",chart_id_var);
        pindah.putExtra("chartId",chart_id_var);

        startActivityForResult(pindah,1);


    }

    @OnClick(R.id.button_Author)
    public void author(){

//
//         fungsi pindah
        Intent pindah = new Intent(JoinActivity.this, AccessCodeActivity.class);

        pindah.putExtra("username",String.valueOf(" "));
        pindah.putExtra("id_account",String.valueOf(" "));
        pindah.putExtra("role","subscriber");

        String chart_id_var = chart_id.getText().toString();

        pindah.putExtra("chartName", chart_id_var);

        pindah.putExtra("chartId", chart_id_var);

        startActivityForResult(pindah,1);

        Log.e("@aaa",chart_id_var);

    }

    @OnClick(R.id.button_back)
    public void create(){

        Intent pindah2 = new Intent(JoinActivity.this, SearchActivity.class);

        startActivityForResult(pindah2, 1 );

        //        onBackPressed();
//
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        realtimeCount = "0";
        ButterKnife.bind(this);

        t1.start();


        // -- vibrate

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        if (vibrator != null && vibrator.hasVibrator()) {

            // vibrateFor500ms();

//            customVibratePatternNoRepeat();

//            customVibratePatternRepeatFromSpecificIndex();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createOneShotVibrationUsingVibrationEffect();
                createWaveFormVibrationUsingVibrationEffect();
                createWaveFormVibrationUsingVibrationEffectAndAmplitude();
            }

        } else {
            Toast.makeText(this, "Device does not support vibration", Toast.LENGTH_SHORT).show();
        }


        //


        writeTable();
        Intent intent = getIntent();
        String chartName = intent.getStringExtra("chartName");
        String chartId = intent.getStringExtra("chartId");

        chart_id.setText(chartId); // set chart id
        setTitle("Subscriber Page : chart"+chartId);

        btnAddData = (Button)findViewById(R.id.button_add);
        btnviewAll = (Button)findViewById(R.id.button_play);
        btnDelete= (Button)findViewById(R.id.button_delete);
        mySound = new SoundPool(6, AudioManager.STREAM_NOTIFICATION, 0);
        raygunID = mySound.load(this, R.raw.p1, 1);

        //mpa method

        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);
        mChart.setDescription("");
        mChart.setData(new LineData());
        mChart.setScaleEnabled(false);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("Chart is Empty");

        mChart.invalidate();

        loadData(); // panggil fungsi yang dibawah

        setBookmark();


        Log.d("bookmarknya@@$:", String.valueOf(tempContainers));
// ---- new

        btnAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // masukkan

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
                            Toast.makeText(JoinActivity.this, "Data added", Toast.LENGTH_SHORT).show();

                            addEntry(Integer.parseInt(y_var));
                        }else{
                            Toast.makeText(JoinActivity.this, "Failed to add data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Value> call, Throwable t) {
                        t.printStackTrace();
                        progress.dismiss();
                        Toast.makeText(JoinActivity.this,"Error Connection",Toast.LENGTH_SHORT).show();
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
                            removeLastEntry();
                        }else{
                            Toast.makeText(JoinActivity.this, "fail to remove data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Value> call, Throwable t) {
                        t.printStackTrace();
                        progress.dismiss();
                        Toast.makeText(JoinActivity.this,"Error Connection",Toast.LENGTH_SHORT).show();
                    }
                });


            }

//            @Override
//            public void onClick(View v) {
//                removeLastEntry();
//            }
        });
//
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

    private void setBookmark(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        chart_id_var = chart_id.getText().toString();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<ValueBookmarks> call = api.setBookmark(chart_id_var);

        Log.w("char_ id value @@$ :", String.valueOf(call));

        call.enqueue(new Callback<ValueBookmarks>(){
            @Override
            public void onResponse(Call<ValueBookmarks> call, Response<ValueBookmarks> response) {

                if (iteration == 1) { // make sure only once response

                    String value = response.body().getValue();


                    if (value.equals("1")) { // nilai satu means bisa menghubungi server

                        String data = new Gson().toJson(response.body().getResult()).toString();
                        Log.w("Data Bookmark @@$ :",data);
                        try {

                            JSONArray jsonArr = new JSONArray(data);

                            String berapa = "0";
                            for (int i = 0; i < jsonArr.length(); i++) {

                                JSONObject jsonObj = jsonArr.getJSONObject(i);

//                                addEntry(Integer.parseInt(jsonObj.getString("value")));
                                tempContainers.add(Integer.parseInt(jsonObj.getString("value")));
                                berapa = jsonObj.getString("id");

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
            }

            @Override
            public void onFailure(Call<ValueBookmarks> call, Throwable t) {

                Toast.makeText(JoinActivity.this,"Error Network", Toast.LENGTH_SHORT).show();
            }


        });




    }

    // untuk meload data mahasiswa
    private void loadData(){
        data = mChart.getData();
        if(data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);


            if (set == null) {
                set = createSet(); //masih off
                data.addDataSet(set);
            }
        }


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

                        chart_list = response.body().getResult();

                        viewAdapter = new RecyclerViewAdapter(JoinActivity.this, chart_list);
                        recyclerView.setAdapter(viewAdapter);

                        try {

                            JSONArray jsonArr = new JSONArray(data);

                            String berapa = "0";
                            for (int i = 0; i < jsonArr.length(); i++) {

                                JSONObject jsonObj = jsonArr.getJSONObject(i);

                                addEntry(Integer.parseInt(jsonObj.getString("y")));

                                berapa = jsonObj.getString("id");

                            }

// /

                            realtimeCount = berapa;

                            Log.d("realcount LoadData@@:",realtimeCount);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        iteration++;
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {

                Toast.makeText(JoinActivity.this,"Error Network", Toast.LENGTH_SHORT).show();
            }


        });


        iteration = 1;


    }


    // mpa lagi

    int[] mColors = ColorTemplate.VORDIPLOM_COLORS;

    private void addEntry(int masukan) {


        data = mChart.getData();
        if(data != null) {
//            int test = Integer.parseInt(editY.getText().toString()); masih off
            ILineDataSet set = data.getDataSetByIndex(0);

            // add a new x-value first
            data.addXValue(set.getEntryCount() + "");

            // choose a random dataSet
            int randomDataSetIndex = (int) (Math.random() * data.getDataSetCount());
            //System.out.println("randomDataSetIndex: "+randomDataSetIndex);

            // tambah dari disini dari db
            data.addEntry(new Entry((float) masukan, set.getEntryCount()) , 0);

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            mChart.setVisibleXRangeMaximum(220);
            mChart.setVisibleYRangeMaximum(220, YAxis.AxisDependency.LEFT);


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
                // or remove by index
                // mData.removeEntry(xIndex, dataSetIndex);

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

            ILineDataSet set = data.getDataSetByIndex(0);
            Entry e = set.getEntryForXIndex(set.getEntryCount() - 1);

//            Log.d("datasetnya @@", String.valueOf(data.getDataSetByIndex(0).getEntryCount()));

            data.removeDataSet(data.getDataSetByIndex(data.getDataSetCount() - 1));

            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
    }


    private void removeAll() {


        data = mChart.getData();
        Log.d("getdata:",String.valueOf(data));

        if(data != null) {
            Log.d("masuk",String.valueOf(data));


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

                    String value = response.body().getValue();

                    progressBar.setVisibility(View.GONE);

                    if (value.equals("1")) { // nilai satu means bisa menghubungi server

                        String data = new Gson().toJson(response.body().getResult()).toString();
                        chart_list = response.body().getResult();
                        viewAdapter = new RecyclerViewAdapter(JoinActivity.this, chart_list);
                        recyclerView.setAdapter(viewAdapter);

                        try {

                            JSONArray jsonArr = new JSONArray(data);

                            ArrayList<String> xVals = new ArrayList<String>();

                            for(int i=0;i<=jsonArr.length()-1;i++){

                                xVals.add((i) + "");

                            }

                            for (int i = 0; i < jsonArr.length(); i++) {

                                JSONObject jsonObj = jsonArr.getJSONObject(i);

                                yVals.add(new Entry(Integer.parseInt(jsonObj.getString("y")), i));

                                Log.d("Y:",String.valueOf(yVals));

                            }



                            Log.d("Coba masukkan ke chart",String.valueOf(yVals));
                            // create a dataset and give it a type
                            LineDataSet set1 = new LineDataSet(yVals, "0");

                            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                            dataSets.add(set1); // add the datasets

                            // create a data object with the datasets
                            LineData data1 = new LineData(xVals, dataSets);

                            // set data
                            mChart.setData(data1);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }

                @Override
                public void onFailure(Call<Value> call, Throwable t) {

                    Toast.makeText(JoinActivity.this,"Error Network", Toast.LENGTH_SHORT).show();
                }


            });


            //mas


        }
    }

    private void removeAllSet() {

        LineData data = mChart.getData();

        if(data != null) {

            for(int i=1;i<=data.getDataSetByIndex(0).getEntryCount();i++){

                removeLastEntry();

            }

            // untuk menghapus dataset
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
        return true;
    }



    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        float volume= ((e.getVal()/(mChart.getYChartMax()-mChart.getYChartMin()))*5);

        // play sound
        mySound.play(raygunID, 1, 1, 1, 0, volume);//        customVibratePatternNoRepeat();




        // play ketika ada bookmarks

        if(tempContainers.contains(e.getVal())){

            customVibratePatternNoRepeat();

        }


        // 500ms
        morseVibrate((int) (e.getXIndex() +1));

        Log.w("dataset x:",String.valueOf(dataSetIndex + 1));

        Log.w("dataset x:",String.valueOf(dataSetIndex + 1));
        Log.w("highligh x:",String.valueOf(dataSetIndex + 1));
        Log.w("@@mget y: ",String.valueOf(mChart.getY()));
        Log.w("@@mChart.getXAxis() x:",String.valueOf(mChart.getXAxis()));



        Log.w("@@mChart.getX() x:",String.valueOf(mChart.getX()));
        Log.w("@@e.getX() x:",String.valueOf(e.getXIndex()));
        Log.w("@@e.getData() x:",String.valueOf(e.getData()));
        Log.w("@@e.getVal() x:",String.valueOf(e.getVal()));
        Log.w("@@e.getVal() x:",String.valueOf(e.getVal()));


        ViewPortHandler handler = mChart.getViewPortHandler();

        PointD topLeft = mChart.getValuesByTouchPoint(handler.contentLeft(), handler.contentTop(), YAxis.AxisDependency.LEFT);
        PointD bottomRight = mChart.getValuesByTouchPoint(handler.contentRight(), handler.contentBottom(), YAxis.AxisDependency.LEFT);

        Log.w("@@x:",String.valueOf(handler.getScaleX()));
        Log.w("@@y:",String.valueOf(handler.getScaleY()));
        Log.w("@@getTransX:",String.valueOf(handler.getTransX()));
        Log.w("@@getTransY:",String.valueOf(handler.getTransY()));



//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createWaveFormVibrationUsingVibrationEffectAndAmplitude();
//        }
//        customVibratePatternRepeatFromSpecificIndex();

    }

    @Override
    public void onNothingSelected() {

    }
//
//

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("action", "onPause");

        stop = true; // matikan thread
        Thread.interrupted();

        t1.interrupt();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("action", "onStop");

        stop = true;

        Thread.interrupted();
//
//        finish();
//
        t1.interrupt();
//        t1.stop();
    }


    @Override
    protected void onResume(){
        super.onResume();
        Log.d("action", "OnResume");

    }
    //
    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d("action", "OnRestart");
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.w("Gesture@@1", "START, x@@#: " + me.getX() + ", y@@#: " + me.getY());

        // getar atas
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width10Percent = (int) (displayMetrics.widthPixels - (displayMetrics.widthPixels * 0.2));
        Log.w("height@@1", "height, height@@#: " + height + ", width@@#: " + width10Percent);


        // kalo di luar garis
        if(me.getX() <=60){

            vibrateFor500ms();

        }else if(me.getX() >= width10Percent){

            vibrateFor500ms();

        }else if(me.getY() <=60 ){

            vibrateFor500ms();
        }

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

        // getar atas
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width10Percent = (int) (displayMetrics.widthPixels - (displayMetrics.widthPixels * 0.2));
        Log.w("height@@1", "height, height@@#: " + height + ", width@@#: " + width10Percent);


        // kalo di luar garis
        if(me.getX() <=60){

            vibrateFor500ms();

        }else if(me.getX() >= width10Percent){

            vibrateFor500ms();

        }else if(me.getY() <=60 ){

            vibrateFor500ms();
        }


        // un-highlight values after the gesture is finished and no single-tap
        if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
    }
    @Override
    public void onChartLongPressed(final MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
        final Entry h = mChart.getEntryByTouchPoint(me.getX(), me.getY());
        int xIndex = h.getXIndex();
        tempContainers.add((int) h.getVal());


        Log.w("LongPress@@! getIndex", String.valueOf(xIndex));
        Log.w("LongPress@@! getVal", String.valueOf(h.getVal()));
        Log.w("LongPress@@! getData", String.valueOf(h.getData()));
        Log.w("LongPress@@! getX", String.valueOf(me.getX()));
        Log.w("LongPress@@! getY", String.valueOf(me.getY()));
        Log.w("tempContainers@@! TempY", String.valueOf(tempContainers));


        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Bookmark Dialog");
        alertDialogBuilder.setMessage("Do you want to bookmark this point?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Bookmark This", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {




//                btnviewAll.performClick();
//                mChart.setOnChartValueSelectedListener();

                // save to db

                x_var = editX.getText().toString();
                y_var = editY.getText().toString();
                chart_id_var = chart_id.getText().toString();
                category_var = category.getText().toString();




                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RegisterAPI api = retrofit.create(RegisterAPI.class);
                Call<ValueBookmarks> call = api.InsertBookmarks("1",String.valueOf(h.getVal()),chart_id_var);

                call.enqueue(new Callback<ValueBookmarks>(){
                    @Override
                    public void onResponse(Call<ValueBookmarks> call, Response<ValueBookmarks> response) {
                        String value = response.body().getValue();
                        String message = response.body().getMessage();

                        if(value.equals("1")){


                            // masukkan ke bookmark array

                            tempContainers.add((int) h.getVal());

                        }else{
//                            Toast.makeText(CreateActivity.this, "failed to add data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ValueBookmarks> call, Throwable t) {
                        t.printStackTrace();
//                        progress.dismiss();
//                        Toast.makeText(CreateActivity.this,"Error Connection",Toast.LENGTH_SHORT).show();
                    }
                });


                // end of save  to Db

            }
        }).setNegativeButton("Play", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();



    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    //@Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    public class Task1 implements Runnable {
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {

                while ( !stop ) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            chart_id_var = chart_id.getText().toString();
                            category_var = category.getText().toString();

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(URL)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            RegisterAPI api = retrofit.create(RegisterAPI.class);

                            Call<Value> call = api.getLastChartById(chart_id_var);

                            call.enqueue(new Callback<Value>(){
                                @Override
                                public void onResponse(Call<Value> call, Response<Value> response) {
                                    String value = response.body().getValue();
                                    String message = response.body().getMessage();

                                    if(value.equals("1")){

                                        String data = new Gson().toJson(response.body().getResult()).toString();

                                        try {

                                            JSONArray jsonArr = new JSONArray(data);


                                            for (int i = 0; i < jsonArr.length(); i++) {

                                                JSONObject jsonObj = jsonArr.getJSONObject(i);

                                                if(realtimeCount.equals(jsonObj.getString("id"))){

                                                    Log.d("@@realtime","no update");

                                                }else{
                                                    Log.d("@@realtime","update");
                                                    Log.d("@@realtimevar",realtimeCount);
                                                    Log.d("@@id",jsonObj.getString("id"));


                                                    realtimeCount = jsonObj.getString("id");

                                                    Toast.makeText(JoinActivity.this, "Chart updated", Toast.LENGTH_SHORT).show();


                                                    // refresth logika2

                                                    Intent intent = getIntent();
                                                    String chartName = intent.getStringExtra("chartName");
                                                    String chartId = intent.getStringExtra("chartId");

                                                    String chart_id_var = chart_id.getText().toString();

                                                    Intent pindah = new Intent(JoinActivity.this, JoinActivity.class);

                                                    pindah.putExtra("chartName",chart_id_var);
                                                    pindah.putExtra("chartId",chart_id_var);

                                                    startActivityForResult(pindah,1);
//--

                                                }

                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }



                                    }else{
                                        Toast.makeText(JoinActivity.this, "fail to remove data", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Value> call, Throwable t) {
                                    t.printStackTrace();


                                }
                            });

                        }
                    });

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    private void feedMultiple() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                while ( !stop ) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            chart_id_var = chart_id.getText().toString();
                            category_var = category.getText().toString();

                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(URL)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            RegisterAPI api = retrofit.create(RegisterAPI.class);

                            Call<Value> call = api.getLastChartById(chart_id_var);

                            call.enqueue(new Callback<Value>(){
                                @Override
                                public void onResponse(Call<Value> call, Response<Value> response) {
                                    String value = response.body().getValue();
                                    String message = response.body().getMessage();

                                    if(value.equals("1")){

                                        String data = new Gson().toJson(response.body().getResult()).toString();

                                        try {

                                            JSONArray jsonArr = new JSONArray(data);


                                            for (int i = 0; i < jsonArr.length(); i++) {

                                                JSONObject jsonObj = jsonArr.getJSONObject(i);

                                                if(realtimeCount.equals(jsonObj.getString("id"))){

                                                    Log.d("@@realtime","no update");

                                                }else{
                                                    Log.d("@@realtime","update");
                                                    Log.d("@@realtimevar",realtimeCount);
                                                    Log.d("@@id",jsonObj.getString("id"));


                                                    realtimeCount = jsonObj.getString("id");

                                                    //-- logika1
//                                                    removeAllSet();
////                                                    removeDataSet();
//                                                    loadData();
                                                    Toast.makeText(JoinActivity.this, "Chart updated", Toast.LENGTH_SHORT).show();
//---


                                                    // refresth logika2

                                                    Intent intent = getIntent();
                                                    String chartName = intent.getStringExtra("chartName");
                                                    String chartId = intent.getStringExtra("chartId");

                                                    String chart_id_var = chart_id.getText().toString();

                                                    Intent pindah = new Intent(JoinActivity.this, JoinActivity.class);

                                                    pindah.putExtra("chartName",chart_id_var);
                                                    pindah.putExtra("chartId",chart_id_var);

                                                    startActivityForResult(pindah,1);
//--

                                                }

                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }



                                    }else{
                                        Toast.makeText(JoinActivity.this, "fail to remove data", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Value> call, Throwable t) {
                                    t.printStackTrace();

//                                    Toast.makeText(JoinActivity.this,"Error Connection",Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    });

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    // vibration

    private void vibrateFor500ms() {
        vibrator.vibrate(500);
    }

    private void customVibratePatternNoRepeat() {

        // 0 : Start without a delay
        // 400 : Vibrate for 400 milliseconds
        // 200 : Pause for 200 milliseconds
        // 400 : Vibrate for 400 milliseconds
        long[] mVibratePattern = new long[]{0, 400, 200, 400};

        // -1 : Do not repeat this pattern
        // pass 0 if you want to repeat this pattern from 0th index
        vibrator.vibrate(mVibratePattern, -1);

    }


    private void morseVibrate( int input) {

        // 0 : Start without a delay
        // 400 : Vibrate for 400 milliseconds
        // 200 : Pause for 200 milliseconds
        // 400 : Vibrate for 400 milliseconds
        if(input %5 == 0){
            int wave =input * 100;
            long[] mVibratePattern = new long[]{0, wave, 50, wave};
            vibrator.vibrate(mVibratePattern, -1);

        }else{

            vibrator.vibrate(100);

        }


        // -1 : Do not repeat this pattern
        // pass 0 if you want to repeat this pattern from 0th index
//        vibrator.vibrate(mVibratePattern, -1);

    }


    private void customVibratePatternRepeatFromSpecificIndex() {
        long[] mVibratePattern = new long[]{0, 400, 800, 600, 800, 800, 800, 1000};

        // 3 : Repeat this pattern from 3rd element of an array
        vibrator.vibrate(mVibratePattern, 3);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createOneShotVibrationUsingVibrationEffect() {
        // 1000 : Vibrate for 1 sec
        // VibrationEffect.DEFAULT_AMPLITUDE - would perform vibration at full strength
        VibrationEffect effect = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);
        vibrator.vibrate(effect);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createWaveFormVibrationUsingVibrationEffect() {
        long[] mVibratePattern = new long[]{0, 400, 1000, 600, 1000, 800, 1000, 1000};
        // -1 : Play exactly once
        VibrationEffect effect = VibrationEffect.createWaveform(mVibratePattern, -1);
        vibrator.vibrate(effect);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createWaveFormVibrationUsingVibrationEffectAndAmplitude() {

        long[] mVibratePattern = new long[]{0, 400, 800, 600, 800, 800, 800, 1000};
        int[] mAmplitudes = new int[]{0, 255, 0, 255, 0, 255, 0, 255};
        // -1 : Play exactly once

        if (vibrator.hasAmplitudeControl()) {
            VibrationEffect effect = VibrationEffect.createWaveform(mVibratePattern, mAmplitudes, -1);
            vibrator.vibrate(effect);
        }
    }




}