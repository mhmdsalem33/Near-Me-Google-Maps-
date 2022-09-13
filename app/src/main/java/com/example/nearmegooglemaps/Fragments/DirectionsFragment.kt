package com.example.nearmegooglemaps.Fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nearmegooglemaps.LocationPermission.LocationPermission
import com.example.nearmegooglemaps.R
import com.example.nearmegooglemaps.data.DirectionsData.MapData
import com.example.nearmegooglemaps.databinding.FragmentDirectionsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request


class DirectionsFragment : Fragment() , OnMapReadyCallback {

    private lateinit var binding : FragmentDirectionsBinding
    private lateinit var mMap    : GoogleMap
    private  val locationRequest  by lazy { LocationPermission() }
    private  var originLatitude          : Double ? = null
    private  var originLongitude         : Double ? = null
    private  var destinationLatitude     : Double ? = null
    private  var destinationLongitude    : Double ? = null
    private  var isTrafficEnable   : Boolean  = false
    var travelMode = "driving"





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDirectionsBinding.inflate(inflater , container , false)
        return   binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val mapFragment = childFragmentManager.findFragmentById(R.id.directionMap) as SupportMapFragment
            mapFragment.getMapAsync(this)

            getInformationByBundle()

    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap



        //  locationRequest.initLocationRequest()
          //locationRequest.initLocationCallback(mMap)
          //locationRequest.initFusedLocationProviderClient(requireContext())
          //locationRequest.requestPermission(requireContext() , mMap)
          locationRequest.enableButtonLocation(mMap , requireContext())

        markLocationsAndMoveCamera()
        getDirectionsUrl()
        enableTraffic()

    }

    private fun getDirectionsUrl() {

        val origin      = LatLng(originLatitude!! , originLongitude!!)
        val destination = LatLng(destinationLatitude!! , destinationLongitude!!)
        val url = getDirectionURL(origin, destination , "driving")
            GetDirection(url).execute()



        binding.travelMode.setOnCheckedChangeListener{ _ , checked ->


            if (checked != -1)
            {
                when(checked)
                {
                    R.id.btnChipDriving -> {
                            mMap.clear()
                        val callUrl = getDirectionURL(origin, destination , "driving")
                            GetDirection(callUrl).execute()
                            travelMode = "driving"
                    }
                    R.id.btnChipWalking ->
                    {
                            mMap.clear()
                        val callUrl = getDirectionURL(origin, destination , "walking")
                            GetDirection(callUrl).execute()
                            travelMode = "walking"
                    }
                    R.id.btnChipBike ->
                    {
                            mMap.clear()
                        val callUrl = getDirectionURL(origin, destination , "bicycling")
                            GetDirection(callUrl).execute()
                            travelMode = "bicycling"
                    }
                    R.id.btnChipTrain ->
                    {
                            mMap.clear()
                        val callUrl = getDirectionURL(origin, destination , "transit")
                            GetDirection(callUrl).execute()
                            travelMode = "transit"
                    }
                }
            }
        }
    }


    private fun getInformationByBundle() {
        val data = arguments
        if (data != null)
        {
            originLatitude       = data.getDouble("originLatitude")
            originLongitude      = data.getDouble("originLongitude")
            destinationLatitude  = data.getDouble("destinationLatitude")
            destinationLongitude = data.getDouble("destinationLongitude")
        }
    }


    private fun markLocationsAndMoveCamera() {

        val origin      = LatLng(originLatitude!! , originLongitude!!)
        val destination = LatLng(destinationLatitude!! , destinationLongitude!!)
            mMap.addMarker(MarkerOptions().position(origin))
            mMap.addMarker(MarkerOptions().position(destination))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin ,18f))
    }



    private fun getDirectionURL(origin: LatLng, dest: LatLng , mode :String) : String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=" + mode +
                "&key=Your_Api"
    }



    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url : String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()
            Log.d("testApp" , data)

            val result =  ArrayList<List<LatLng>>()
            try{
                val respObj = Gson().fromJson(data, MapData::class.java)
                val path    =  ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size){
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))

                    if (respObj.routes[0].legs[0].start_address.isNotEmpty() && respObj.routes[0].legs[0].end_address.isNotEmpty())
                    {
                        binding.txtStartLocation.text =  respObj.routes[0].legs[0].start_address
                        binding.txtEndLocation.text   =  respObj.routes[0].legs[0].end_address

                    }
                }
                result.add(path)
            }catch (e:Exception){
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            if (travelMode == "driving")
            {
                val pattern = listOf(Dash(30f))
                val polyLine = PolylineOptions()
                for (i in result.indices){
                    polyLine.addAll(result[i])
                    polyLine.width(10f)
                    polyLine.color(Color.BLACK)
                    polyLine.startCap(SquareCap())
                    polyLine.jointType(JointType.ROUND)
                    polyLine.geodesic(true)
                    polyLine.pattern(pattern)
                }
                mMap.addPolyline(polyLine)
            }
            else if (travelMode == "walking")
             {
                 val pattern = listOf(Dot() , Gap(10f) )
                 val polyLine = PolylineOptions()
                 for (i in result.indices){
                     polyLine.addAll(result[i])
                     polyLine.width(10f)
                     polyLine.color(Color.BLACK)
                     polyLine.startCap(SquareCap())
                     polyLine.jointType(JointType.ROUND)
                     polyLine.geodesic(true)
                     polyLine.pattern(pattern)
                 }
                 mMap.addPolyline(polyLine)
             }
            else if (travelMode == "bicycling")
            {
                val pattern = listOf(Dash(30f))
                val polyLine = PolylineOptions()
                for (i in result.indices){
                    polyLine.addAll(result[i])
                    polyLine.width(10f)
                    polyLine.color(Color.BLACK)
                    polyLine.startCap(SquareCap())
                    polyLine.jointType(JointType.ROUND)
                    polyLine.geodesic(true)
                    polyLine.pattern(pattern)
                }
                mMap.addPolyline(polyLine)
            }
            else if (travelMode == "transit")
            {
                val pattern = listOf(Dot() , Gap(10f) )
                val polyLine = PolylineOptions()
                for (i in result.indices){
                    polyLine.addAll(result[i])
                    polyLine.width(10f)
                    polyLine.color(Color.BLACK)
                    polyLine.startCap(SquareCap())
                    polyLine.jointType(JointType.ROUND)
                    polyLine.geodesic(true)
                    polyLine.pattern(pattern)
                }
                mMap.addPolyline(polyLine)
            }


        }
    }

    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
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


}





