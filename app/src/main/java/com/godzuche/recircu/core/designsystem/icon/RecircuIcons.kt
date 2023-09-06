package com.godzuche.recircu.core.designsystem.icon

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
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
    val Map = Icons.Filled.Map
    val List = Icons.Filled.List
}

sealed interface RecircuIcon {
    data class ImageVectorIcon(val imageVector: ImageVector) : RecircuIcon
    data class PainterResourceIcon(@DrawableRes val id: Int) : RecircuIcon
}