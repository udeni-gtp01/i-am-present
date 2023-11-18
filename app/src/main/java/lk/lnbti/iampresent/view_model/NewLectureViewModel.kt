package lk.lnbti.iampresent.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.data.LectureStatus
import lk.lnbti.iampresent.data.Result
import lk.lnbti.iampresent.data.User
import lk.lnbti.iampresent.repo.LectureRepo
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * ViewModel class for handling data related to creating and saving new lectures.
 *
 * @param lectureListUiState LiveData representing the UI state of the lecture list.
 * @param todaysLectureListUiState LiveData representing the UI state of today's lecture list.
 * @param lectureInfoUiState LiveData representing the UI state of lecture information.
 * @param lectureRepo Repository for handling lecture-related data operations.
 */
@HiltViewModel
class NewLectureViewModel @Inject constructor(
    private val lectureListUiState: LectureListUiState,
    private val todaysLectureListUiState: TodaysLectureListUiState,
    private val lectureInfoUiState: LectureInfoUiState,
    private val lectureRepo: LectureRepo,
) : ViewModel() {

    private val _batch: MutableLiveData<String> = MutableLiveData("")
    val batch: LiveData<String> = _batch

    private val _semester: MutableLiveData<String> = MutableLiveData("")
    val semester: LiveData<String> = _semester

    private val _subject: MutableLiveData<String> = MutableLiveData("")
    val subject: LiveData<String> = _subject

    private val _location: MutableLiveData<String> = MutableLiveData("")
    val location: LiveData<String> = _location

    private val _startDate: MutableLiveData<String> = MutableLiveData("")
    val startDate: LiveData<String> = _startDate

    private val _startTime: MutableLiveData<String> = MutableLiveData("")
    val startTime: LiveData<String> = _startTime

    private val _endDate: MutableLiveData<String> = MutableLiveData("")
    val endDate: LiveData<String> = _endDate

    private val _endTime: MutableLiveData<String> = MutableLiveData("")
    val endTime: LiveData<String> = _endTime

    private val _lecturerName: MutableLiveData<String> = MutableLiveData("")
    val lecturerName: LiveData<String> = _lecturerName

    private val _lecturerEmail: MutableLiveData<String> = MutableLiveData("")
    val lecturerEmail: LiveData<String> = _lecturerEmail

    private val _isBatchError: MutableLiveData<Boolean> = MutableLiveData(false)
    val isBatchError: LiveData<Boolean> = _isBatchError

    private val _isSemesterError: MutableLiveData<Boolean> = MutableLiveData(false)
    val isSemesterError: LiveData<Boolean> = _isSemesterError

    private val _isSubjectError: MutableLiveData<Boolean> = MutableLiveData(false)
    val isSubjectError: LiveData<Boolean> = _isSubjectError

    private val _isLocationError: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLocationError: LiveData<Boolean> = _isLocationError

    private val _isStartDateError: MutableLiveData<Boolean> = MutableLiveData(false)
    val isStartDateError: LiveData<Boolean> = _isStartDateError

    private val _isStartTimeError: MutableLiveData<Boolean> = MutableLiveData(false)
    val isStartTimeError: LiveData<Boolean> = _isStartTimeError

    private val _isEndDateError: MutableLiveData<Boolean> = MutableLiveData(false)
    val isEndDateError: LiveData<Boolean> = _isEndDateError

    private val _isEndTimeError: MutableLiveData<Boolean> = MutableLiveData(false)
    val isEndTimeError: LiveData<Boolean> = _isEndTimeError

    private val _isLecturerNameError: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLecturerNameError: LiveData<Boolean> = _isLecturerNameError

    private val _isLecturerEmailError: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLecturerEmailError: LiveData<Boolean> = _isLecturerEmailError

    private val _lectureSaveResult = MutableLiveData<Result<Lecture?>>()
    val lectureSaveResult: LiveData<Result<Lecture?>> = _lectureSaveResult

    private val _savedLectureId = MutableLiveData("0")
    val savedLectureId: LiveData<String> = _savedLectureId

    fun onBatchChange(newBatch: String) {
        _batch.value = newBatch
        checkBatchValidation()
    }

    private fun checkBatchValidation() {
        _isBatchError.value = _batch.value.isNullOrBlank()
    }

    fun onSemesterChange(newSemester: String) {
        _semester.value = newSemester
        checkSemesterValidation()
    }

    private fun checkSemesterValidation() {
        _isSemesterError.value = _semester.value.isNullOrBlank()
    }

    fun onSubjectChange(newSubject: String) {
        _subject.value = newSubject
        checkSubjectValidation()
    }

    private fun checkSubjectValidation() {
        _isSubjectError.value = _subject.value.isNullOrBlank()
    }

    fun onLocationChange(newLocation: String) {
        _location.value = newLocation
        checkLocationValidation()
    }

    private fun checkLocationValidation() {
        _isLocationError.value = _location.value.isNullOrBlank()
    }

    fun onStartDateChange(newStartDate: String) {
        _startDate.value = newStartDate
        checkStartDateValidation()
    }

    private fun checkStartDateValidation() {
        if (_startDate.value.isNullOrBlank()) {
            _isStartDateError.value = true
        } else {
            // Parse the start date to compare it with today's date
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val selectedStartDate =
                dateFormat.parse(convertDateToSqlFormat(_startDate.value.toString()))

            // Get today's date
            val currentDate = Calendar.getInstance().time
            val calendar = Calendar.getInstance()
            calendar.time = currentDate

            // Adjust month component because months are zero-based in Calendar
            val formattedCurrentDate =
                dateFormat.parse(
                    "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH) + 1}-${
                        calendar.get(
                            Calendar.DAY_OF_MONTH
                        )
                    }"
                )
            // Check if the selected start date is before today
            val result = selectedStartDate.before(formattedCurrentDate)
            _isStartDateError.value = result
        }
    }

    fun onStartTimeChange(newStartTime: String) {
        _startTime.value = newStartTime
        checkStartTimeValidation()
    }

    private fun checkStartTimeValidation() {
        if (_startDate.value.isNullOrBlank() || _startTime.value.isNullOrBlank()) {
            _isStartTimeError.value = true
        } else {
            val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            // Parse the selected start date and time
            val selectedStartDateTime = dateTimeFormat.parse(
                "${convertDateToSqlFormat(_startDate.value.toString())} ${
                    convertTimeToSqlFormat(
                        _startTime.value.toString()
                    )
                }"
            )

            // Get the current date and time
            val currentDateTime = Calendar.getInstance().time

            // Check if the selected start date and time are before the current date and time
            val result = selectedStartDateTime.before(currentDateTime)
            _isStartTimeError.value = result
        }
    }

    fun onEndDateChange(newEndDate: String) {
        _endDate.value = newEndDate
        checkEndDateValidation()
    }

    private fun checkEndDateValidation() {
        if (_startDate.value.isNullOrBlank() || _endDate.value.isNullOrBlank()) {
            _isEndDateError.value = true
        } else {
            // Parse the start date to compare it with today's date
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val selectedStartDate =
                dateFormat.parse(convertDateToSqlFormat(_startDate.value.toString()))
            val selectedEndDate =
                dateFormat.parse(convertDateToSqlFormat(_endDate.value.toString()))

            // Check if the selected end date is before start date
            val result = selectedEndDate.before(selectedStartDate)
            _isEndDateError.value = result
        }
    }

    fun onEndTimeChange(newEndTime: String) {
        _endTime.value = newEndTime
        checkEndTimeValidation()
    }

    private fun checkEndTimeValidation() {
        if (_startDate.value.isNullOrBlank() || _startTime.value.isNullOrBlank() || _endDate.value.isNullOrBlank() || _endTime.value.isNullOrBlank()) {
            _isEndTimeError.value = true
        } else {
            val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            // Parse the selected start date and time
            val selectedStartDateTime = dateTimeFormat.parse(
                "${convertDateToSqlFormat(_startDate.value.toString())} ${
                    convertTimeToSqlFormat(
                        _startTime.value.toString()
                    )
                }"
            )

            // Parse the selected end date and time
            val selectedEndDateTime = dateTimeFormat.parse(
                "${convertDateToSqlFormat(_endDate.value.toString())} ${
                    convertTimeToSqlFormat(
                        _endTime.value.toString()
                    )
                }"
            )

            // Check if the selected start date and time are before the end date and time
            val result = selectedEndDateTime.before(selectedStartDateTime)
            _isEndTimeError.value = result
        }
    }

    fun onLecturerNameChange(newLecturerName: String) {
        _lecturerName.value = newLecturerName
        checkLecturerNameValidation()
    }

    private fun checkLecturerNameValidation() {
        _isLecturerNameError.value = _lecturerName.value.isNullOrBlank()
    }

    fun onLecturerEmailChange(newLecturerEmail: String) {
        _lecturerEmail.value = newLecturerEmail
        checkLecturerEmailValidation()
    }

    /**
     * Validates the lecture data and sets error state for the lecturer email.
     *
     * @param newLecturerEmail The new lecturer email value.
     */
    private fun checkLecturerEmailValidation() {
        _isLecturerEmailError.value = _lecturerEmail.value.isNullOrBlank()
        if (!isLecturerEmailError.value!!) {
            val emailPattern = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
            )
            val result = lecturerEmail.value?.let { emailPattern.matcher(it).matches() } ?: true
            _isLecturerEmailError.value = !result
        }
    }

    /**
     * Checks all validation criteria for lecture data.
     */
    private fun checkAllValidation() {
        checkBatchValidation()
        checkSemesterValidation()
        checkSubjectValidation()
        checkLocationValidation()
        checkStartDateValidation()
        checkStartTimeValidation()
        checkEndDateValidation()
        checkEndTimeValidation()
        checkLecturerNameValidation()
        checkLecturerEmailValidation()
    }

    /**
     * Checks if all validation criteria are successful.
     *
     * @return True if all validations pass, false otherwise.
     */
    fun isValidationSuccess(): Boolean {
        checkAllValidation()
        return _isBatchError.value == false &&
                _isSemesterError.value == false &&
                _isSubjectError.value == false &&
                _isLocationError.value == false &&
                _isStartDateError.value == false &&
                _isStartTimeError.value == false &&
                _isEndDateError.value == false &&
                _isEndTimeError.value == false &&
                _isLecturerNameError.value == false &&
                _isLecturerEmailError.value == false
    }

    /**
     * Saves the lecture data and updates the UI accordingly.
     */
    fun saveLecture() {
        _lectureSaveResult.value = Result.Loading
        val newLecture: Lecture = Lecture(
            batch = _batch.value.toString(),
            semester = _semester.value.toString().toInt(),
            subject = _subject.value.toString(),
            location = _location.value.toString(),
            startDate = convertDateToSqlFormat(_startDate.value.toString()),
            startTime = convertTimeToSqlFormat(_startTime.value.toString()),
            endDate = convertDateToSqlFormat(_endDate.value.toString()),
            endTime = convertTimeToSqlFormat(_endTime.value.toString()),
            organizer = User(
                email = _lecturerEmail.value.toString(),
            ),
            lecturer = User(
                name = _lecturerName.value.toString(),
                email = _lecturerEmail.value.toString()
            ),
            lectureStatus = LectureStatus(statusName = "")
        )

        viewModelScope.launch {
            val result: Result<Lecture?> = lectureRepo.saveLecture(lecture = newLecture)
            _lectureSaveResult.value = result
            when (result) {
                is Result.Success -> {
                    _savedLectureId.value = result.data?.lectureId.toString()
                    lectureInfoUiState.loadLecture((result as Result.Success<Lecture>).data)
                    reloadLectureList()
                }

                else -> {
                    reloadLectureList()
                }
            }

        }
    }

    private fun reloadLectureList() {
        viewModelScope.launch {
            val result: Result<List<Lecture>> = lectureRepo.findLectureList()
            if (result is Result.Success) {
                lectureListUiState.loadLectureList((result).data)
            }
        }

        viewModelScope.launch {
            val result: Result<List<Lecture>> = lectureRepo.findTodaysLectureList()
            if (result is Result.Success) {
                todaysLectureListUiState.loadLectureList((result).data)
            }
        }
    }

    private fun convertDateToSqlFormat(myDate: String): String {
        val selectedDateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        val sqlDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val selectedDate: Date = selectedDateFormat.parse(myDate)
        return sqlDateFormat.format(selectedDate)
    }

    private fun convertTimeToSqlFormat(myTime: String): String {
        val selectedTimeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val sqlTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        val selectedTime: Date = selectedTimeFormat.parse(myTime)
        return sqlTimeFormat.format(selectedTime)
    }

    fun resetLectureSaveResult() {
        _lectureSaveResult.value = null
    }
}