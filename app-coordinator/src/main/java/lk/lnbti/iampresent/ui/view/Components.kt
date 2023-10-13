package lk.lnbti.iampresent.ui.view

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(@StringRes title: Int, @StringRes description: Int) {
    TopAppBar(
        title = {
            Column(Modifier.fillMaxWidth()) {
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
        modifier = Modifier.padding(vertical = 20.dp)
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
@Preview
@Composable
fun PreviewTopAppBar(){
    Scaffold(
//        topBar = {
//            TopAppBar(title = R.string.all , description = R.string.all_description)
//        },
        bottomBar = { BottomNavigation(
            onTodayNavButtonClicked = {},
            onAllNavButtonClicked = {},
            onReportsNavButtonClicked = {}
        ) }
    ) {
    Column(Modifier.padding(it)) {

    }
    }
}
