package com.godzuche.recircu

sealed interface AppUiEvent{
    data class ShowBottomSheet(val sheetContent: RecircuBottomSheetContent): AppUiEvent
}

enum class RecircuBottomSheetContent{
    SCHEDULE,
    MAP
}