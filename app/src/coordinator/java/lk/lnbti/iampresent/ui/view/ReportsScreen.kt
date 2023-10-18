package lk.lnbti.iampresent.ui.view

import ErrorScreen
import LoadingScreen
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.constant.Constant
import lk.lnbti.iampresent.data.Attendance
import lk.lnbti.iampresent.data.Result
import lk.lnbti.iampresent.ui.theme.DefaultColorScheme
import lk.lnbti.iampresent.view_model.ReportsViewModel
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.FileOutputStream


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
                        onDownloadButtonClicked = { attendanceResult ->
                            reportsViewModel.generateExcelReport(
                                attendanceResult
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

@Composable
private fun FilterSection(
    attendanceList: List<Attendance>,
    onDownloadButtonClicked: (List<Attendance>) -> Unit,
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
                FilterItem(
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
private fun FilterItem(
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionDropdown(
    attendanceList: List<Attendance>,
    selectedFilter: Int,
    onDownloadButtonClicked: (List<Attendance>) -> Unit,
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
            ResultViewer(
                itemResults[listItems.indexOf(selectedItem)],
                onDownloadButtonClicked = onDownloadButtonClicked
            )
        }
    }
}

@Composable
fun p(selectionOption: String) {

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ResultViewer(
    attendanceList: List<Attendance>,
    onDownloadButtonClicked: (attendanceResult: List<Attendance>) -> Unit
) {
    val filePermissionState =
        rememberPermissionState(permission = Constant.PERMISSION_WRITE_EXTERNAL_STORAGE)

    when {
        filePermissionState.status.isGranted -> {
            Text(text = "Results: ${attendanceList.size} available.")
            Log.d("oyasumi", "Results: ${attendanceList.size} available.")
            Button(onClick = {
                onDownloadButtonClicked(attendanceList)
            }) {
                Text(text = stringResource(id = R.string.download_result))
            }
        }

        filePermissionState.status.shouldShowRationale -> {
            // Permission is denied, but a rationale should be shown
            Text("Please grant file storage permission in app settings.")
        }

        else -> {
            // Permission is denied, show the permission request button
            Button(
                onClick = {
                    filePermissionState.launchPermissionRequest()
                }
            ) {
                Text(text = "Request file storage access permission")
            }
        }
    }




}

@Composable
fun generateExcelReport(data: List<Attendance>) {
    LaunchedEffect(true) {
        // Create a new Excel Workbook using Apache POI
        val workbook: Workbook = WorkbookFactory.create(true)

        // Create a sheet and populate it with data
        val sheet = workbook.createSheet("Data Sheet")

        // Iterate through data and populate the Excel sheet
        var rowNumber = 0
        for (dataItem in data) {
            val row = sheet.createRow(rowNumber)
            // Add data to rows and cells

            // Example:
            row.createCell(0).setCellValue(dataItem.lecture.batch)
            row.createCell(1).setCellValue(dataItem.lecture.startDate)
            // Add more cells as needed
            rowNumber++
        }

        // Save the workbook to a file
        val fileOutputStream = FileOutputStream("your-excel-report.xlsx")
        workbook.write(fileOutputStream)
        fileOutputStream.close()
    }
}

