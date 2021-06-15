package com.practice.searchengine.ui.initial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class InitialViewModelFactory(private val initialViewModel: ViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel: ViewModel = if (modelClass == InitialViewModel::class.java) {
            initialViewModel
        } else {
            throw RuntimeException("unsupported view model class: $modelClass")
        }
        return viewModel as T
    }
}
