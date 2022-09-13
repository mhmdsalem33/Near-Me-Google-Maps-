package com.example.nearmegooglemaps.Activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.nearmegooglemaps.R
import com.example.nearmegooglemaps.Repository.GetNearPlacesRepository
import com.example.nearmegooglemaps.ViewModels.GetNearPlacesViewModel
import com.example.nearmegooglemaps.ViewModels.GetNearPlacesViewModelFactory


class MainActivity : AppCompatActivity() {


    val getNearMvvm : GetNearPlacesViewModel by lazy {
        val repository     = GetNearPlacesRepository()
        val factory        = GetNearPlacesViewModelFactory(repository)
        ViewModelProvider(this , factory)[GetNearPlacesViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
    }
}