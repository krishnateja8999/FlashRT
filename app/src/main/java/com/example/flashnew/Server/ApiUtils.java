package com.example.flashnew.Server;

import com.example.flashnew.BuildConfig;

import static com.example.flashnew.BuildConfig.BASE_URL;

public class ApiUtils {

    //    private static final String BASE_URL= "http://191.241.243.133:8080";
    public static final String BASE_URL1 = BuildConfig.BASE_URL;

    public static final String LOGIN = BASE_URL + "/FlashPegasus/api/rt/login/v1/authenticate";
    public static final String GET_LIST = BASE_URL + "/FlashPegasus/api/rt/lista/v1/";
    public static final String GET_COLETA = BASE_URL + "/FlashOnline/rest/coleta/getconsulta";
    public static final String POST_COLETA = BASE_URL + "/FlashOnline/rest/rt/baixar/coleta";

}
