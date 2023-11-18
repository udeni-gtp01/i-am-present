package lk.lnbti.iampresent.ui.view

import ErrorScreen
import LoadingScreen
import android.content.*
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.opencsv.CSVWriter
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.data.Attendance
import lk.lnbti.iampresent.data.Result
import lk.lnbti.iampresent.ui.theme.DefaultColorScheme
import lk.lnbti.iampresent.view_model.ReportsViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ReportsScreen(
    reportsViewModel: ReportsViewModel = hiltViewModel(),
    onTodayNavButtonClicked: () -> Unit,
    onReportsNavButtonClicked: () -> Unit,
    onAllNavButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val attendanceList: List<Attendance> by reportsViewModel.attendanceList.observeAsState(emptyList())
    val attendanceListResult by reportsViewModel.attendanceListResult.observeAsState(Result.Loading)

    Scaffold(
        topBar = {
            TopAppBar(
                title = R.string.reports,
                description = R.string.reports_description,
                modifier = modifier
            )
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
                .padding(padding)
        ) {
            when (attendanceListResult) {
                is Result.Loading -> {
                    // Handle loading state
                    LoadingScreen()
                }

                is Result.Success<*> -> {
                    // Handle success state
                    FilterSection(
                        attendanceList = attendanceList,
                        onDownloadButtonClicked = { pickedUri: Uri, attendanceResult: List<Attendance> ->
                            reportsViewModel.saveFileToPickedDirectory(
                                pickedUri, attendanceResult
                            )
                        },
                        modifier = modifier
                    )
                }

                is Result.Error -> {
                    // Handle error state
                    val errorMessage = (attendanceListResult as Result.Error).message
                    ErrorScreen(
                        errorMessage = errorMessage,
                        onRetry = { reportsViewModel.findAttendanceList() })
                }
            }
            Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
        }
    }
}

@Composable
fun ReportSearchBar(
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

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
private fun FilterSection(
    attendanceList: List<Attendance>,
    onDownloadButtonClicked: (Uri, List<Attendance>) -> Unit,
    modifier: Modifier = Modifier
) {
    val criteriaList = listOf(
        R.string.filter_by_batch,
        R.string.filter_by_date,
        R.string.filter_by_student,
        R.string.filter_by_lecturer,
        R.string.filter_by_lecture_status,
        R.string.filter_by_location,
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
                ReportFilterItem(
                    criteria = item,
                    isSelected = isSelected,
                    onClick = { selectedItem = item },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Column(modifier = modifier.fillMaxWidth()) {
            OptionDropdown(
                attendanceList = attendanceList,
                selectedFilter = selectedItem,
                onDownloadButtonClicked = onDownloadButtonClicked,
                modifier = modifier
            )
        }

    }
}

@Composable
fun ReportFilterItem(
    @StringRes criteria: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) DefaultColorScheme.secondary else Color.White

    Text(
        text = stringResource(id = criteria),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(8.dp)
            .defaultMinSize(minWidth = 50.dp),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        color = if (isSelected) DefaultColorScheme.accent else DefaultColorScheme.primary,
    )
}


@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionDropdown(
    attendanceList: List<Attendance>,
    selectedFilter: Int,
    onDownloadButtonClicked: (Uri, List<Attendance>) -> Unit,
    modifier: Modifier,
) {
    var grouped: Map<String, List<Attendance>>? = null
    var listItems = mutableListOf<String>()
    var itemResults = mutableListOf<List<Attendance>>()
    // remember the selected item

    when (selectedFilter) {
        R.string.filter_by_date -> {
            val sortedAttendanceList = attendanceList.sortedBy { it.lecture.startDate }
            grouped = sortedAttendanceList.groupBy { it.lecture.startDate }
            listItems = emptyList<String>().toMutableList()
//            selectedItem= mutableStateOf("").toString()
        }

        R.string.filter_by_lecture_status -> {
            val sortedAttendanceList =
                attendanceList.sortedBy { it.lecture.lectureStatus.statusName }
            grouped = sortedAttendanceList.groupBy { it.lecture.lectureStatus.statusName }
            listItems = emptyList<String>().toMutableList()
//            selectedItem=""
        }

        R.string.filter_by_batch -> {
            val sortedAttendanceList = attendanceList.sortedBy { it.lecture.batch }
            grouped = sortedAttendanceList.groupBy { it.lecture.batch }
            listItems = emptyList<String>().toMutableList()
//            selectedItem=""
        }

        R.string.filter_by_lecturer -> {
            val sortedAttendanceList = attendanceList.sortedBy { it.lecture.lecturer.name }
            grouped = sortedAttendanceList.groupBy { it.lecture.lecturer.name }
            listItems = emptyList<String>().toMutableList()
//            selectedItem=""
        }

        R.string.filter_by_student -> {
            val sortedAttendanceList = attendanceList.sortedBy { it.student.email }
            grouped = sortedAttendanceList.groupBy { it.student.email!! }
            listItems = emptyList<String>().toMutableList()
//            selectedItem=""
        }

        R.string.filter_by_location -> {
            val sortedAttendanceList = attendanceList.sortedBy { it.lecture.location }
            grouped = sortedAttendanceList.groupBy { it.lecture.location }
            listItems = emptyList<String>().toMutableList()
//            selectedItem=""
        }
    }

    grouped?.forEach { (initial, attendanceList) ->
        listItems.add(initial.toString())
        itemResults.add(attendanceList)
    }

    var selectedItem by rememberSaveable {
        mutableStateOf("")
    }
    // state of the menu
    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .menuAnchor(),
            value = selectedItem,
            onValueChange = { },
            readOnly = true,
            label = { Text(stringResource(R.string.select_option)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            listItems.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(text = selectionOption) },
                    onClick = {
                        selectedItem = selectionOption
                        expanded = false
                    }
                )
            }
        }
    }
    Column {
        if (itemResults.isNotEmpty() && listItems.indexOf(selectedItem) > -1) {
            var fileName="Attendance_report_${stringResource(id = selectedFilter)}_${selectedItem}.csv"
            ResultViewer(
                fileName=fileName,
                itemResults[listItems.indexOf(selectedItem)],
                onDownloadButtonClicked = onDownloadButtonClicked
            )
        }
    }
}

