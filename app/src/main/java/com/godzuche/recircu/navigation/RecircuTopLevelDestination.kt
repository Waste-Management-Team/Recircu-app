package com.godzuche.recircu.navigation

import androidx.annotation.StringRes
import com.godzuche.recircu.R
import com.godzuche.recircu.core.designsystem.icon.RecircuIcon
import com.godzuche.recircu.core.designsystem.icon.RecircuIcons

enum class RecircuTopLevelDestination(
    val selectedIcon: RecircuIcon? = null,
    val unselectedIcon: RecircuIcon? = null,
    @StringRes val iconTextId: Int? = null,
    @StringRes val titleTextId: Int? = null
) {
    SELLER_HOME(
        selectedIcon = RecircuIcon.ImageVectorIcon(imageVector = RecircuIcons.Home),
        unselectedIcon = RecircuIcon.ImageVectorIcon(imageVector = RecircuIcons.HomeOutline),
        iconTextId = R.string.home,
        titleTextId = R.string.home
    ),
    EXPLORE(
        selectedIcon = RecircuIcon.ImageVectorIcon(imageVector = RecircuIcons.Explore),
        unselectedIcon = RecircuIcon.ImageVectorIcon(imageVector = RecircuIcons.ExploreOutline),
        iconTextId = R.string.explore,
        titleTextId = R.string.explore
    ),
    CONNECT(
        selectedIcon = RecircuIcon.ImageVectorIcon(imageVector = RecircuIcons.People),
        unselectedIcon = RecircuIcon.ImageVectorIcon(imageVector = RecircuIcons.PeopleOutline),
        iconTextId = R.string.connect,
        titleTextId = R.string.connect
    ),
    PROFILE(
        selectedIcon = RecircuIcon.ImageVectorIcon(imageVector = RecircuIcons.Person),
        unselectedIcon = RecircuIcon.ImageVectorIcon(imageVector = RecircuIcons.PersonOutline),
        iconTextId = R.string.profile,
        titleTextId = R.string.profile
    ),
    USER_SELECTION,
    GETTING_STARTED
}