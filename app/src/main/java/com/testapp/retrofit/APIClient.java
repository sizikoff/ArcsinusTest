package com.testapp.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

public class APIClient {
    private static APIPlug REST_CLIENT;
    private static final String API_URL = "http://swapi.co/api/";

    static { setupRestClient(); }

    private APIClient() {
    }

    public static APIPlug get() {
        return REST_CLIENT;
    }



    private static void setupRestClient() {
        //перехватчик

        /*
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        */

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .writeTimeout(20_000, TimeUnit.MILLISECONDS)
                .connectTimeout(20_000,TimeUnit.MILLISECONDS)
                .readTimeout(45_000,TimeUnit.MILLISECONDS);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        REST_CLIENT = retrofit.create(APIPlug.class);
    }
}
