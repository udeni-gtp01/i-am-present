package lk.lnbti.iampresent.ui.compose

import LoadingScreen
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.data.Result
import lk.lnbti.iampresent.ui.theme.CommonColorScheme
import lk.lnbti.iampresent.view_model.NewLectureViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * Composable function for the New Lecture screen.
 *
 * @param newLectureViewModel The ViewModel for managing the new lecture data.
 * @param onSuccessfulSave Callback function invoked upon successful lecture save.
 * @param onTodayNavButtonClicked Callback function for the "Today" navigation button.
 * @param onAllNavButtonClicked Callback function for the "All" navigation button.
 * @param modifier Modifier for styling and layout customization.
 * @param onReportsNavButtonClicked Callback function for the "Reports" navigation button.
 */
@Composable
fun NewLectureScreen(
    newLectureViewModel: NewLectureViewModel = hiltViewModel(),
    onSuccessfulSave: (String) -> Unit,
    onTodayNavButtonClicked: () -> Unit,
    onAllNavButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    onReportsNavButtonClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = R.string.new_title,
                description = R.string.new_title_description,
                modifier = modifier
            )
        },
        bottomBar = {
            CoordinatorBottomNavigation(
                onTodayNavButtonClicked = onTodayNavButtonClicked,
                onAllNavButtonClicked = onAllNavButtonClicked,
                onReportsNavButtonClicked = onReportsNavButtonClicked,
                isTodayNavItemSelected = false,
                isReportsNavItemSelected = false,
                isAllNavItemSelected = false
            )
        }
    ) { padding ->
        Column(
            modifier
                .padding(padding)
        ) {
            val batch: String by newLectureViewModel.batch.observeAsState(initial = "")
            val semester: String by newLectureViewModel.semester.observeAsState(initial = "")
            val subject: String by newLectureViewModel.subject.observeAsState(initial = "")
            val location: String by newLectureViewModel.location.observeAsState(initial = "")
            val startDate: String by newLectureViewModel.startDate.observeAsState(initial = "")
            val startTime: String by newLectureViewModel.startTime.observeAsState(initial = "")
            val endDate: String by newLectureViewModel.endDate.observeAsState(initial = "")
            val endTime: String by newLectureViewModel.endTime.observeAsState(initial = "")
            val lecturerName: String by newLectureViewModel.lecturerName.observeAsState(initial = "")
            val lecturerEmail: String by newLectureViewModel.lecturerEmail.observeAsState(initial = "")
            val isBatchError: Boolean by newLectureViewModel.isBatchError.observeAsState(initial = false)
            val isSemesterError: Boolean by newLectureViewModel.isSemesterError.observeAsState(
                initial = false
            )
            val isSubjectError: Boolean by newLectureViewModel.isSubjectError.observeAsState(initial = false)
            val isLocationError: Boolean by newLectureViewModel.isLocationError.observeAsState(
                initial = false
            )
            val isStartdateError: Boolean by newLectureViewModel.isStartDateError.observeAsState(
                initial = false
            )
            val isStartTimeError: Boolean by newLectureViewModel.isStartTimeError.observeAsState(
                initial = false
            )
            val isEndDateError: Boolean by newLectureViewModel.isEndDateError.observeAsState(initial = false)
            val isEndTimeError: Boolean by newLectureViewModel.isEndTimeError.observeAsState(initial = false)
            val isLecturerNameError: Boolean by newLectureViewModel.isLecturerNameError.observeAsState(
                initial = false
            )
            val isLecturerEmailError: Boolean by newLectureViewModel.isLecturerEmailError.observeAsState(
                initial = false
            )
            val lectureSaveResult by newLectureViewModel.lectureSaveResult.observeAsState(null)
            val savedLectureId by newLectureViewModel.savedLectureId.observeAsState("0")

            when (lectureSaveResult) {
                is Result.Loading -> {
                    // Handle loading state
                    LoadingScreen()
                }

                is Result.Success -> {
                    // Handle success state
                    newLectureViewModel.resetLectureSaveResult()
                    onSuccessfulSave(savedLectureId)
                }

                is Result.Error -> {
                    // Handle error state
                    val errorMessage = (lectureSaveResult as Result.Error).message
                    ErrorScreen(
                        errorMessage = errorMessage,
                        onRetry = { newLectureViewModel.saveLecture() })
                }

                else -> {
                    NewLectureContent(
                        batch = batch,
                        semester = semester,
                        subject = subject,
                        location = location,
                        startDate = startDate,
                        startTime = startTime,
                        endDate = endDate,
                        endTime = endTime,
                        lecturerName = lecturerName,
                        lecturerEmail = lecturerEmail,
                        isBatchError = isBatchError,
                        isSemesterError = isSemesterError,
                        isSubjectError = isSubjectError,
                        isLocationError = isLocationError,
                        isStartdateError = isStartdateError,
                        isStartTimeError = isStartTimeError,
                        isEndDateError = isEndDateError,
                        isEndTimeError = isEndTimeError,
                        isLecturerNameError = isLecturerNameError,
                        isLecturerEmailError = isLecturerEmailError,
                        onBatchChange = { newLectureViewModel.onBatchChange(it) },
                        onSemesterChange = { newLectureViewModel.onSemesterChange(it) },
                        onSubjectChange = { newLectureViewModel.onSubjectChange(it) },
                        onLocationChange = { newLectureViewModel.onLocationChange(it) },
                        onStartDateChange = { newLectureViewModel.onStartDateChange(it) },
                        onStartTimeChange = { newLectureViewModel.onStartTimeChange(it) },
                        onEndDateChange = { newLectureViewModel.onEndDateChange(it) },
                        onEndTimeChange = { newLectureViewModel.onEndTimeChange(it) },
                        onLecturerNameChange = { newLectureViewModel.onLecturerNameChange(it) },
                        onLecturerEmailChange = { newLectureViewModel.onLecturerEmailChange(it) },
                        onSaveButtonClicked = {
                            if (newLectureViewModel.isValidationSuccess()) {
                                newLectureViewModel.saveLecture()
                            }
                        },
                        modifier = modifier
                    )
                }
            }
            Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
        }
    }
}

