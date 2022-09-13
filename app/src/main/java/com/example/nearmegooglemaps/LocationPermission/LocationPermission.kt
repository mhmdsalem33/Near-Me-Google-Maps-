package com.example.nearmegooglemaps.LocationPermission

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class LocationPermission {



    //Location
     private  lateinit var locationRequest    : LocationRequest
     lateinit var locationCallback            : LocationCallback
     lateinit var fusedLocationProviderClient : FusedLocationProviderClient


     fun initLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority             = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.fastestInterval      = 3000
        locationRequest.interval             = 5000
        locationRequest.smallestDisplacement = 10f

    }

     fun initLocationCallback(mMap: GoogleMap) {
        locationCallback = object :LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                // اول لما البرنامج يفتح
               // val newPosition = LatLng(locationResult!!.lastLocation.latitude , locationResult.lastLocation.longitude)
                   // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 18f))
            }
        }
    }

    fun initFusedLocationProviderClient(context: Context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }


    fun requestPermission(context : Context , mMap : GoogleMap){
        //request permission
        Dexter.withContext(context)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {

                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }

                    //enable button
                     mMap.isMyLocationEnabled = true
                     mMap.uiSettings.isMyLocationButtonEnabled = true
                     getCurrentLocation(mMap , context)

                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(context, "Dined "+p0!!.permissionName, Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?
                ) {}

            }).check()

    }


    fun enableButtonLocation(mMap: GoogleMap , context: Context){
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
    }


    @SuppressLint("MissingPermission")
    fun getCurrentLocation(map: GoogleMap , context: Context){
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null)
            {
                val userLocation= LatLng(location.latitude , location.longitude)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation , 18f))
                    map.clear()
                    map.addMarker(MarkerOptions().position(userLocation).title("Mohamed").snippet("13 California").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                Log.d("testApp" , "${location.latitude} ,   ${location.longitude}")
            }
            else
            {
                Toast.makeText(context, "Please enable your GBS", Toast.LENGTH_SHORT).show()
            }
        }
    }




}