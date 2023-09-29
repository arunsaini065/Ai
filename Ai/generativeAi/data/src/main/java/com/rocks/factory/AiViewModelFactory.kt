package com.rocks.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rocks.usecase.ModelUseCase
import com.rocks.viewmodel.AiViewModel

@Suppress("UNCHECKED_CAST")
class AiViewModelFactory(private val useCase: ModelUseCase):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  AiViewModel(useCase) as T
    }
}