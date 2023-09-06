package com.godzuche.recircu.feature.seller.seller_profile.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SellerProfileViewModel @Inject constructor() : ViewModel() {

    /*    private val _userState: MutableStateFlow<UserState> = MutableStateFlow(UserState.Loading)
        val userState = _userState.asStateFlow()

        fun onGetCurrentUser(userData: UserData?) {
            _userState.update {
                UserState.SignedIn(
                    User(
                        name = userData?.displayName ?: "No name",
                        photo = userData?.profilePictureUrl,
                        email = userData?.email,
                        location = ""
                    )
                )
            }
        }*/
}