package com.godzuche.recircu.feature.seller.seller_dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godzuche.recircu.UserAuthState
import com.godzuche.recircu.core.common.RecircuResult
import com.godzuche.recircu.core.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SellerHomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val sampleUser = User(
        name = "God'swill",
        photo = null,
        email = null,
        location = "Rivers State University"
    )
    val buyerAds = listOf(
        BuyerAd(WasteType.Plastic, 20, 0),
        BuyerAd(WasteType.Metal, 10, 1),
        BuyerAd(WasteType.Metal, 15, 2),
    )

    private var _userState: MutableStateFlow<UserAuthState> =
        MutableStateFlow(UserAuthState.Loading)
    val userState: StateFlow<UserAuthState> get() = _userState.asStateFlow()

    private val wasteTypes = MutableStateFlow(
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
    }

    fun getCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.getSignedInUser().onEach { result ->
                when (result) {
                    is RecircuResult.Loading -> {
                        _userState.update {
                            UserAuthState.Loading
                        }
                    }

                    is RecircuResult.Success -> {
                        result.data?.let {
                            _userState.update {
                                UserAuthState.SignedIn(userData = result.data)
                            }
                        } ?: _userState.update {
                            UserAuthState.NotSignedIn
                        }
                    }

                    else -> Unit
                }
            }.launchIn(viewModelScope)
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
                val filter = _state.value.filter.toMutableList()
                filter.add(wasteType)
                _state.update {
                    it.copy(filter = filter)
                }
            }
        } else {
            viewModelScope.launch {
                val filter = _state.value.filter.toMutableList()
                filter.remove(wasteType)
                _state.update {
                    it.copy(filter = filter)
                }
            }
        }
    }
}

data class User(
    val name: String,
    val email: String?,
    val photo: String?,
    val location: String
)

data class BuyerAd(
    val wasteType: WasteType,
    val weight: Int,
    val buyerId: Int
)