package com.example.deni.chartcollaboration.api;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import com.example.deni.chartcollaboration.model.Value;
/**
 * Created by deni on 19/06/2018.
 */

// penghubung dari android ke API
    // just make an interface, because retrofit has done it
//{"value":1,"result":[{"x":"1","y":"1","chart_id":"1","code":"1","category":"1","id":"1"}]}
// $x = $_POST['x'];
//         $y = $_POST['y'];
//         $chart_id = $_POST['chart_id'];
//         $code = $_POST['code'];
//         $category = $_POST['category'];



public interface RegisterAPI {
    @FormUrlEncoded
    @POST ("insert.php")// input data android ke DB
    Call<Value> daftar(@Field("x") String x,
                       @Field("y") String y,
                       @Field("chart_id") String chart_id,
                       @Field("code") String code,
                       @Field("category") String category);
    @FormUrlEncoded
    @POST ("view.php")// input data android ke DB
    Call<Value> view( @Field("chart_id") String chart_id );

    @FormUrlEncoded
    @POST ("view_last_chart.php")// input data android ke DB
    Call<Value> getLastValueFromChart( @Field("chart_id") String chart_id );


    @GET ("view_all.php")// input data android ke DB
    Call<Value> view_all();

    @FormUrlEncoded
    @POST("search.php")
    Call<Value> search(@Field("search")String search);

    @FormUrlEncoded
    @POST("delete.php")
    Call<Value> hapus(@Field("nim")String nim);


    @FormUrlEncoded
    @POST("update.php")
    Call<Value> ubah(@Field("nim") String nim,
                     @Field("nama") String nama,
                     @Field("jurusan") String jurusan,
                     @Field("jk") String jk
    );


}
