package lk.lnbti.iampresent.ui.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.data.Contact
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.repo.LectureListRepo
import lk.lnbti.iampresent.ui.theme.IAmPresentTheme
import lk.lnbti.iampresent.view_model.LectureListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LectureListScreen(
    onLectureItemClicked: (String) -> Unit,
    onNewLectureClicked: () -> Unit,
    lectureListViewModel: LectureListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val lectureList: List<Lecture> by lectureListViewModel.lectureList.observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.app_name)
                    )
                },
            )
        },
        floatingActionButton = {
            AddNewContactButton()
        },
        bottomBar = {}
    ) { padding ->
        Column(
            modifier
                .padding(padding)
        ) {
            SearchBar(Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_main_content)))
            Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
            OrderByBar(Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_main_content)))
            Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
            LectureListSection(
                lectureList = lectureList,
                onLectureItemClicked = onLectureItemClicked,
                Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_main_content))
            )
        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderByBar(modifier: Modifier = Modifier) {
    val options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }

    ) {
//        TextField(
//            readOnly = true,
//            value = selectedOptionText,
//            onValueChange = { },
//            label = { Text("Label") },
//            trailingIcon = {
//                ExposedDropdownMenuDefaults.TrailingIcon(
//                    expanded = expanded
//                )
//            },
//            colors = ExposedDropdownMenuDefaults.textFieldColors()
//        )
        TextField(
            readOnly = true,
            value = selectedOptionText,
            label = { Text(stringResource(id = R.string.order_by)) },
            onValueChange = {},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = dimensionResource(id = R.dimen.height_search_bar))
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(text = { selectionOption },
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    })

            }
        }
    }
}

@Composable
fun AddNewContactButton() {
    FloatingActionButton(
        shape = MaterialTheme.shapes.large.copy(CornerSize(percent = 50)),
        //contentColor = Color.White,
        onClick = { }
    ) {
        Icon(Icons.Default.Add, contentDescription = null)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LectureListSection(
    lectureList: List<Lecture>,
    onLectureItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_between_list_item)),
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        modifier = modifier
    ) {
        val grouped = lectureList.groupBy { it.startdate }
        grouped.forEach { (initial, lectureList) ->
            stickyHeader {
                LectureGroupHeader(initial.toString())
            }
            items(lectureList) {
//                var showLabel = true
//                if (lectureList.indexOf(it) > 0) {
//                    showLabel = false
//                }
                LectureListItem(
                    item = it,
                    onLectureItemClicked = onLectureItemClicked
                )
            }
        }
    }
}
//if (showLabel) {
//    Text(
//        text = "000",
//        modifier = Modifier
//            .drawBehind {
//                drawCircle(
//                    Color(0xFFBBAAEE),
//                    radius = 50f
//                    //cornerRadius = CornerRadius(10.dp.toPx())
//                )
//            }
//    )
//}

@Composable
fun LectureGroupHeader(header: String) {
    Text(
        text = header,
        modifier = Modifier
            .fillMaxWidth()
    )
    Text(
        text = header,
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    Color(0xFFBBAAEE),
                    cornerRadius = CornerRadius(10.dp.toPx())
                )
            }
    )
}

@Composable
fun LectureListItem(
    item: Lecture,
    onLectureItemClicked: (String) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            //.padding(15.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color.LightGray)
            .padding(horizontal = 15.dp, vertical = 20.dp)
            .fillMaxWidth()
            .clickable { }
    ) {
        Column {
            item.subjectid.subjectname?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Text(
                text = item.topic.toString(),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = item.batchcode.batchcode,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${item.startdate.toString()} ${item.starttime.toString()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LectureListScreenPreview() {
    IAmPresentTheme {
       // LectureListScreen({}, {})

    }
}