@Composable
fun ResultViewer(
    fileName:String,
    attendanceList: List<Attendance>,
    onDownloadButtonClicked: (Uri, attendanceResult: List<Attendance>) -> Unit
) {
    val context = LocalContext.current
    val onShareDataOpen = remember {
        mutableStateOf(false)
    }

    if (onShareDataOpen.value) {
        val uri = saveFileAndGetUri(context,fileName, attendanceList = attendanceList)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/csv"
        intent.putExtra(Intent.EXTRA_SUBJECT, "My Export Data")
        intent.putExtra(Intent.EXTRA_STREAM, uri)

        val chooser = Intent.createChooser(intent, "Share With")
        ContextCompat.startActivity(
            context,
            chooser,
            null
        )
        onShareDataOpen.value = false
    }

    Text(text = "Results: ${attendanceList.size} available.")
    Spacer(modifier = Modifier.height(30.dp))
    Button(onClick = { onShareDataOpen.value = true }) {
        Text(text = stringResource(id = R.string.share_result))
    }
}

// Function to save data to a file and return its content URI
private fun saveFileAndGetUri(
    context: Context,
    fileName:String,
    attendanceList: List<Attendance>
): Uri {
    try {
        var data1 = mutableListOf(
            arrayOf(
                "Batch",
                "Semester",
                "Subject",
                "Lecture Date",
                "Start Time",
                "End Time",
                "Student email",
                "Arrival time"
            ),
        )
// Convert lectureList into a list of arrays
        attendanceList.map { attendanceItem ->
            data1.add(
                arrayOf(
                    attendanceItem.lecture.batch,
                    attendanceItem.lecture.semester.toString(),
                    attendanceItem.lecture.subject,
                    attendanceItem.lecture.startDate,
                    attendanceItem.lecture.startTime,
                    attendanceItem.lecture.endTime,
                    attendanceItem.student.email ?: "",
                    attendanceItem.checkintime
                )
            )
        }

        val fos: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        val writer = fos.bufferedWriter()
        val writer1 = CSVWriter(writer)
        writer1.writeAll(data1)
        writer1.flush()
        writer1.close()
        fos.flush()
        fos.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    val file = File(context.filesDir, fileName)
    return FileProvider.getUriForFile(
        context,
        context.applicationContext.packageName + ".provider",
        file
    )
}

