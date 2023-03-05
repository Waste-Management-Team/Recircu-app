package com.example.recircu.navigation

import androidx.annotation.StringRes
import com.example.recircu.R
import com.example.recircu.core.ui.icon.RecircuIcon
import com.example.recircu.core.ui.icon.RecircuIcons

enum class RecircuTopLevelDestination(
    val selectedIcon: RecircuIcon? = null,
    val unselectedIcon: RecircuIcon? = null,
    @StringRes val iconTextId: Int? = null,
    @StringRes val titleTextId: Int? = null
) {
    SELLER_HOME(
        selectedIcon = RecircuIcon.ImageVectorIcon(RecircuIcons.Home),
        unselectedIcon = RecircuIcon.ImageVectorIcon(RecircuIcons.HomeOutline),
        iconTextId = R.string.home,
        titleTextId = R.string.home
    ),
    EXPLORE(
        selectedIcon = RecircuIcon.ImageVectorIcon(RecircuIcons.Explore),
        unselectedIcon = RecircuIcon.ImageVectorIcon(RecircuIcons.ExploreOutline),
        iconTextId = R.string.explore,
        titleTextId = R.string.explore
    ),
    PROFILE(
        selectedIcon = RecircuIcon.ImageVectorIcon(RecircuIcons.Person),
        unselectedIcon = RecircuIcon.ImageVectorIcon(RecircuIcons.PersonOutline),
        iconTextId = R.string.profile,
        titleTextId = R.string.profile
    ),
    GETTING_STARTED
}