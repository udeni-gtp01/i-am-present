package lk.lnbti.iampresent.ui.view

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.ui.theme.CommonColorScheme
import lk.lnbti.iampresent.ui.theme.DefaultColorScheme
import lk.lnbti.iampresent.ui.theme.IAmPresentTheme
import lk.lnbti.iampresent.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(@StringRes title: Int, @StringRes description: Int, modifier: Modifier = Modifier) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
//            titleContentColor = CommonColorScheme.white
        ),
        title = {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                   // .background(CommonColorScheme.dark_blue)
            ) {
                Text(
                    stringResource(id = title),
                    style = Typography.titleLarge
                )
                Text(
                    stringResource(id = description),
                    style = Typography.titleSmall
                )
            }
        },
        modifier = modifier
            .padding(top = 20.dp, bottom = 10.dp)
    )
}
@Composable
fun filterSection(
    criteriaList: List<Int> = listOf<Int>(),
    modifier: Modifier = Modifier
) {
    var selectedItem by rememberSaveable { mutableIntStateOf(criteriaList.first()) }
    Column {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_between_list_item)),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = modifier
                //.background(CommonColorScheme.dark_blue)
                .padding(vertical = 16.dp)
        ) {
            items(items = criteriaList) {
                val isSelected = selectedItem == it
                FilterItem(
                    criteria = it,
                    isSelected = isSelected,
                    onClick = { selectedItem = it },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun FilterItem(
    @StringRes criteria: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = criteria),
        style = MaterialTheme.typography.bodyMedium,
        modifier = if (isSelected) {
            Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            CommonColorScheme.shade_yellow,
                            CommonColorScheme.main_orange
                        )
                    ),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner))
                )

                .clickable { onClick() }
                .padding(10.dp)
                .defaultMinSize(minWidth = 50.dp)
        } else {
            Modifier
                .background(
                    color = Color.Transparent,
//                    shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner))
                )
                .clickable { onClick() }
                .padding(10.dp)
//                .defaultMinSize(minWidth = 50.dp)
        },
        textAlign = TextAlign.Center,
        fontWeight = if (isSelected) {
            FontWeight.Bold
        } else {
            FontWeight.Normal
        }
    )
}

@Composable
fun ListGroupHeader(header: String) {
    Text(
        text = header,
        modifier = Modifier
            .fillMaxWidth()
//            .background(
//                brush = Brush.verticalGradient(
//                    colors = listOf(
//                        CommonColorScheme.gray,
//                        CommonColorScheme.dark_blue
//
//                    ),
//                ),
//                shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner))
//            )
            .padding(10.dp),
//        color = CommonColorScheme.nav_blue,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun ListItemContent(
    content: @Composable() (ColumnScope.() -> Unit)
) {
    ElevatedCard(
        content = content,
//        elevation=CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors=CardDefaults.cardColors(containerColor =CommonColorScheme.transparent_white ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner)),
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun AddNewLectureButton(onNewLectureClicked: () -> Unit) {
    FloatingActionButton(
        shape = MaterialTheme.shapes.large.copy(CornerSize(percent = 50)),
        onClick = onNewLectureClicked,
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = Color.Transparent,
    ) {
        Box(
            modifier = Modifier
                .size(56.dp, 56.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            CommonColorScheme.nav_light_blue,
                            CommonColorScheme.nav_blue
                        )
                    )
                ),
            contentAlignment = Alignment.Center

        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = DefaultColorScheme.accent)

    }
    }
}
@Composable
fun BottomNavigation(
    modifier: Modifier = Modifier,
    onTodayNavButtonClicked: () -> Unit,
    onReportsNavButtonClicked: () -> Unit,
    onAllNavButtonClicked: () -> Unit,
) {
    NavigationBar(
        containerColor = CommonColorScheme.nav_blue,
        // .padding(16.dp)
//        modifier = Modifier
//            .background(brush = Brush.verticalGradient(
//                colors = listOf(
//                    MaterialTheme.colorScheme.primaryContainer,
//                    CommonColorScheme.dark_blue,
//                )
//            )
//            ),
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
            selected = true,
            onClick = onTodayNavButtonClicked,
//                modifier = modifier.background(color = CommonColorScheme.gray, RoundedCornerShape(10.dp))
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
            selected = false,
            onClick = onReportsNavButtonClicked,
            colors = NavigationBarItemDefaults
                .colors(
                    selectedIconColor = CommonColorScheme.shade_yellow,
                    selectedTextColor = CommonColorScheme.shade_yellow,
                    unselectedTextColor = Color.White,
                    indicatorColor = CommonColorScheme.nav_light_blue,
                    unselectedIconColor = Color.White,

                    )
//                modifier = modifier.background(color = CommonColorScheme.gray, RoundedCornerShape(10.dp))
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
            selected = false,
            onClick = onAllNavButtonClicked
        )

    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    TextField(
        value = "",
        onValueChange = {},
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface
        ),
        placeholder = {
            Text(stringResource(R.string.placeholder_search))
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = dimensionResource(id = R.dimen.height_search_bar))
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTopAppBar() {
    IAmPresentTheme {
        Scaffold(
            modifier = Modifier.background(CommonColorScheme.dark_blue),
            topBar = {
                TopAppBar(
                    title = R.string.all,
                    description = R.string.all_description
                )
            },
            bottomBar = {
                BottomNavigation(
                    onTodayNavButtonClicked = {},
                    onAllNavButtonClicked = {},
                    onReportsNavButtonClicked = {}
                )
            }
        ) {
            Column(Modifier.padding(it)) {

            }
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewFilterSection() {
    val criteriaList = listOf(
        R.string.filter_by_start_time,
        R.string.filter_by_lecture_status,
        R.string.filter_by_location,
        R.string.filter_by_lecturer,
        R.string.filter_by_batch,
        R.string.filter_by_subject,
    )
    IAmPresentTheme {
        filterSection(criteriaList = criteriaList)
    }
}

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewListItemContent() {
    val criteriaList = listOf(
        R.string.filter_by_start_time,
        R.string.filter_by_lecture_status,
        R.string.filter_by_location,
        R.string.filter_by_lecturer,
        R.string.filter_by_batch,
        R.string.filter_by_subject,
    )
    IAmPresentTheme {
        filterSection(criteriaList = criteriaList)
    }
}
