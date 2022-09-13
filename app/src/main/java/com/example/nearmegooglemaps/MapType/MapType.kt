package com.example.nearmegooglemaps.MapType

import android.view.MenuItem
import com.example.nearmegooglemaps.R
import com.google.android.gms.maps.GoogleMap

class MapType {
    fun getMapType(item : MenuItem , googleMap: GoogleMap)
    {
        when(item.itemId)
        {
            R.id.normalMap    ->  googleMap.mapType      = GoogleMap.MAP_TYPE_NORMAL
            R.id.hybridMap    ->  googleMap.mapType      = GoogleMap.MAP_TYPE_HYBRID
            R.id.satelliteMap ->  googleMap.mapType      = GoogleMap.MAP_TYPE_SATELLITE
            R.id.terrainMap   ->  googleMap.mapType      = GoogleMap.MAP_TYPE_TERRAIN
            R.id.noneMap      ->  googleMap.mapType      = GoogleMap.MAP_TYPE_NONE
        }
    }
}