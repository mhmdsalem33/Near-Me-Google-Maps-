package com.example.nearmegooglemaps.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import com.example.nearme.Models.test.Result
import com.example.nearmegooglemaps.Constants.AppConstant
import com.example.nearmegooglemaps.LocationPermission.LocationPermission
import com.example.nearmegooglemaps.Activites.MainActivity
import com.example.nearmegooglemaps.Adapters.CustomInfoAdapter
import com.example.nearmegooglemaps.MapStyle.MapStyle
import com.example.nearmegooglemaps.MapType.MapType
import com.example.nearmegooglemaps.R
import com.example.nearmegooglemaps.ViewModels.GetNearPlacesViewModel
import com.example.nearmegooglemaps.databinding.FragmentHomeBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.chip.Chip
import kotlinx.coroutines.runBlocking


class HomeFragment : Fragment() , OnMapReadyCallback , GoogleMap.OnMarkerClickListener,  GoogleMap.OnInfoWindowClickListener{


    private lateinit var binding  : FragmentHomeBinding
    private lateinit var mMap     : GoogleMap
    private val requestPermission by lazy { LocationPermission() }
    private val mapType           by lazy { MapType() }
    private val mapStyle          by lazy { MapStyle()}
    private val appConstant       by lazy { AppConstant() }
    private var isTrafficEnable   : Boolean  = false
    private lateinit var nearMvvm : GetNearPlacesViewModel






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nearMvvm = (context as MainActivity).getNearMvvm



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater , container , false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


     val mapFragment = childFragmentManager.findFragmentById(R.id.homeMap) as SupportMapFragment
         mapFragment.getMapAsync(this)

         chip()

    }





    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        requestPermission.initLocationRequest()
        requestPermission.initLocationCallback(mMap)
        requestPermission.initFusedLocationProviderClient(requireContext())
        requestPermission.requestPermission(requireContext() , mMap)
        requestPermission.enableButtonLocation(mMap , requireContext())

        binding.currentLocation.setOnClickListener {
            requestPermission.getCurrentLocation(map = mMap , requireContext())

        }



        mapType()
        getMapStyle()
        enableTraffic()

        mMap.setOnMarkerClickListener(this)
        mMap.setOnInfoWindowClickListener(this)
        mMap.setInfoWindowAdapter(CustomInfoAdapter(requireContext()))

    }


    private fun getMapStyle() {
       binding.btnMapStyle.setOnClickListener {
           val popupMenu = PopupMenu(requireContext() , it)
               popupMenu.apply {
                   menuInflater.inflate(R.menu.menu_map_style , popupMenu.menu)
                   setOnMenuItemClickListener { item ->
                       mapStyle.getMapStyle(item  , mMap , requireContext())
                       true
                   }
                   show()
               }
       }
    }
    
    private fun mapType(){
        binding.btnMapType.setOnClickListener {
            val popupMenu = PopupMenu(requireContext() , it)
                popupMenu.apply {
                menuInflater.inflate(R.menu.menu_map_type , popupMenu.menu)
                    setOnMenuItemClickListener {  item ->
                        mapType.getMapType(item , mMap)
                        true
                    }
                    show()
                }
        }
    }

    private fun enableTraffic() {
        binding.enableTraffic.setOnClickListener {
            if (isTrafficEnable)
            {
                mMap.apply {
                    isTrafficEnabled = false
                    isTrafficEnable  = false
                }
            }
            else
            {
                mMap.apply {
                    isTrafficEnabled = true
                    isTrafficEnable  = true
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun chip() {
        for (placeModel in appConstant.placesName)
        {
            val chip      = Chip(requireContext())
            chip.id   = placeModel.id
            chip.text = placeModel.name
            chip.setPadding(8,8,8,8)
            chip.setTextColor(resources.getColor(R.color.white , null))
            chip.chipBackgroundColor  = resources.getColorStateList(R.color.primaryColor , null)
            chip.chipIcon             = ResourcesCompat.getDrawable(resources , placeModel.drawableId , null)
            chip.chipIconTint         = resources.getColorStateList(R.color.white , null)
            chip.isCheckable          = true
            chip.isCheckedIconVisible = false
            binding.placesGroup.addView(chip)
        }

        binding.placesGroup.setOnCheckedChangeListener{ _, checkedId ->
            if (checkedId != -1)
            {
                val placeModel = appConstant.placesName[checkedId -1]
                binding.edtPlaceName.setText(placeModel.name)
                getNearPlaces(placeModel.placeType)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    private fun getNearPlaces(placeType: String) {
        requestPermission.fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null)
            {
                val url = ("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                        + location.latitude + "," + location.longitude
                        + "&radius=1500" + "&type=" + placeType + "&key=" +
                          "Your_Api")


                  nearMvvm.getNearPlaces(url)
                  nearMvvm.getNearPlaces.observe(viewLifecycleOwner){ places ->
                        if (places != null)
                        {
                            mMap.clear()
                            for (i in places.results.indices)
                            {
                                addMarkerForNearPlaces(places.results[i] , i)
                            }
                        }
                  }

            }
            else
            {
                Toast.makeText(context, "Please Check your GBS && Internet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun addMarkerForNearPlaces(result: Result, position: Int) {
        val markerOptions = MarkerOptions()
            .position(
                LatLng(
                    result.geometry.location.lat ,
                    result.geometry.location.lng)
            )
            .title(result.name)
            .snippet(result.vicinity)
            .icon(getCustomIcon())
             mMap.addMarker(markerOptions)?.tag = position

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getCustomIcon(): BitmapDescriptor {
        val background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_location)
            background?.setTint(resources.getColor(R.color.quantum_googred900))
            background?.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(
            background?.intrinsicWidth!!,
            background.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
            background.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    override fun onDestroy() {
        requestPermission.fusedLocationProviderClient.removeLocationUpdates(requestPermission.locationCallback)
        super.onDestroy()
    }

    @SuppressLint("MissingPermission")
    override fun onInfoWindowClick(marker: Marker) {
        requestPermission.fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null)
            {
                val fragment = DirectionsFragment()
                val bundle = Bundle()
                    bundle.putDouble("originLatitude"       , location.latitude)
                    bundle.putDouble("originLongitude"      , location.longitude)
                    bundle.putDouble("destinationLatitude"  , marker.position.latitude)
                    bundle.putDouble("destinationLongitude" , marker.position.longitude)
                fragment.arguments = bundle
                findNavController().navigate(R.id.action_homeFragment_to_directionsFragment , bundle)
            }
        }

    }

    override fun onMarkerClick(marker: Marker): Boolean {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position , 18f))
       return false
    }

}