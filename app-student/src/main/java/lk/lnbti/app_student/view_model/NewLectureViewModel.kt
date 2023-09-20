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
import lk.lnbti.iampresent.data.User
import lk.lnbti.iampresent.repo.LectureRepo
import lk.lnbti.iampresent.ui_state.LectureListUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class NewLectureViewModel @Inject constructor(
    private val lectureListUiState: LectureListUiState,
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
    }

    fun onStartTimeChange(newStartTime: String) {
        _startTime.value = newStartTime
        checkStartTimeValidation()
    }

    private fun checkStartTimeValidation() {
        _isStartTimeError.value = _startTime.value.isNullOrBlank()
    }

    fun onEndDateChange(newEndDate: String) {
        _endDate.value = newEndDate
        checkEndDateValidation()
    }

    private fun checkEndDateValidation() {
        _isEndDateError.value = _endDate.value.isNullOrBlank()
    }

    fun onEndTimeChange(newEndTime: String) {
        _endTime.value = newEndTime
        checkEndTimeValidation()
    }

    private fun checkEndTimeValidation() {
        _isEndTimeError.value = _endTime.value.isNullOrBlank()
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

    fun saveLecture(): String {
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
        val respose = ""
        viewModelScope.launch {
            Log.d("oyasumi", "Subject says: " + newLecture.subject)

            val respose = lectureRepo.saveLecture(lecture = newLecture)
            findLectureList()
            Log.d("oyasumi", "Saved lecture says: " + respose)
        }
        return respose
    }

    fun findLectureList() {
        viewModelScope.launch {
            lectureListUiState.loadLectureList(lectureRepo.findLectureList())
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
}