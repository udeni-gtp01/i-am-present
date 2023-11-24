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
import lk.lnbti.iampresent.view_model.LectureListViewModel

/**
 * Composable function for the Lecture List screen.
 *
 * @param onLectureItemClicked Callback function for when a lecture item is clicked.
 * @param onNewLectureClicked Callback function for when the "New Lecture" button is clicked.
 * @param lectureListViewModel ViewModel for managing the lecture list data.
 * @param onTodayNavButtonClicked Callback function for the "Today" navigation button.
 * @param onReportsNavButtonClicked Callback function for the "Reports" navigation button.
 * @param onAllNavButtonClicked Callback function for the "All" navigation button.
 * @param modifier Modifier for styling and layout customization.
 */
@Composable
fun LectureListScreen(
    onLectureItemClicked: (String) -> Unit,
    onNewLectureClicked: () -> Unit,
    lectureListViewModel: LectureListViewModel = hiltViewModel(),
    onTodayNavButtonClicked: () -> Unit,
    onReportsNavButtonClicked: () -> Unit,
    onAllNavButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val groupedLectureList: Map<String, List<Lecture>> by lectureListViewModel.groupedLectureList.observeAsState(
        emptyMap()
    )
    val lectureListResult by lectureListViewModel.lectureListResult.observeAsState(Result.Loading)
    Scaffold(
        topBar = {
            TopAppBar(
                title = R.string.all,
                description = R.string.all_description,
                modifier = modifier
            )
        },
        floatingActionButton = {
            AddNewLectureButton(onNewLectureClicked = onNewLectureClicked)
        },
        bottomBar = {
            CoordinatorBottomNavigation(
                onTodayNavButtonClicked = onTodayNavButtonClicked,
                onReportsNavButtonClicked = onReportsNavButtonClicked,
                onAllNavButtonClicked = onAllNavButtonClicked,
                isTodayNavItemSelected = false,
                isReportsNavItemSelected = false,
                isAllNavItemSelected = true
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
                    GroupBySection(
                        onGroupByDateClicked = { lectureListViewModel.groupLectureListByStartDate() },
                        onGroupByLectureStatusClicked = { lectureListViewModel.groupLectureListByLectureStatus() },
                        onGroupByBatchClicked = { lectureListViewModel.groupLectureListByBatch() },
                        onGroupBySubjectClicked = { lectureListViewModel.groupLectureListBySubject() },
                        onGroupByLecturerClicked = { lectureListViewModel.groupLectureListByLecturer() },
                        onGroupByLocationClicked = { lectureListViewModel.groupLectureListByLocation() },
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
                        onRetry = { lectureListViewModel.findLectureList() })
                }
            }
            Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
        }
    }
}

/**
 * Composable function for the section that groups lectures based on specified criteria.
 *
 * @param onGroupByDateClicked Callback function for grouping by date.
 * @param onGroupByLectureStatusClicked Callback function for grouping by lecture status.
 * @param onGroupByBatchClicked Callback function for grouping by batch.
 * @param onGroupBySubjectClicked Callback function for grouping by subject.
 * @param onGroupByLecturerClicked Callback function for grouping by lecturer.
 * @param onGroupByLocationClicked Callback function for grouping by location.
 */
@Composable
private fun GroupBySection(
    onGroupByDateClicked: () -> Unit,
    onGroupByLectureStatusClicked: () -> Unit,
    onGroupByBatchClicked: () -> Unit,
    onGroupBySubjectClicked: () -> Unit,
    onGroupByLecturerClicked: () -> Unit,
    onGroupByLocationClicked: () -> Unit,
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
    when (selectedItem) {
        R.string.filter_by_date -> {
            run { onGroupByDateClicked() }
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
 * Composable function for the section of the lecture list.
 *
 * @param groupedLectureList Map of grouped lectures.
 * @param onLectureItemClicked Callback function for when a lecture item is clicked.
 * @param modifier Modifier for styling and layout customization.
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
