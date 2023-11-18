package lk.lnbti.iampresent.ui.view

import ErrorScreen
import LoadingScreen
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.opencsv.CSVWriter
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.data.Result
import lk.lnbti.iampresent.ui.theme.CommonColorScheme
import lk.lnbti.iampresent.ui.theme.IAmPresentTheme
import lk.lnbti.iampresent.view_model.TodaysLectureListViewModel
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException


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
                    groupBySection(
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
@Composable
private fun groupBySection(
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
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LectureListSection(
    groupedLectureList: Map<String, List<Lecture>>,
    onLectureItemClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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