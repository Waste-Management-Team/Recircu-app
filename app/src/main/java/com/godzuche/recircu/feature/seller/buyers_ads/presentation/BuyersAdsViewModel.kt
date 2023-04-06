package com.godzuche.recircu.feature.seller.buyers_ads

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BuyersAdsViewModel @Inject constructor() : ViewModel() {
    private val _isMapView = MutableStateFlow(false)
    val isMapView = _isMapView.asStateFlow()

    private val _state = MutableStateFlow(BuyersAdsState())
    val state = _state.asStateFlow() /*combine(_isMapView, _state) { isMapView, state ->
        Log.d("BuyersAds VM", _state.value.view.name)
        state.copy(
            view = if (isMapView) BuyersAdsView.MAP else BuyersAdsView.LIST
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BuyersAdsState()
    )*/

    fun setViewType(isMapView: Boolean) {
        /*_state.update {
            it.copy(
                view = if (isMapView) BuyersAdsView.MAP else BuyersAdsView.LIST
            )
        }
        Log.d("BuyersAds VM", _state.value.view.name)*/
        /*_isMapView.update {
            isMapView
        }*/
        _state.update {
            it.copy(
                isMapView = isMapView
            )
        }
    }
}

data class BuyersAdsState(
    val isMapView: Boolean = false,
    val view: BuyersAdsView = BuyersAdsView.LIST
)

enum class BuyersAdsView {
    LIST,
    MAP
}