package lk.lnbti.iampresent.ui.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.ui.theme.CommonColorScheme

@Composable
fun CoordinatorBottomNavigation(
    onTodayNavButtonClicked: () -> Unit,
    onReportsNavButtonClicked: () -> Unit,
    onAllNavButtonClicked: () -> Unit,
    isTodayNavItemSelected:Boolean,
    isReportsNavItemSelected:Boolean,
    isAllNavItemSelected:Boolean,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        containerColor = CommonColorScheme.nav_blue,
        contentColor = Color.White
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.today))
            },
            colors = NavigationBarItemDefaults
                .colors(
                    selectedIconColor = CommonColorScheme.shade_yellow,
                    selectedTextColor = CommonColorScheme.shade_yellow,
                    unselectedTextColor = Color.White,
                    indicatorColor = CommonColorScheme.nav_light_blue,
                    unselectedIconColor = Color.White,

                    ),
            selected = isTodayNavItemSelected,
            onClick = onTodayNavButtonClicked,
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.reports))
            },
            selected = isReportsNavItemSelected,
            onClick = onReportsNavButtonClicked,
            colors = NavigationBarItemDefaults
                .colors(
                    selectedIconColor = CommonColorScheme.shade_yellow,
                    selectedTextColor = CommonColorScheme.shade_yellow,
                    unselectedTextColor = Color.White,
                    indicatorColor = CommonColorScheme.nav_light_blue,
                    unselectedIconColor = Color.White,

                    )
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.all))
            },
            colors = NavigationBarItemDefaults
                .colors(
                    selectedIconColor = CommonColorScheme.shade_yellow,
                    selectedTextColor = CommonColorScheme.shade_yellow,
                    unselectedTextColor = Color.White,
                    indicatorColor = CommonColorScheme.nav_light_blue,
                    unselectedIconColor = Color.White,
                ),
            selected = isAllNavItemSelected,
            onClick = onAllNavButtonClicked
        )
    }
}