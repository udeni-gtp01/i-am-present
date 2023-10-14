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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.data.Result
import lk.lnbti.iampresent.ui.theme.CommonColorScheme
import lk.lnbti.iampresent.ui.theme.IAmPresentTheme
import lk.lnbti.iampresent.ui.view.AddNewLectureButton
import lk.lnbti.iampresent.ui.view.BottomNavigation
import lk.lnbti.iampresent.ui.view.ListGroupHeader
import lk.lnbti.iampresent.ui.view.ListItemContent
import lk.lnbti.iampresent.ui.view.TopAppBar
import lk.lnbti.iampresent.ui.view.filterSection
import lk.lnbti.iampresent.view_model.TodaysLectureListViewModel


@Composable
fun TodaysLectureListScreen(
    onLectureItemClicked: (String) -> Unit,
    onNewLectureClicked: () -> Unit,
    todayLectureListViewModel: TodaysLectureListViewModel = hiltViewModel(),
    onTodayNavButtonClicked: () -> Unit,
    onAllNavButtonClicked: () -> Unit,
    onReportsNavButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val lectureList: List<Lecture> by todayLectureListViewModel.lectureList.observeAsState(emptyList())
    val lectureListResult by todayLectureListViewModel.lectureListResult.observeAsState(Result.Loading)
    todayLectureListViewModel.findTodaysLectureList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = R.string.today,
                description = R.string.today_description,
                modifier = modifier
            )
        },
        floatingActionButton = {
            AddNewLectureButton(onNewLectureClicked = onNewLectureClicked)
        },
        bottomBar = {
            BottomNavigation(
                onTodayNavButtonClicked = onTodayNavButtonClicked,
                onAllNavButtonClicked = onAllNavButtonClicked,
                onReportsNavButtonClicked = onReportsNavButtonClicked
            )
        }
    ) { padding ->
        Column(
            modifier
                .background(CommonColorScheme.dark_blue)
                .padding(padding)
        ) {
            when (lectureListResult) {
                is Result.Loading -> {
                    // Handle loading state
                    LoadingScreen()
                }

                is Result.Success<*> -> {
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
                        onRetry = { todayLectureListViewModel.findTodaysLectureList() })
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

@Composable
private fun LectureListContent(
    lectureList: List<Lecture>,
    onLectureItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val criteriaList = listOf(
        R.string.filter_by_start_time,
        R.string.filter_by_lecture_status,
        R.string.filter_by_location,
        R.string.filter_by_lecturer,
        R.string.filter_by_batch,
        R.string.filter_by_subject,
    )
    var selectedItem by rememberSaveable { mutableIntStateOf(criteriaList.first()) }
    Column(modifier = modifier.background(CommonColorScheme.dark_blue)) {
        filterSection(criteriaList = criteriaList)
        LectureListSection(
            lectureList = lectureList,
            selectedFilter = selectedItem,
            onLectureItemClicked = onLectureItemClicked,
            modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_main_content))
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LectureListSection(
    lectureList: List<Lecture>,
    selectedFilter: Int,
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
        var grouped: Map<String, List<Lecture>>? = null
        when (selectedFilter) {
            R.string.filter_by_start_time -> {
                val sortedLectureList = lectureList.sortedBy { it.startTime }
                grouped = sortedLectureList.groupBy { it.startTime }
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
                ListGroupHeader(initial.toString())
            }
            items(lectureList) {
                ListItemContent {
                    LectureListItem(
                        item = it,
                        onLectureItemClicked = onLectureItemClicked
                    )
                }
            }
        }
    }
}

@Composable
fun LectureListItem(
    item: Lecture,
    onLectureItemClicked: (String) -> Unit,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                //.padding(15.dp)
                .clip(RoundedCornerShape(10.dp))
//            .border(
//                width = 2.dp,
//                brush = Brush.verticalGradient(
//                    colors = listOf(
//                        DefaultColorScheme.primary, // Top color
//                        DefaultColorScheme.secondary // Bottom color
//                    )
//                ),
//                shape = RoundedCornerShape(10.dp),
//            )
                //.border(color = DefaultColorScheme.primary, width = 2.dp)
                .padding(horizontal = 7.dp, vertical = 10.dp)
                .fillMaxWidth()
                .clickable { onLectureItemClicked(item.lectureId.toString()) }
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = item.lectureStatus.statusName,
                    style = MaterialTheme.typography.bodyLarge,
                    // color = DefaultColorScheme.primary
                )
                Text(
                    text = item.subject,
                    style = MaterialTheme.typography.bodyLarge,
                    // color = DefaultColorScheme.primary
                )
                Text(
                    text = item.batch,
                    style = MaterialTheme.typography.titleLarge,
                    //  color = DefaultColorScheme.primary
                )


                Text(
                    text = "${item.startTime.toString()} - ${item.endTime.toString()}",
                    style = MaterialTheme.typography.bodyMedium,
                    // color = DefaultColorScheme.primary
                )
                Text(
                    text = item.location,
                    style = MaterialTheme.typography.bodyMedium,
                    //  color = DefaultColorScheme.primary
                )
                Text(
                    text = item.lecturer.name,
                    style = MaterialTheme.typography.bodyMedium,
                    // color = DefaultColorScheme.primary
                )
            }
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

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTopAppBar() {
    Scaffold(
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