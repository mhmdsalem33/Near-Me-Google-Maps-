package com.example.nearmegooglemaps.Repository

import android.util.Log
import com.example.nearme.Models.test.PlacesModelList
import com.example.nearmegooglemaps.NetWork.RetrofitInstance
import kotlinx.coroutines.runBlocking
import retrofit2.Response

class GetNearPlacesRepository {

    suspend fun getNearPlaces(url : String) :Response<PlacesModelList>  {
            val response = RetrofitInstance.api.getNearPlaces(url)
            if (response.isSuccessful)
            {
                Log.d("testApp" ,"connected places Api" )
            }
            else
            {
               Log.d("testApp" , response.code().toString())
               Log.d("testApp" , "Failed To connected places Api")
            }
        return response
    }
}