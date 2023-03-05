package com.example.recircu.features.seller.seller_dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellerHomeViewModel @Inject constructor() : ViewModel() {
    val user = User(
        firstName = "Onyi",
        lastName = "Jonah",
        location = "Rivers State University"
    )
    val buyerAds = listOf(
        BuyerAd(WasteType.Plastic, 20, 0),
        BuyerAd(WasteType.Metal, 10, 1),
        BuyerAd(WasteType.Metal, 15, 2),
    )

    private var _userState: MutableStateFlow<UserState> = MutableStateFlow(UserState.Loading)
    val userState: StateFlow<UserState> get() = _userState.asStateFlow()

    var wasteTypes = MutableStateFlow(
        mutableSetOf<WasteType>()
    )

    private val _state = MutableStateFlow(SellerHomeUiState(buyerAds = buyerAds))
    val state = combine(
        wasteTypes,
        _state
    ) { wasteTypes, state ->
        state.copy(wasteTypes = wasteTypes)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = _state.value
    )

    init {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        wasteTypes.update {
            mutableSetOf(
                WasteType.Plastic,
                WasteType.Metal,
                WasteType.Paper,
                WasteType.Glass,
                WasteType.Others
            )
        }
        _state.update {
            it.copy(
                accountBalance = 10_000,
//                wasteTypes = wasteTypes.value,
                buyerAds = buyerAds,
                isLoading = false
            )
        }
        viewModelScope.launch {
            delay(2000)
            _userState.update {
                UserState.Success(
                    user = user
                )
            }
        }
    }

    fun filterBuyersAds(wasteType: WasteType, isSelected: Boolean) {
        wasteTypes.update {
            it.map { type ->
                if (type == wasteType) type.apply { this.isSelected = !isSelected }
                else type
            }.toMutableSet()
        }
        if (!isSelected) {
            viewModelScope.launch {
                val a = _state.value.filter.toMutableList()
                a.add(wasteType)
                _state.update {
                    it.copy(filter = a)
                }
            }
        } else {
            viewModelScope.launch {
                val a = _state.value.filter.toMutableList()
                a.remove(wasteType)
                _state.update {
                    it.copy(filter = a)
                }
            }
        }
    }
}

sealed interface UserState {
    object Loading : UserState
    data class Success(
        val user: User
    ) : UserState
}

data class User(
    val firstName: String,
    val lastName: String,
    val location: String
)

data class BuyerAd(
    val wasteType: WasteType,
    val weight: Int,
    val buyerId: Int
)

data class SellerHomeUiState(
    val isLoading: Boolean = false,
    val accountBalance: Int = 0,
    val wasteTypes: MutableSet<WasteType> = mutableSetOf(),
    val filter: List<WasteType> = emptyList(),
    val buyerAds: List<BuyerAd> = emptyList()
)