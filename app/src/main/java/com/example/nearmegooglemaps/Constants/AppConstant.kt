package com.example.nearmegooglemaps.Constants

import com.example.nearmegooglemaps.R
import com.example.nearmegooglemaps.data.NearPLacesData.PlaceModel

class AppConstant {


    val placesName =
        listOf<PlaceModel>(
            PlaceModel(1  , R.drawable.ic_restaurant   , "Restaurant"         , "restaurant"),
            PlaceModel(2  , R.drawable.ic_atm          , "ATM"                , "atm"),
            PlaceModel(3  , R.drawable.ic_gas_station  , "Gas"                , "gas_station"),
            PlaceModel(4  , R.drawable.ic_shopping_cart, "Groceries"          , "supermarket"),
            PlaceModel(5  , R.drawable.ic_hotel        , "Hotels"             , "hotel"),
            PlaceModel(6  , R.drawable.ic_pharmacy     , "Pharmacies"         , "pharmacy"),
            PlaceModel(7  , R.drawable.ic_hospital     , "Hospitals & Clinics", "hospital"),
            PlaceModel(8  , R.drawable.ic_car_wash     , "Car Wash"           , "car_wash"),
            PlaceModel(9  , R.drawable.ic_saloon       , "Beauty Salons"      , "beauty_salon"),
            PlaceModel(10 , R.drawable.mosque          , "Mosques"            , "mosque")
        )
}

