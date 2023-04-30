package com.godzuche.recircu.feature.seller.buyers_ads.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuyersAdsViewModel @Inject constructor() : ViewModel() {
    /*   private val _isMapView = MutableStateFlow(false)
       val isMapView = _isMapView.asStateFlow()*/

    private val _state = MutableStateFlow(BuyersAdsState())
    val state = _state.asStateFlow()
    private var searchJob: Job? = null

    fun setViewType(isMapView: Boolean) {
        val adsViewMode = if (isMapView) BuyersAdsView.MAP else BuyersAdsView.LIST
        _state.update {
            it.copy(
                isMapView = isMapView,
                adsViewMode = adsViewMode
            )
        }
    }

    fun onSearchQueryChange(queryText: String) {
        _state.update {
            it.copy(searchQuery = queryText)
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            // Perform search on query change
        }
    }

    fun search(queryText: String) {
        _state.update {
            it.copy(searchQuery = queryText)
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            // Perform search
        }
    }

    fun clearSearchQueryText() {
        _state.update {
            it.copy(searchQuery = "")
        }
    }
}

data class BuyersAdsState(
    val isMapView: Boolean = false,
    val searchQuery: String = "",
    val adsViewMode: BuyersAdsView = BuyersAdsView.LIST
)

enum class BuyersAdsView {
    LIST,
    MAP
}