package lk.lnbti.iampresent.ui.view

import ErrorScreen
import LoadingScreen
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
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
            modifier = modifier
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
    val selectedItem by rememberSaveable { mutableIntStateOf(criteriaList.first()) }
    Column(
        //modifier = modifier.background(CommonColorScheme.dark_blue)
    ) {
        filterSection(criteriaList = criteriaList)
        Column(
            modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            CommonColorScheme.main_orange,
                            CommonColorScheme.main_blue
                        )
                    )
                )
                .fillMaxSize()
        ) {


            TodaysLectureListSection(
                lectureList = lectureList,
                selectedFilter = selectedItem,
                onLectureItemClicked = onLectureItemClicked,
                modifier = modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_main_content))
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TodaysLectureListSection(
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
                ListGroupHeader(initial)
            }
            items(lectureList) {
                ListItemContent(
                    content = {
                        LectureListItemToday(
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
fun LectureListItemToday(
    item: Lecture,
    onLectureItemClicked: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .clickable { onLectureItemClicked(item.lectureId.toString()) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.batch,
                    style = MaterialTheme.typography.titleLarge,
                    //  color = DefaultColorScheme.primary
                )
                if(item.lectureStatus.lectureStatusId==2){
                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = CommonColorScheme.status_ongoing),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner)),
                    ) {
                        Text(
                            text = "${stringResource(id = R.string.ongoing)}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier=Modifier.padding(7.dp)
                        )
                    }
                }else if(item.lectureStatus.lectureStatusId==3){
                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = CommonColorScheme.status_complete),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner)),
                        modifier = Modifier
//                        .padding(7.dp)
//                        .fillMaxWidth()
                    ) {
                        Text(
                            text = "${stringResource(id = R.string.complete)}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier=Modifier.padding(7.dp)
                            // color = DefaultColorScheme.primary
                        )
                    }
                }
            }


            Text(
                text = "${stringResource(R.string.subject)}: ${item.subject}",
                style = MaterialTheme.typography.bodyLarge,
//                lineHeight =20.sp
                // color = DefaultColorScheme.primary
            )
            Text(
                text = "${stringResource(id = R.string.lecturer)}: ${item.lecturer.name}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)
                // color = DefaultColorScheme.primary
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row{
                    val timePainter: Painter = painterResource(id = R.drawable.time)
                    Icon(
                        painter = timePainter,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "${item.startTime} - ${item.endTime}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
                Row{
                    val locationPainter: Painter = painterResource(id = R.drawable.location)
                    Icon(
                        painter = locationPainter,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "${item.location}",
                        style = MaterialTheme.typography.bodyMedium,
                        //  color = DefaultColorScheme.primary
                    )
                }
            }



        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTodaysLectureListSection() {

    val jsonString =
        "[{\"lectureid\":1,\"venue\":\"online\",\"startdate\":\"2023-12-12\",\"starttime\":\"08:00:00\",\"enddate\":\"2023-12-12\",\"endtime\":\"12:00:00\",\"semester\":2,\"subject\":\"kotlin\",\"batch\":\"gtp01\",\"organizerid\":{\"userid\":1,\"name\":\"admin\",\"email\":\"admin@lnbti.edu.lk\",\"token\":null,\"roleid\":{\"roleid\":1,\"name\":\"admin\"},\"isuseravailable\":1},\"lecturerid\":{\"userid\":1,\"name\":\"admin\",\"email\":\"admin@lnbti.edu.lk\",\"token\":null,\"roleid\":{\"roleid\":1,\"name\":\"admin\"},\"isuseravailable\":1},\"lecturestatusid\":{\"lecturestatusid\":2,\"statusname\":\"new\"}},{\"lectureid\":1,\"venue\":\"online\",\"startdate\":\"2023-12-12\",\"starttime\":\"08:00:00\",\"enddate\":\"2023-12-12\",\"endtime\":\"12:00:00\",\"semester\":2,\"subject\":\"kotlin\",\"batch\":\"gtp01\",\"organizerid\":{\"userid\":1,\"name\":\"admin\",\"email\":\"admin@lnbti.edu.lk\",\"token\":null,\"roleid\":{\"roleid\":1,\"name\":\"admin\"},\"isuseravailable\":1},\"lecturerid\":{\"userid\":1,\"name\":\"admin\",\"email\":\"admin@lnbti.edu.lk\",\"token\":null,\"roleid\":{\"roleid\":1,\"name\":\"admin\"},\"isuseravailable\":1},\"lecturestatusid\":{\"lecturestatusid\":3,\"statusname\":\"new\"}},{\"lectureid\":1,\"venue\":\"online\",\"startdate\":\"2023-12-12\",\"starttime\":\"08:00:00\",\"enddate\":\"2023-12-12\",\"endtime\":\"12:00:00\",\"semester\":2,\"subject\":\"kotlin\",\"batch\":\"gtp01\",\"organizerid\":{\"userid\":1,\"name\":\"admin\",\"email\":\"admin@lnbti.edu.lk\",\"token\":null,\"roleid\":{\"roleid\":1,\"name\":\"admin\"},\"isuseravailable\":1},\"lecturerid\":{\"userid\":1,\"name\":\"admin\",\"email\":\"admin@lnbti.edu.lk\",\"token\":null,\"roleid\":{\"roleid\":1,\"name\":\"admin\"},\"isuseravailable\":1},\"lecturestatusid\":{\"lecturestatusid\":1,\"statusname\":\"new\"}},{\"lectureid\":1,\"venue\":\"online\",\"startdate\":\"2023-12-12\",\"starttime\":\"08:00:00\",\"enddate\":\"2023-12-12\",\"endtime\":\"12:00:00\",\"semester\":2,\"subject\":\"kotlin\",\"batch\":\"gtp01\",\"organizerid\":{\"userid\":1,\"name\":\"admin\",\"email\":\"admin@lnbti.edu.lk\",\"token\":null,\"roleid\":{\"roleid\":1,\"name\":\"admin\"},\"isuseravailable\":1},\"lecturerid\":{\"userid\":1,\"name\":\"admin\",\"email\":\"admin@lnbti.edu.lk\",\"token\":null,\"roleid\":{\"roleid\":1,\"name\":\"admin\"},\"isuseravailable\":1},\"lecturestatusid\":{\"lecturestatusid\":1,\"statusname\":\"new\"}}]"
// Create a Gson instance
    val gson = Gson()

// Define a TypeToken for the list of Lecture objects
    val lectureListType = object : TypeToken<List<Lecture>>() {}.type

// Parse the JSON string into a list of Lecture objects
    val lectureList: List<Lecture> = gson.fromJson(jsonString, lectureListType)

    IAmPresentTheme {
        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = R.string.today,
//                    description = R.string.today_description,
//                )
//            },
//            floatingActionButton = {
//                AddNewLectureButton(onNewLectureClicked = {})
//            },
//            bottomBar = {
//                BottomNavigation(
//                    onTodayNavButtonClicked = {},
//                    onAllNavButtonClicked = {},
//                    onReportsNavButtonClicked = {}
//                )
//            }
        ) { padding ->
            Column(

                Modifier
//                .background(
//                brush = Brush.verticalGradient(
//                    colors = listOf(
//                        MaterialTheme.colorScheme.primaryContainer,
//                        MaterialTheme.colorScheme.secondaryContainer
//                    )
//                )
//            )
                    .padding(padding)
            ) {

                LectureListContent(
                    lectureList = lectureList,
                    onLectureItemClicked = { },
                )
            }
        }
    }
}