/**
 * Composable function for the content of the New Lecture screen.
 *
 * @param batch Batch value.
 * @param semester Semester value.
 * @param subject Subject value.
 * @param location Location value.
 * @param startDate Start date value.
 * @param startTime Start time value.
 * @param endDate End date value.
 * @param endTime End time value.
 * @param lecturerName Lecturer name value.
 * @param lecturerEmail Lecturer email value.
 * @param isBatchError Flag indicating if there is a batch input error.
 * @param isSemesterError Flag indicating if there is a semester input error.
 * @param isSubjectError Flag indicating if there is a subject input error.
 * @param isLocationError Flag indicating if there is a location input error.
 * @param isStartdateError Flag indicating if there is a start date input error.
 * @param isStartTimeError Flag indicating if there is a start time input error.
 * @param isEndDateError Flag indicating if there is an end date input error.
 * @param isEndTimeError Flag indicating if there is an end time input error.
 * @param isLecturerNameError Flag indicating if there is a lecturer name input error.
 * @param isLecturerEmailError Flag indicating if there is a lecturer email input error.
 * @param onBatchChange Callback for batch value change.
 * @param onSemesterChange Callback for semester value change.
 * @param onSubjectChange Callback for subject value change.
 * @param onLocationChange Callback for location value change.
 * @param onStartDateChange Callback for start date value change.
 * @param onStartTimeChange Callback for start time value change.
 * @param onEndDateChange Callback for end date value change.
 * @param onEndTimeChange Callback for end time value change.
 * @param onLecturerNameChange Callback for lecturer name value change.
 * @param onLecturerEmailChange Callback for lecturer email value change.
 * @param onSaveButtonClicked Callback for the save button click.
 * @param modifier Modifier for styling and layout customization.
 */
