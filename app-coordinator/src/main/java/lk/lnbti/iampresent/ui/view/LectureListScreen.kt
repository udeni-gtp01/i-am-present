package lk.lnbti.iampresent.ui.view

import ErrorScreen
import LoadingScreen
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.data.Result
import lk.lnbti.iampresent.ui.theme.DefaultColorScheme
import lk.lnbti.iampresent.ui.theme.IAmPresentTheme
import lk.lnbti.iampresent.view_model.LectureListViewModel


typealias OnLectureItemClicked = (String) -> Unit

@Composable
fun LectureListScreen(
    onLectureItemClicked: OnLectureItemClicked,
    onNewLectureClicked: () -> Unit,
    lectureListViewModel: LectureListViewModel = hiltViewModel(),
    onTodayNavButtonClicked: () -> Unit,
    onReportsNavButtonClicked: () -> Unit,
    onAllNavButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val lectureList: List<Lecture> by lectureListViewModel.lectureList.observeAsState(emptyList())
    val lectureListResult by lectureListViewModel.lectureListResult.observeAsState(Result.Loading)
    lectureListViewModel.findLectureList()
    Scaffold(
        topBar = {
            TopAppBar(title = R.string.all, description = R.string.all_description)
        },
        floatingActionButton = {
            AddNewLectureButton(onNewLectureClicked = onNewLectureClicked)
        },
        bottomBar = {
            BottomNavigation(
                onTodayNavButtonClicked = onTodayNavButtonClicked,
                onReportsNavButtonClicked = onReportsNavButtonClicked,
                onAllNavButtonClicked = onAllNavButtonClicked
            )
        }
    ) { padding ->
        Column(
            modifier
                .padding(padding)
        ) {
            when (lectureListResult) {
                is Result.Loading -> {
                    // Handle loading state
                    LoadingScreen()
                }

                is Result.Success -> {
                    // Handle success state
                    LectureListContent(
                        lectureList = lectureList,
                        onLectureItemClicked = onLectureItemClicked,
                        modifier = modifier
                    )
                }

                is Result.Error -> {
                    // Handle error state
                    val errorMessage = (lectureListResult as Result.Error).message
                    ErrorScreen(
                        errorMessage = errorMessage,
                        onRetry = { lectureListViewModel.findLectureList() })
                }
            }
            Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
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
fun AddNewLectureButton(onNewLectureClicked: () -> Unit) {
    FloatingActionButton(
        shape = MaterialTheme.shapes.large.copy(CornerSize(percent = 50)),
        onClick = onNewLectureClicked,
        contentColor = DefaultColorScheme.accent,
        containerColor = DefaultColorScheme.secondary,
        //modifier = Modifier.background(DefaultColorScheme.secondary)
    ) {
        Icon(Icons.Default.Add, contentDescription = null, tint = DefaultColorScheme.accent)
    }
}

@Composable
private fun LectureListContent(
    lectureList: List<Lecture>,
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

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_between_list_item)),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = modifier
                .background(DefaultColorScheme.accent)
                .padding(vertical = 16.dp)
        ) {
            items(criteriaList) { item ->
                val isSelected = selectedItem == item
                FilterItem(
                    criteria = item,
                    isSelected = isSelected,
                    onClick = { selectedItem = item },
                    modifier = Modifier.weight(1f)
                )

            }
        }

        LectureListSection(
            lectureList = lectureList,
            selectedFilter = selectedItem,
            onLectureItemClicked = onLectureItemClicked,
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_main_content))
        )
    }
}

