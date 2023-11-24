package lk.lnbti.iampresent.ui.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import lk.lnbti.iampresent.ui.theme.CommonColorScheme
import lk.lnbti.iampresent.view_model.AttendanceListViewModel

typealias OnAttendanceItemClicked = (String) -> Unit

@Composable
fun AttendanceListScreen(
    onAttendanceItemClicked: OnAttendanceItemClicked,
    onNewAttendanceClicked: () -> Unit,
    attendanceListViewModel: AttendanceListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val groupedAttendanceList: Map<String, List<Attendance>> by attendanceListViewModel.groupedAttendanceList.observeAsState(
        emptyMap()
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
            AddNewAttendanceButton(onNewAttendanceClicked = onNewAttendanceClicked)
        },
    ) { padding ->
        Column(
            modifier
                .padding(padding)
        ) {
            groupBySection(
                onGroupByDateClicked = { attendanceListViewModel.groupAttendanceListByDate() },
                onGroupByLectureStatusClicked = { attendanceListViewModel.groupAttendanceListByLectureStatus() },
                onGroupBySubjectClicked = { attendanceListViewModel.groupAttendanceListBySubject() },
                onGroupByLecturerClicked = { attendanceListViewModel.groupAttendanceListByLecturer() },
                onGroupByLocationClicked = { attendanceListViewModel.groupAttendanceListByLectureLocation() },
            )
            AttendanceListSection(
                groupedAttendanceList = groupedAttendanceList,
                onAttendanceItemClicked = onAttendanceItemClicked,
                modifier = modifier
            )
            Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
        }
    }
}

@Composable
fun AddNewAttendanceButton(onNewAttendanceClicked: () -> Unit) {
    FloatingActionButton(
        shape = MaterialTheme.shapes.large.copy(CornerSize(percent = 50)),
        onClick = onNewAttendanceClicked,
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = Color.Transparent,
    ) {
        Box(
            modifier = Modifier
                .size(56.dp, 56.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            CommonColorScheme.nav_light_blue,
                            CommonColorScheme.nav_blue
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            val painter: Painter = painterResource(id = R.drawable.qr_icon)
            Icon(painter, contentDescription = null, tint = CommonColorScheme.white)

        }
    }
}

@Composable
private fun groupBySection(
    onGroupByDateClicked: () -> Unit,
    onGroupByLectureStatusClicked: () -> Unit,
    onGroupBySubjectClicked: () -> Unit,
    onGroupByLecturerClicked: () -> Unit,
    onGroupByLocationClicked: () -> Unit,
) {
    val criteriaList = listOf(
        R.string.filter_by_date,
        R.string.filter_by_lecture_status,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AttendanceListSection(
    groupedAttendanceList: Map<String, List<Attendance>>,
    onAttendanceItemClicked: OnAttendanceItemClicked,
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
            groupedAttendanceList.forEach { (initial, attendanceList) ->
                initial?.let {
                    stickyHeader {
                        ListGroupHeader(initial)
                    }
                }

                items(attendanceList) { attendanceItem ->
                    AttendanceListItemContent(
                        content = {
                            AttendanceListItem(
                                item = attendanceItem,
                                onAttendanceItemClicked = onAttendanceItemClicked
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AttendanceListItemContent(
    content: @Composable() (ColumnScope.() -> Unit)
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_between_label))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CommonColorScheme.main_orange,
                        CommonColorScheme.main_blue
                    )
                ),
                shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner))
            )
    ) {
        Column(
            content = content
        )
    }
}

@Composable
fun AttendanceListItem(
    item: Attendance,
    onAttendanceItemClicked: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onAttendanceItemClicked(item.lecture.lectureId.toString()) }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${stringResource(R.string.batch)} ",
                    style = MaterialTheme.typography.bodySmall,
                    color = CommonColorScheme.dark_text
                )
                Text(
                    text = item.lecture.batch,
                    style = MaterialTheme.typography.titleLarge,
                    color = CommonColorScheme.dark_text
                )
            }
            if (item.lecture.lectureStatus.lectureStatusId == 2) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = CommonColorScheme.status_ongoing),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner)),
                ) {
                    Text(
                        text = "${stringResource(id = R.string.ongoing)}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(7.dp),
                        color = CommonColorScheme.dark_text
                    )
                }
            } else if (item.lecture.lectureStatus.lectureStatusId == 3) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = CommonColorScheme.status_complete),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner)),
                    modifier = Modifier
                ) {
                    Text(
                        text = "${stringResource(id = R.string.complete)}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(7.dp),
                        color = CommonColorScheme.dark_text
                    )
                }
            }
        }
        Text(
            text = "${stringResource(R.string.subject)}: ${item.lecture.subject}",
            style = MaterialTheme.typography.bodyLarge,
            color = CommonColorScheme.dark_text
        )
        Text(
            text = "${stringResource(id = R.string.lecturer)}: ${item.lecture.lecturer.name}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 6.dp, bottom = 6.dp),
            color = CommonColorScheme.dark_text
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            val datePainter: Painter = painterResource(id = R.drawable.calendar)
            Icon(
                painter = datePainter,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = CommonColorScheme.dark_text
            )
            Text(
                text = item.lecture.startDate,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 10.dp,top = 6.dp, bottom = 6.dp),
                color = CommonColorScheme.dark_text
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val timePainter: Painter = painterResource(id = R.drawable.time)
                Icon(
                    painter = timePainter,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = CommonColorScheme.dark_text
                )
                Text(
                    text = "${item.lecture.startTime} - ${item.lecture.endTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 10.dp),
                    color = CommonColorScheme.dark_text
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                val locationPainter: Painter = painterResource(id = R.drawable.location)
                Icon(
                    painter = locationPainter,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = CommonColorScheme.dark_text
                )
                Text(
                    text = "${item.lecture.location}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CommonColorScheme.dark_text
                )
            }
        }
    }
}