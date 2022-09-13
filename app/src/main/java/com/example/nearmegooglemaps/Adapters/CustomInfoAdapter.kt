package com.example.nearmegooglemaps.Adapters

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.nearmegooglemaps.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoAdapter(context: Context):GoogleMap.InfoWindowAdapter {

    private val contentView = (context as Activity).layoutInflater.inflate(R.layout.custom_info_adapter_row , null)

    override fun getInfoContents(marker: Marker): View? {
        renderView(marker , contentView)
        return contentView
    }

    override fun getInfoWindow(marker: Marker): View? {
        renderView(marker , contentView)
        return contentView
    }

    private fun renderView(marker: Marker? , contextView :View){

        val titleInfo        =  contextView.findViewById<TextView>(R.id.title_mosque)
            titleInfo.text   =  marker?.title

        val locationInfo      = contextView.findViewById<TextView>(R.id.location_info)
            locationInfo.text = marker?.snippet

    }
}