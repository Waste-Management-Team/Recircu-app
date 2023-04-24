package com.godzuche.recircu

sealed interface AppUiEvent {
    data class Navigate(val route: String) : AppUiEvent
    object RequestLocationPermission : AppUiEvent
}

interface RecircuDialog {
    val title: String?
    val description: String?
    val dismissText: String?
    val confirmText: String?
}

class GpsDisabledDialog : RecircuDialog {
    override val title: String? = null
    override val description: String = "For a better experience, turn on device location, " +
            "which uses Google's location service."
    override val dismissText: String = "No, thanks"
    override val confirmText: String = "OK"
}

class ConfirmationDialog(
    titleText: String,
    descriptionText: String,
    cancelText: String,
    okText: String,
    val action: ConfirmActions
) : RecircuDialog {
    override val title: String = titleText
    override val description = descriptionText
    override val dismissText = cancelText
    override val confirmText: String = okText
}

enum class ConfirmActions {
    SIGN_OUT
}

enum class RecircuBottomSheetContent {
    SCHEDULE,
    MAP
}