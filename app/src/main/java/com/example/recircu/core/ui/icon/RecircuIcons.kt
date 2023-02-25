package com.example.recircu.core.ui.icon

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.recircu.R

object RecircuIcons {
    val Plastic = R.drawable.plastic
    val Metal = R.drawable.metal
    val Home = Icons.Filled.Home
    val HomeOutline = Icons.Outlined.Home
    val Person = Icons.Filled.Person
    val PersonOutline = Icons.Outlined.PersonOutline
    val Explore = Icons.Filled.AddBox
    val ExploreOutline = Icons.Outlined.AddBox
}

sealed interface RecircuIcon {
    data class ImageVectorIcon(val imageVector: ImageVector) : RecircuIcon
    data class PainterResourceIcon(@DrawableRes val id: Int) : RecircuIcon
}