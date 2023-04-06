package com.godzuche.recircu.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PeopleOutline
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
    val Explore = Icons.Filled.Explore
    val ExploreOutline = Icons.Outlined.Explore
    val Notifications = R.drawable.notification_icon
    val Location = R.drawable.location
    val MyLocation = Icons.Filled.MyLocation
    val Clear = Icons.Filled.Clear
    val ExpandMore = Icons.Filled.ExpandMore
    val ExpandLess = Icons.Filled.ExpandLess
    val People = Icons.Filled.People
    val PeopleOutline = Icons.Outlined.PeopleOutline
    val Avatar12 = R.drawable.avatar_12
    val Edit = Icons.Filled.Edit
    val Google24 = R.drawable.icons8_google_24
}

sealed interface RecircuIcon {
    data class ImageVectorIcon(val imageVector: ImageVector) : RecircuIcon
    data class PainterResourceIcon(@DrawableRes val id: Int) : RecircuIcon
}