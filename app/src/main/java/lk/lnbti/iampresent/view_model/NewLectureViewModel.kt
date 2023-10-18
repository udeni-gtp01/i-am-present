package lk.lnbti.iampresent.view_model

import android.util.Log
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
import lk.lnbti.iampresent.ui_state.LectureInfoUiState
import lk.lnbti.iampresent.ui_state.LectureListUiState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

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

     val _dialogError: MutableLiveData<String?> = MutableLiveData(null)
    val dialogError: LiveData<String?> = _dialogError

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
        _isStartDateError.value = _startDate.value.isNullOrBlank()
        if (!isStartDateError.value!!) {
            // Parse the start date to compare it with today's date
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val selectedStartDate = dateFormat.parse(convertDateToSqlFormat(_startDate.value.toString()))

            if (selectedStartDate != null) {
                // Get today's date
                val currentDate = Calendar.getInstance().time

                // Check if the selected start date is before today
                val result=selectedStartDate.before(currentDate)
                _isStartDateError.value = result
                if(result) {
                    _dialogError.value = "Start date cannot be in past."
                }
            }
        }
    }

    fun onStartTimeChange(newStartTime: String) {
        _startTime.value = newStartTime
        checkStartTimeValidation()
    }

    private fun checkStartTimeValidation() {
        _isStartTimeError.value = _startTime.value.isNullOrBlank()
        if (!isStartTimeError.value!!) {
            // Parse the start time to compare it with the current time
            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val selectedStartTime = timeFormat.parse(convertTimeToSqlFormat(_startTime.value.toString()))

            if (selectedStartTime != null) {
                // Get the current time
                val currentTime = Calendar.getInstance()
                val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
                val currentMinute = currentTime.get(Calendar.MINUTE)

                // Parse the selected start time
                val selectedHour = selectedStartTime.hours
                val selectedMinute = selectedStartTime.minutes

                // Check if the selected start time is before the current time
                val result=selectedHour < currentHour || (selectedHour == currentHour && selectedMinute <= currentMinute)
                _isStartTimeError.value = result
                if(result) {
                    _dialogError.value = "Start time cannot be in past."
                }
            }
        }
    }

    fun onEndDateChange(newEndDate: String) {
        _endDate.value = newEndDate
        checkEndDateValidation()
    }

    private fun checkEndDateValidation() {
        _isEndDateError.value = _endDate.value.isNullOrBlank()
        if (!isEndDateError.value!!){
            if (isStartDateError.value==false && isEndDateError.value==false) {
                val startDateCal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(convertDateToSqlFormat(_startDate.value.toString()))
                val endDateCal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(convertDateToSqlFormat(_endDate.value.toString()))

                if (startDateCal != null && endDateCal != null) {
                    if (endDateCal.before(startDateCal)) {
                        _dialogError.value = "End date cannot be before the start date."
                    }
                }
            }
        }
    }

    fun onEndTimeChange(newEndTime: String) {
        _endTime.value = newEndTime
        checkEndTimeValidation()
    }

    private fun checkEndTimeValidation() {
        _isEndTimeError.value = _endTime.value.isNullOrBlank()
        if (!isEndTimeError.value!!){
            if (isStartDateError.value==false && isEndDateError.value==false && isStartTimeError.value==false && isEndTimeError.value==false) {
                val startDateCal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(convertDateToSqlFormat(_startDate.value.toString()))
                val endDateCal = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(convertDateToSqlFormat(_endDate.value.toString()))
                val startTimeCal = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(convertTimeToSqlFormat(_startTime.value.toString()))
                val endTimeCal = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(convertTimeToSqlFormat(_endTime.value.toString()))

                if (startDateCal != null && endDateCal != null && startTimeCal != null && endTimeCal != null) {
                    if (endDateCal == Date() && endTimeCal.before(startTimeCal)) {
                        _dialogError.value = "End time cannot be before the start time."
                    }
                }
            }
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

    private fun checkLecturerEmailValidation() {
        _isLecturerEmailError.value = _lecturerEmail.value.isNullOrBlank()
        if (!isLecturerEmailError.value!!) {
            val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
            val result=lecturerEmail.value?.let { emailPattern.matches(it) }
            _isLecturerEmailError.value=result
            result?.let {
                if (!result) {
                    _dialogError.value = "Please enter valid email."

                }
            }

        }
    }

    fun checkAllValidation() {
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
                name = "admin",
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
                    _savedLectureId.value=result.data?.lectureId.toString()
                    lectureInfoUiState.loadLecture((result as Result.Success<Lecture>).data)
                    reloadLectureList()
                }
                else -> {reloadLectureList()}
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
    fun resetLectureSaveResult(){
        _lectureSaveResult.value=null
    }
    fun resetdialogError(){
        _dialogError.value=null
    }
}