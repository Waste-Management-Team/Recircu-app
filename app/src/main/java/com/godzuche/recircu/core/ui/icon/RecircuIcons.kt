package com.godzuche.recircu.core.ui.icon

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.ui.graphics.vector.ImageVector
import com.godzuche.recircu.R

object RecircuIcons {
    val Plastic = R.drawable.plastic
    val Metal = R.drawable.metal
    val Home = Icons.Filled.Home
    val HomeOutline = Icons.Outlined.Home
    val Person = Icons.Filled.Person
    val PersonOutline = Icons.Outlined.PersonOutline
    val Explore = Icons.Filled.AddBox
    val ExploreOutline = Icons.Outlined.AddBox
    val Notifications = R.drawable.notification_icon
    val Location = R.drawable.location
    val MyLocation = Icons.Filled.MyLocation
    val Clear = Icons.Filled.Clear
}

sealed interface RecircuIcon {
    data class ImageVectorIcon(val imageVector: ImageVector) : RecircuIcon
    data class PainterResourceIcon(@DrawableRes val id: Int) : RecircuIcon
}