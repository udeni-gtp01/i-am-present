package lk.lnbti.iampresent.ui.view

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.ui.theme.IAmPresentTheme
import lk.lnbti.iampresent.view_model.LectureListViewModel

typealias OnLectureItemClicked = (String) -> Unit
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LectureListScreen(
    onLectureItemClicked: OnLectureItemClicked,
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
            AddNewLectureButton(onNewLectureClicked= onNewLectureClicked)
        },
        bottomBar = { BottomNavigation() }
    ) { padding ->
        Column(
            modifier
                .padding(padding)
        ) {
            LectureListContent(
                lectureList = lectureList,
                onLectureItemClicked = onLectureItemClicked,
                modifier = modifier
            )
            //SearchBar(Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_main_content)))
            //Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
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
fun AddNewLectureButton(onNewLectureClicked: () -> Unit) {
    FloatingActionButton(
        shape = MaterialTheme.shapes.large.copy(CornerSize(percent = 50)),
        //contentColor = Color.White,
        onClick = onNewLectureClicked
    ) {
        Icon(Icons.Default.Add, contentDescription = null)
    }
}

@Composable
fun LectureListContent(
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
        ) {
            items(criteriaList) { item ->
                val isSelected = selectedItem == item
                FilterItem(
                    criteria = item,
                    isSelected = isSelected,
                    onClick = { selectedItem = item })
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
    val backgroundColor = if (isSelected) Color.Yellow else Color(0xFFBBAAEE)

    Text(
        text = stringResource(id = criteria),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .drawBehind {
                drawRoundRect(
                    backgroundColor,
                    cornerRadius = CornerRadius(10.dp.toPx())
                )
            }
            .clickable { onClick() }
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
                grouped = lectureList.groupBy { it.startDate }
            }

            R.string.filter_by_lecture_status -> {
                grouped = lectureList.groupBy { it.lectureStatus.statusName }
            }

            R.string.filter_by_batch -> {
                grouped = lectureList.groupBy { it.batch }
            }

            R.string.filter_by_subject -> {
                grouped = lectureList.groupBy { it.subject }
            }

            R.string.filter_by_lecturer -> {
                grouped = lectureList.groupBy { it.lecturer.name }
            }

            R.string.filter_by_location -> {
                grouped = lectureList.groupBy { it.location }
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
    onLectureItemClicked: OnLectureItemClicked,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            //.padding(15.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = Color.LightGray)
            .padding(horizontal = 15.dp, vertical = 20.dp)
            .fillMaxWidth()
            .clickable { onLectureItemClicked(item.lectureId.toString())}
    ) {
        Column {
            item.subject?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Text(
                text = item.batch,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${item.startDate.toString()} ${item.startTime.toString()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
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

//@Preview(showBackground = true, showSystemUi = true)
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

//@Preview
@Composable
fun PreviewBottomNavigation() {
    BottomNavigation()
}
@Preview( showBackground = true)
@Composable
fun PreviewAddNewLectureButton(){
    AddNewLectureButton({ })
}