@Composable
fun NewLectureContent(
    batch: String,
    semester: String,
    subject: String,
    location: String,
    startDate: String,
    startTime: String,
    endDate: String,
    endTime: String,
    lecturerName: String,
    lecturerEmail: String,
    isBatchError: Boolean,
    isSemesterError: Boolean,
    isSubjectError: Boolean,
    isLocationError: Boolean,
    isStartdateError: Boolean,
    isStartTimeError: Boolean,
    isEndDateError: Boolean,
    isEndTimeError: Boolean,
    isLecturerNameError: Boolean,
    isLecturerEmailError: Boolean,

    onBatchChange: (String) -> Unit,
    onSemesterChange: (String) -> Unit,
    onSubjectChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onStartDateChange: (String) -> Unit,
    onStartTimeChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit,
    onEndTimeChange: (String) -> Unit,
    onLecturerNameChange: (String) -> Unit,
    onLecturerEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onSaveButtonClicked: () -> Unit,
) {
    LazyColumn(contentPadding = PaddingValues(16.dp)) {

        item {
            NewOutlinedTextField(
                value = batch,
                label = {
                    if (isBatchError) {
                        Text(text = stringResource(id = R.string.enter_correct_batch))
                    } else {
                        Text(text = stringResource(id = R.string.enter_batch))
                    }
                },
                onValueChange = onBatchChange,
                isError = isBatchError,
                modifier = modifier
            )
        }
        item {
            NewOutlinedTextField(
                value = semester,
                label = {
                    if (isSemesterError) {
                        Text(text = stringResource(id = R.string.enter_correct_semester))
                    } else {
                        Text(text = stringResource(id = R.string.enter_semester))
                    }
                },
                onValueChange = onSemesterChange,
                keyboardType = KeyboardType.Number,
                isError = isSemesterError,
                modifier = modifier
            )
        }
        item {
            NewOutlinedTextField(
                value = subject,
                label = {
                    if (isSubjectError) {
                        Text(text = stringResource(id = R.string.enter_correct_subject))
                    } else {
                        Text(text = stringResource(id = R.string.enter_subject))
                    }
                },
                onValueChange = onSubjectChange,
                isError = isSubjectError,
                modifier = modifier
            )
        }
        item {
            NewOutlinedTextField(
                value = location,
                label = {
                    if (isLocationError) {
                        Text(text = stringResource(id = R.string.enter_correct_location))
                    } else {
                        Text(text = stringResource(id = R.string.enter_location))
                    }
                },
                onValueChange = onLocationChange,
                isError = isLocationError,
                modifier = modifier
            )
        }
        item {
            NewDateField(
                value = startDate,
                label = stringResource(id = R.string.start_date),
                isDateError = isStartdateError,
                onValueChange = onStartDateChange
            )
        }
        item {
            NewTimeField(
                value = startTime,
                label = stringResource(id = R.string.start_time),
                isTimeError = isStartTimeError,
                onValueChange = onStartTimeChange
            )
        }
        item {
            NewDateField(
                value = endDate,
                label = stringResource(id = R.string.end_date),
                isDateError = isEndDateError,
                onValueChange = onEndDateChange
            )
        }
        item {
            NewTimeField(
                value = endTime,
                label = stringResource(id = R.string.end_time),
                isTimeError = isEndTimeError,
                onValueChange = onEndTimeChange
            )
        }
        item {
            NewOutlinedTextField(
                value = lecturerName,
                label = {
                    if (isLecturerNameError) {
                        Text(text = stringResource(id = R.string.enter_correct_lecturer_name))
                    } else {
                        Text(text = stringResource(id = R.string.enter_lecturer_name))
                    }
                },
                onValueChange = onLecturerNameChange,
                isError = isLecturerNameError,
                modifier = modifier
            )
        }
        item {

            NewOutlinedTextField(
                value = lecturerEmail,
                label = {
                    if (isLecturerEmailError) {
                        Text(text = stringResource(id = R.string.enter_correct_lecturer_email))
                    } else {
                        Text(text = stringResource(id = R.string.enter_lecturer_email))
                    }
                },
                onValueChange = onLecturerEmailChange,
                isError = isLecturerEmailError,
                modifier = modifier
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CommonColorScheme.dark_text),
                onClick = onSaveButtonClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                CommonColorScheme.shade_yellow,
                                CommonColorScheme.main_orange
                            )
                        ),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner))
                    )
            ) {
                Text(
                    text = stringResource(R.string.save),
                    fontSize = 16.sp
                )
            }
        }
    }
}

