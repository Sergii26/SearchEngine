package com.practice.searchengine.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchViewModelFactory(private val searchViewModel: ViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel: ViewModel = if (modelClass == SearchViewModel::class.java) {
            searchViewModel
        } else {
            throw RuntimeException("unsupported view model class: $modelClass")
        }
        return viewModel as T
    }
}
