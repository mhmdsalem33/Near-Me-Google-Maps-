package com.example.nearmegooglemaps.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nearme.Models.test.PlacesModelList
import com.example.nearmegooglemaps.Repository.GetNearPlacesRepository
import kotlinx.coroutines.launch

class GetNearPlacesViewModel(
    private val nearPlacesRepository: GetNearPlacesRepository
) :ViewModel() {

    private val _getNearPlaces = MutableLiveData<PlacesModelList>()
    val getNearPlaces :LiveData<PlacesModelList> = _getNearPlaces

    fun getNearPlaces(url :String) = viewModelScope.launch {
       val response  =  nearPlacesRepository.getNearPlaces(url)
           response.body()?.let {
               _getNearPlaces.postValue(it)
           }
    }
}