/**
 * Composable function for an outlined text field with specific styling.
 *
 * @param value Current value of the text field.
 * @param label Label for the text field.
 * @param onValueChange Callback for value change.
 * @param singleLine Whether the text field should be a single line.
 * @param isError Whether the text field has an error.
 * @param keyboardType Type of keyboard to display.
 * @param keyboardActions Actions to be associated with the keyboard.
 * @param modifier Modifier for styling and layout customization.
 */
@Composable
fun NewOutlinedTextField(
    value: String,
    label: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        label = label,
        onValueChange = onValueChange,
        singleLine = singleLine,
        isError = isError,
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = keyboardType
        ),
        keyboardActions = keyboardActions,
        modifier = modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(dimensionResource(id = R.dimen.padding_between_field)))
}

/**
 * Composable function for a time input field.
 *
 * @param value Current value of the time field.
 * @param label Label for the time field.
 * @param isTimeError Whether the time field has an error.
 * @param onValueChange Callback for value change.
 */
@Composable
fun NewDateField(
    value: String,
    label: String,
    isDateError: Boolean,
    onValueChange: (String) -> Unit,
) {
    val context = LocalContext.current
    val defaultDateFormat = SimpleDateFormat("yyyy-M-d")
    val newDateFormat = SimpleDateFormat("MMMM d, yyyy")
    val calendar = Calendar.getInstance()
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
    var selectedDate: String = ""

    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, SelectedDayOfMonth: Int ->
            val newDate = "$selectedYear-${selectedMonth + 1}-$SelectedDayOfMonth"
            val date = defaultDateFormat.parse(newDate)
            selectedDate = newDateFormat.format(date)
            onValueChange(selectedDate)
        },
        year,
        month,
        dayOfMonth
    )
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedButton(
            onClick = { datePicker.show() }
        ) {
            Text(text = label)
        }
        if (isDateError) {
            Text(
                text = stringResource(id = R.string.invalid_date),
                color = MaterialTheme.colorScheme.error
            )
        } else {
            Text(text = value)
        }
    }
    Spacer(Modifier.height(dimensionResource(id = R.dimen.padding_between_field)))
}

@Composable
fun NewTimeField(
    value: String,
    label: String,
    isTimeError: Boolean,
    onValueChange: (String) -> Unit,
) {
    val context = LocalContext.current
    val defaultTimeFormat = SimpleDateFormat("H:m")
    val newTimeFormat = SimpleDateFormat("h:mm a")
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]
    var selectedTime: String = ""

    val timePicker = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            val newTime = "$selectedHour:$selectedMinute"
            val time = defaultTimeFormat.parse(newTime)
            selectedTime = newTimeFormat.format(time)
            onValueChange(selectedTime)
        },
        hour,
        minute,
        true
    )
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedButton(
            onClick = { timePicker.show() }
        ) {
            Text(text = label)
        }
        if (isTimeError) {
            Text(
                text = stringResource(id = R.string.invalid_time),
                color = MaterialTheme.colorScheme.error
            )
        } else {
            Text(text = value)
        }

    }
    Spacer(Modifier.height(dimensionResource(id = R.dimen.padding_between_field)))
}