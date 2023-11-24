package lk.lnbti.iampresent.ui.compose

import LoadingScreen
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.data.Result
import lk.lnbti.iampresent.view_model.TodaysLectureListViewModel

/**
 * Composable function for displaying the screen with today's lecture list.
 *
 * @param onLectureItemClicked Callback when a lecture item is clicked.
 * @param onNewLectureClicked Callback when the "New Lecture" button is clicked.
 * @param todayLectureListViewModel ViewModel for today's lecture list.
 * @param onTodayNavButtonClicked Callback for the "Today" navigation button.
 * @param onAllNavButtonClicked Callback for the "All" navigation button.
 * @param onReportsNavButtonClicked Callback for the "Reports" navigation button.
 * @param modifier Modifier for styling.
 */
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
    val groupedLectureList: Map<String, List<Lecture>> by todayLectureListViewModel.groupedLectureList.observeAsState(
        emptyMap()
    )
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
            CoordinatorBottomNavigation(
                onTodayNavButtonClicked = onTodayNavButtonClicked,
                onAllNavButtonClicked = onAllNavButtonClicked,
                onReportsNavButtonClicked = onReportsNavButtonClicked,
                isTodayNavItemSelected = true,
                isReportsNavItemSelected = false,
                isAllNavItemSelected = false
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
                    GroupBySection(
                        onGroupByStartTimeClicked = { todayLectureListViewModel.groupLectureListByStartTime() },
                        onGroupByLectureStatusClicked = { todayLectureListViewModel.groupLectureListByLectureStatus() },
                        onGroupByBatchClicked = { todayLectureListViewModel.groupLectureListByBatch() },
                        onGroupBySubjectClicked = { todayLectureListViewModel.groupLectureListBySubject() },
                        onGroupByLecturerClicked = { todayLectureListViewModel.groupLectureListByLecturer() },
                        onGroupByLocationClicked = { todayLectureListViewModel.groupLectureListByLocation() },
                    )
                    LectureListSection(
                        groupedLectureList = groupedLectureList,
                        onLectureItemClicked = onLectureItemClicked,
                        modifier = modifier
                    )
                }

                is Result.Error -> {
                    // Handle error state
                    val errorMessage = (lectureListResult as Result.Error).message
                    ErrorScreen(
                        errorMessage = errorMessage,
                        onRetry = { todayLectureListViewModel.findTodaysLectureList() }
                    )
                }
            }
            Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
        }
    }
}

/**
 * Composable function to display the section for grouping lectures.
 *
 * @param onGroupByStartTimeClicked Callback for the "Group by Start Time" button.
 * @param onGroupByLectureStatusClicked Callback for the "Group by Lecture Status" button.
 * @param onGroupByBatchClicked Callback for the "Group by Batch" button.
 * @param onGroupBySubjectClicked Callback for the "Group by Subject" button.
 * @param onGroupByLecturerClicked Callback for the "Group by Lecturer" button.
 * @param onGroupByLocationClicked Callback for the "Group by Location" button.
 */
@Composable
private fun GroupBySection(
    onGroupByStartTimeClicked: () -> Unit,
    onGroupByLectureStatusClicked: () -> Unit,
    onGroupByBatchClicked: () -> Unit,
    onGroupBySubjectClicked: () -> Unit,
    onGroupByLecturerClicked: () -> Unit,
    onGroupByLocationClicked: () -> Unit,
) {
    val criteriaList = listOf(
        R.string.filter_by_start_time,
        R.string.filter_by_lecture_status,
        R.string.filter_by_location,
        R.string.filter_by_lecturer,
        R.string.filter_by_batch,
        R.string.filter_by_subject,
    )
    var selectedItem by rememberSaveable { mutableStateOf(criteriaList.first()) }
    when (selectedItem) {
        R.string.filter_by_start_time -> {
            run { onGroupByStartTimeClicked() }
        }

        R.string.filter_by_lecture_status -> {
            run { onGroupByLectureStatusClicked() }
        }

        R.string.filter_by_batch -> {
            run { onGroupByBatchClicked() }
        }

        R.string.filter_by_subject -> {
            run { onGroupBySubjectClicked() }
        }

        R.string.filter_by_lecturer -> {
            run { onGroupByLecturerClicked() }
        }

        R.string.filter_by_location -> {
            run { onGroupByLocationClicked() }
        }
    }
    Column {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_between_list_item)),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier
                .padding(vertical = 16.dp)
        ) {
            items(items = criteriaList) {
                val isSelected = selectedItem == it
                FilterItem(
                    criteria = it,
                    isSelected = isSelected,
                    onClick = { selectedItem = it },
                )
            }
        }
    }
}

/**
 * Composable function to display the lecture list section.
 *
 * @param groupedLectureList Map of grouped lectures.
 * @param onLectureItemClicked Callback when a lecture item is clicked.
 * @param modifier Modifier for styling.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LectureListSection(
    groupedLectureList: Map<String, List<Lecture>>,
    onLectureItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_between_list_item)),
            contentPadding = WindowInsets.navigationBars.asPaddingValues(),
            modifier = modifier
        ) {
            groupedLectureList.forEach { (initial, lectureList) ->
                stickyHeader {
                    ListGroupHeader(initial)
                }
                items(lectureList) { lecture ->
                    ListItemContent(
                        content = {
                            LectureListItem(
                                item = lecture,
                                onLectureItemClicked = onLectureItemClicked
                            )
                        }
                    )
                }
            }
        }
    }
}