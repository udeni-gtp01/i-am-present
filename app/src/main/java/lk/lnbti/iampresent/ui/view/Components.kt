package lk.lnbti.iampresent.ui.view

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.ui.theme.CommonColorScheme
import lk.lnbti.iampresent.ui.theme.IAmPresentTheme
import lk.lnbti.iampresent.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(@StringRes title: Int, @StringRes description: Int, modifier: Modifier = Modifier) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CommonColorScheme.dark_blue,
            titleContentColor = CommonColorScheme.white
        ),
        title = {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(CommonColorScheme.dark_blue)
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
            .padding(vertical = 20.dp)
    )
}

@Composable
public fun BottomNavigation(
    modifier: Modifier = Modifier,
    onTodayNavButtonClicked: () -> Unit,
    onReportsNavButtonClicked: () -> Unit,
    onAllNavButtonClicked: () -> Unit,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
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
            selected = false,
            onClick = onTodayNavButtonClicked
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
            selected = true,
            onClick = onReportsNavButtonClicked
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
            selected = true,
            onClick = onAllNavButtonClicked
        )
    }
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
                .background(CommonColorScheme.dark_blue)
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
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            CommonColorScheme.shade_blue,
                            CommonColorScheme.shade_green
                        )
                    ),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner))
                )
                .clickable { onClick() }
                .padding(8.dp)
                .defaultMinSize(minWidth = 50.dp)
        } else {
            Modifier
                .background(
                    color = CommonColorScheme.dark_blue,
                    shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner))
                )
                .clickable { onClick() }
                .padding(8.dp)
                .defaultMinSize(minWidth = 50.dp)
        },
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        color = if (isSelected) {
            CommonColorScheme.dark_blue
        } else {
            CommonColorScheme.white
        }
    )
}

@Composable
fun ListGroupHeader(header: String) {
    Text(
        text = header,
        modifier = Modifier
            .fillMaxWidth()
            .background(CommonColorScheme.dark_blue)
            .padding(15.dp),
        color = CommonColorScheme.white,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun ListItemContent(
    content: @Composable() (ColumnScope.() -> Unit)
) {
    Card(
        content = content,
        modifier =
        Modifier
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        CommonColorScheme.shade_orange,
                        CommonColorScheme.shade_yellow
                    )
                ),
                shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner))
            )
//            .padding(8.dp)
//            .defaultMinSize(minWidth = 50.dp)

    )
}


//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTopAppBar() {
    Scaffold(
        modifier = Modifier.background(CommonColorScheme.dark_blue),
        topBar = {
            TopAppBar(title = R.string.all, description = R.string.all_description)
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
