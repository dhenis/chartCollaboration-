package com.example.deni.chartcollaboration.api;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

import com.example.deni.chartcollaboration.model.Value;
import com.example.deni.chartcollaboration.model.ValueAccountManager;
import com.example.deni.chartcollaboration.model.ValueChartManager;
import com.example.deni.chartcollaboration.model.ValueWorkgroups;

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
    @POST ("workgroup_add.php")// input data android ke DB
    Call<ValueWorkgroups> addWorkgroup1(  @Field("name") String name,
                                          @Field("access") String access);

    @FormUrlEncoded
    @POST ("chart_manager_add.php")// input data android ke DB
    Call<ValueWorkgroups> addChartManager( @Field("name") String name,
                                           @Field("status") String status);

    @FormUrlEncoded
    @POST ("workgroup_delete.php")// input data android ke DB
    Call<ValueWorkgroups> deleteWorkgroup(@Field("name") String name);


    @FormUrlEncoded
    @POST ("view.php")// input data android ke DB
    Call<Value> view( @Field("chart_id") String chart_id );

    @FormUrlEncoded
    @POST ("view_last_chart.php")// input data android ke DB
    Call<Value> getLastValueFromChart( @Field("chart_id") String chart_id );

    @FormUrlEncoded
    @POST ("account_manager_view.php")// input data android ke DB
    Call<ValueAccountManager> viewAccountManagerById(@Field("chart_id") String chart_id );

    @FormUrlEncoded
    @POST ("account_manager_delete.php")// input data android ke DB
    Call<ValueAccountManager> deleteAccountManagerById(@Field("account_id") String account_id );

    @FormUrlEncoded
    @POST ("chat_manager_view.php")// input data android ke DB
    Call<ValueChartManager> viewChartManagerById( @Field("chart_id") String chart_id );

    @FormUrlEncoded
    @POST ("login.php")// input data android ke DB
    Call<ValueAccountManager> getUsername( @Field("username") String username ,
                                           @Field("password") String password);

    @GET ("view_all.php")// input data android ke DB
    Call<Value> view_all();

    @GET("view_workgroup.php")
    Call<ValueWorkgroups> viewWorkgroup();


    @FormUrlEncoded
    @POST ("view_workgroup_by_account_id.php")// input data android ke DB
    Call<ValueWorkgroups> viewChartManagerByUsername( @Field("account_id") String account_id );


    @GET("chat_manager_view.php")
    Call<ValueChartManager> viewChartManager();

    @GET("chat_manager_view.php")
    Call<ValueChartManager> getLastChartId();


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