@Composable
fun FilterItem(
    @StringRes criteria: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    //val backgroundColor = if (isSelected) Color.Yellow else Color(0xFFBBAAEE)
    val backgroundColor = if (isSelected) DefaultColorScheme.secondary else Color.White

    Text(
        text = stringResource(id = criteria),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
//            .drawBehind {
//                drawRoundRect(
//                    backgroundColor,
//                    cornerRadius = CornerRadius(10.dp.toPx())
//                )
//            }
            .clickable { onClick() }
            .padding(8.dp)
            .defaultMinSize(minWidth = 50.dp),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        color = if (isSelected) DefaultColorScheme.accent else DefaultColorScheme.primary,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LectureListSection(
    lectureList: List<Lecture>,
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
        var grouped: Map<String, List<Lecture>>? = null
        when (selectedFilter) {
            R.string.filter_by_date -> {
                val sortedLectureList = lectureList.sortedByDescending { it.startDate }
                grouped = sortedLectureList.groupBy { it.startDate }
            }

            R.string.filter_by_lecture_status -> {
                val sortedLectureList = lectureList.sortedBy { it.lectureStatus.statusName }
                grouped = sortedLectureList.groupBy { it.lectureStatus.statusName }
            }

            R.string.filter_by_batch -> {
                val sortedLectureList = lectureList.sortedBy { it.batch }
                grouped = sortedLectureList.groupBy { it.batch }
            }

            R.string.filter_by_subject -> {
                val sortedLectureList = lectureList.sortedBy { it.subject }
                grouped = sortedLectureList.groupBy { it.subject }
            }

            R.string.filter_by_lecturer -> {
                val sortedLectureList = lectureList.sortedBy { it.lecturer.name }
                grouped = sortedLectureList.groupBy { it.lecturer.name }
            }

            R.string.filter_by_location -> {
                val sortedLectureList = lectureList.sortedBy { it.location }
                grouped = sortedLectureList.groupBy { it.location }
            }
        }

        grouped?.forEach { (initial, lectureList) ->
            stickyHeader {
                LectureGroupHeader(initial.toString())
            }
            items(lectureList) {
                LectureListItem(
                    item = it,
                    onLectureItemClicked = onLectureItemClicked
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
//            .drawBehind {
//                drawRoundRect(
//                    Color(0xFFBBAAEE),
//                    cornerRadius = CornerRadius(10.dp.toPx())
//                )
//            }
            .background(Color.White)
            .padding(15.dp),

        color = DefaultColorScheme.primary,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun LectureListItem(
    item: Lecture,
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
            .clickable { onLectureItemClicked(item.lectureId.toString()) }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = item.batch,
                style = MaterialTheme.typography.titleLarge,
                color = DefaultColorScheme.primary
            )
            Text(
                text = item.subject,
                style = MaterialTheme.typography.bodyLarge,
                color = DefaultColorScheme.primary
            )

            Text(
                text = "${item.startDate.toString()} @ ${item.startTime.toString()}",
                style = MaterialTheme.typography.bodyMedium,
                color = DefaultColorScheme.primary
            )
            Text(
                text = item.location,
                style = MaterialTheme.typography.bodyMedium,
                color = DefaultColorScheme.primary
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLectureListSection() {

    val jsonString =
        "[{\"lectureid\":1,\"venue\":\"online\",\"startdate\":\"2023-12-12\",\"starttime\":\"08:00:00\",\"enddate\":\"2023-12-12\",\"endtime\":\"12:00:00\",\"semester\":2,\"subject\":\"kotlin\",\"batch\":\"gtp01\",\"organizerid\":{\"userid\":1,\"name\":\"admin\",\"email\":\"admin@lnbti.edu.lk\",\"token\":null,\"roleid\":{\"roleid\":1,\"name\":\"admin\"},\"isuseravailable\":1},\"lecturerid\":{\"userid\":1,\"name\":\"admin\",\"email\":\"admin@lnbti.edu.lk\",\"token\":null,\"roleid\":{\"roleid\":1,\"name\":\"admin\"},\"isuseravailable\":1},\"lecturestatusid\":{\"lecturestatusid\":1,\"statusname\":\"new\"}}]"
// Create a Gson instance
    val gson = Gson()

// Define a TypeToken for the list of Lecture objects
    val lectureListType = object : TypeToken<List<Lecture>>() {}.type

// Parse the JSON string into a list of Lecture objects
    val lectureList: List<Lecture> = gson.fromJson(jsonString, lectureListType)

    IAmPresentTheme {
        LectureListContent(
            lectureList = lectureList,
            onLectureItemClicked = { },
        )
    }
}

//@Preview( showBackground = true)
@Composable
fun PreviewAddNewLectureButton() {
    AddNewLectureButton({ })
}
//dropdown
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