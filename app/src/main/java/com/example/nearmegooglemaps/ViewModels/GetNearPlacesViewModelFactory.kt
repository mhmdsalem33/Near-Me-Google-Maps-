package com.example.nearmegooglemaps.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nearmegooglemaps.Repository.GetNearPlacesRepository

class GetNearPlacesViewModelFactory(
      private val nearPlacesRepository: GetNearPlacesRepository
) :ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GetNearPlacesViewModel(nearPlacesRepository) as T
    }
}