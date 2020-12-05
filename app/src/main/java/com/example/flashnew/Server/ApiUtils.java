package com.example.flashnew.Server;

import com.example.flashnew.BuildConfig;
import com.example.flashnew.Server.retrofitRelated.APIservice;
import com.example.flashnew.Server.retrofitRelated.RetrofitClient;

import static com.example.flashnew.BuildConfig.BASE_URL;

public class ApiUtils {

    //    private static final String BASE_URL= "http://191.241.243.133:8080";
    public static final String BASE_URL1 = BuildConfig.BASE_URL;

    public static final String LOGIN = BASE_URL + "/FlashPegasus/api/rt/login/v1/authenticate";
    public static final String GET_LIST = BASE_URL + "/FlashPegasus/api/rt/lista/v1/";

    public static APIservice getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIservice.class);
    }

}
