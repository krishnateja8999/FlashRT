package com.example.flashnew.Server.retrofitRelated;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIservice {


    @Multipart
    @POST("/api/upsert-trip-case")
    Call<JsonElement> insert(@Header("UserType") String type, @Header("UserId") String userid, @Part("FormData") RequestBody form_data, @Part List<MultipartBody.Part> files);

    @PUT("/FlashPegasus/api/rt/lista/v1/{lista}")
    Call<JsonElement> send(@Path("lista") String listID, @Header("x-versao-rt") String xVersion, @Header("x-rastreador") String identity, @Body JSONObject jsonBody, @Body JSONArray jsonArray);

    @GET("/FlashPegasus/api/rt/login/v1/authenticate")
    Call<JsonElement> login();

}
