package com.testapp.retrofit;

import com.testapp.models.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface APIPlug
{
    @GET("people")
    Call<PeopleResults> searchPeople(@Query("search") String search);

}

