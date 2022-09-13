package com.example.nearmegooglemaps.MapStyle

import android.content.Context
import android.util.Log
import android.view.MenuItem
import com.example.nearmegooglemaps.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MapStyleOptions
import java.lang.Exception

class MapStyle {


    fun getMapStyle(item : MenuItem , googleMap: GoogleMap , context : Context)
    {
       when(item.itemId)
       {
            R.id.normal_style -> {
                try {
                    val success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context , R.raw.normal_style))
                    if (!success)
                    {
                        Log.d("testApp", "  Failed Map Style Normal")
                    }
                }catch (e: Exception)
                {
                    Log.d("testApp", "${e.message}  Failed Map Style Normal")
                }
            }

           R.id.silver_style -> {
               try {
                   val success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context , R.raw.silver_map_style))
                   if (!success)
                   {
                       Log.d("testApp", " Failed Map Style Silver")
                   }
               }catch (e: Exception)
               {
                   Log.d("testApp", "${e.message}  Failed Map Style Silver")
               }
           }

           R.id.retro_style     -> {
               try {
                   val success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.retro_map_style)
                   )
                   if (!success) {
                       Log.d("testApp", "Failed Map Style Retro")
                   }
               } catch (e: Exception) {
                   Log.d("testApp", "${e.message}  Failed Map Style Retro")
               }
           }
           R.id.dark_style      -> {
               try {
                   val success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle( context , R.raw.dark_map_style ))
                   if (!success)
                   {
                       Log.d("testApp" , "Failed Map Style Dark")
                   }
               }catch (e : Exception)
               {
                   Log.d("testApp" , "${e.message}  Failed Map Style Dark")
               }
           }
           R.id.night_style     -> {
               try {
                   val success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle( context , R.raw.night_map_style ))
                   if (!success)
                   {
                       Log.d("testApp" , "Failed Map Style Night")
                   }
               }catch (e : Exception)
               {
                   Log.d("testApp" , "${e.message}  Failed Map Style Night")
               }
           }
           R.id.Aubergine_style -> {
               try {
                   val success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle( context , R.raw.map_style ))
                   if (!success)
                   {
                       Log.d("testApp" , "Failed Map Style Aubergine")
                   }
               }catch (e : Exception)
               {
                   Log.d("testApp" , "${e.message}  Failed Map Style Aubergine")
               }
           }






       }


    }

}