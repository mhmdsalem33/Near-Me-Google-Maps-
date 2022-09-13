package com.example.nearmegooglemaps.NetWork

import com.example.nearme.Models.test.PlacesModelList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface RetrofitApi {

    @GET()
    suspend fun getNearPlaces(@Url url :String) : Response<PlacesModelList>

}