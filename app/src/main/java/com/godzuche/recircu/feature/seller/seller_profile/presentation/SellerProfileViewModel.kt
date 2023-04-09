package com.godzuche.recircu.feature.seller.seller_profile.presentation

import androidx.lifecycle.ViewModel
import com.godzuche.recircu.feature.authentication.presentation.UserData
import com.godzuche.recircu.feature.seller.seller_dashboard.presentation.User
import com.godzuche.recircu.feature.seller.seller_dashboard.presentation.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SellerProfileViewModel @Inject constructor() : ViewModel() {

    private val _userState: MutableStateFlow<UserState> = MutableStateFlow(UserState.Loading)
    val userState = _userState.asStateFlow()

    fun onGetCurrentUser(userData: UserData?) {
        _userState.update {
            UserState.Success(
                User(
                    name = userData?.displayName ?: "No name",
                    photo = userData?.profilePictureUrl,
                    email = userData?.email,
                    location = ""
                )
            )
        }
    }
}