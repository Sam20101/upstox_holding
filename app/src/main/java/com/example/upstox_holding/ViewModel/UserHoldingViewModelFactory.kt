package com.example.upstox_holding.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.upstox_holding.Repository.UserHoldingRepository
import java.lang.IllegalArgumentException

class UserHoldingViewModelFactory(private val repository: UserHoldingRepository) :ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserHoldingViewModel::class.java))
            return UserHoldingViewModel(repository)as T
        else throw IllegalArgumentException("Illeagal viewmodel")
    }
}