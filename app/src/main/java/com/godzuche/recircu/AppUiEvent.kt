package com.godzuche.recircu

sealed interface AppUiEvent {
    data class ShowBottomSheet(val sheetContent: RecircuBottomSheetContent) : AppUiEvent
    data class ShowDialog(val dialog: RecircuDialog) : AppUiEvent
}

interface RecircuDialog {
    val title: String?
    val text: String?
    val dismissText: String?
    val confirmText: String?
}

class GpsDisabledDialog() : RecircuDialog {
    override val title: String? = null
    override val text: String = "For a better experience, turn on device location, " +
            "which uses Google's location service."
    override val dismissText: String = "No, thanks"
    override val confirmText: String = "OK"
}

enum class RecircuBottomSheetContent {
    SCHEDULE,
    MAP
}