package lk.lnbti.iampresent.ui.view

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.data.Attendance
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.ui.theme.DefaultColorScheme
import lk.lnbti.iampresent.view_model.AttendanceListViewModel


typealias OnLectureItemClicked = (String) -> Unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceListScreen(
    onLectureItemClicked: OnLectureItemClicked,
    onNewLectureClicked: () -> Unit,
    attendanceListViewModel: AttendanceListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val attendanceList: List<Attendance> by attendanceListViewModel.lectureList.observeAsState(
        emptyList()
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = R.string.my_attendance,
                description = R.string.all_attended_description,
                modifier = modifier
            )
        },
        floatingActionButton = {
            AddNewLectureButton(onNewLectureClicked = onNewLectureClicked)
        },
        bottomBar = { BottomNavigation() }
    ) { padding ->
        Column(
            modifier
                .padding(padding)
        ) {
            LectureListContent(
                lectureList = attendanceList,
                onLectureItemClicked = onLectureItemClicked,
                modifier = modifier
            )
            //SearchBar(Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_main_content)))
            //Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
            Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
        }
    }
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
private fun AddNewLectureButton1(onNewLectureClicked: () -> Unit) {
    FloatingActionButton(
        shape = MaterialTheme.shapes.large.copy(CornerSize(percent = 50)),
        onClick = onNewLectureClicked,
        contentColor = DefaultColorScheme.accent,
        containerColor = DefaultColorScheme.secondary,
        //modifier = Modifier.background(DefaultColorScheme.secondary)
    ) {
        val painter: Painter = painterResource(id = R.drawable.baseline_qr_code_scanner_24)

        Icon(painter, contentDescription = null, tint = DefaultColorScheme.accent)
    }
}

@Composable
fun LectureListContent(
    lectureList: List<Attendance>,
    onLectureItemClicked: OnLectureItemClicked,
    modifier: Modifier = Modifier
) {
    val criteriaList = listOf(
        R.string.filter_by_date,
        R.string.filter_by_lecture_status,
        R.string.filter_by_batch,
        R.string.filter_by_subject,
        R.string.filter_by_lecturer,
        R.string.filter_by_location
    )
    var selectedItem by rememberSaveable { mutableStateOf(criteriaList.first()) }
    Column {
        filterSection(criteriaList = criteriaList)
        LectureListSection(
            lectureList = lectureList,
            selectedFilter = selectedItem,
            onLectureItemClicked = onLectureItemClicked,
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_main_content))
        )
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LectureListSection(
    lectureList: List<Attendance>,
    selectedFilter: Int,
    onLectureItemClicked: OnLectureItemClicked,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_between_list_item)),
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        modifier = modifier
    ) {
        var grouped: Map<String, List<Attendance>>? = null
        when (selectedFilter) {
            R.string.filter_by_date -> {
                grouped = lectureList.groupBy { it.lecture.subject }
            }
        }

        grouped?.forEach { (initial, lectureList) ->
            stickyHeader {
                ListGroupHeader(initial.toString())
            }
            items(lectureList) {
                ListItemContent(
                    content = {
                        LectureListItem(
                            item = it,
                            onLectureItemClicked = onLectureItemClicked
                        )
                    }
                )

            }
        }
    }
}

@Composable
fun LectureGroupHeader(header: String) {
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
    item: Attendance,
    onLectureItemClicked: OnLectureItemClicked,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            //.padding(15.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 2.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DefaultColorScheme.primary, // Top color
                        DefaultColorScheme.secondary // Bottom color
                    )
                ),
                shape = RoundedCornerShape(10.dp),
            )
            .border(color = DefaultColorScheme.primary, width = 2.dp)
            .padding(horizontal = 7.dp, vertical = 10.dp)
            .fillMaxWidth()
            .clickable {}
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = item.lecture.batch,
                style = MaterialTheme.typography.titleLarge,
                color = DefaultColorScheme.primary
            )
            item.lecture.subject?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    color = DefaultColorScheme.primary
                )
            }
            Text(
                text = item.lecture.location,
                style = MaterialTheme.typography.bodyMedium,
                color = DefaultColorScheme.primary
            )
        }
    }
}

@Composable
public fun BottomNavigation(modifier: Modifier = Modifier) {
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
            onClick = {}
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
            onClick = {}
        )
    }
}


//@Preview
@Composable
fun PreviewBottomNavigation() {
    BottomNavigation()
}
