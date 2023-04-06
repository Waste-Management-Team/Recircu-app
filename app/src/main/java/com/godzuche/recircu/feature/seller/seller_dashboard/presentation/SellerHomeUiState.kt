package com.godzuche.recircu.feature.seller.seller_dashboard.presentation

data class SellerHomeUiState(
    val isLoading: Boolean = false,
    val accountBalance: Int = 0,
    val wasteTypes: MutableSet<WasteType> = mutableSetOf(),
    val filter: List<WasteType> = emptyList(),
    val buyerAds: List<BuyerAd> = emptyList()
)