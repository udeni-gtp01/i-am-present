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
import javax.inject.Inject

@HiltViewModel
class NewLectureViewModel @Inject constructor(
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
        _isBatchError.value = _batch.value.isNullOrBlank()
    }

    fun onSemesterChange(newSemester: String) {
        _semester.value = newSemester
        _isSemesterError.value = _semester.value.isNullOrBlank()
    }

    fun onSubjectChange(newSubject: String) {
        _subject.value = newSubject
        _isSubjectError.value = _subject.value.isNullOrBlank()
    }

    fun onLocationChange(newLocation: String) {
        _location.value = newLocation
        _isLocationError.value = _location.value.isNullOrBlank()
    }

    fun onStartDateChange(newStartDate: String) {
        _startDate.value = newStartDate
        _isStartDateError.value = _startDate.value.isNullOrBlank()
    }

    fun onStartTimeChange(newStartTime: String) {
        _startTime.value = newStartTime
        _isStartTimeError.value = _startTime.value.isNullOrBlank()
    }

    fun onEndDateChange(newEndDate: String) {
        _endDate.value = newEndDate
        _isEndDateError.value = _endDate.value.isNullOrBlank()
    }

    fun onEndTimeChange(newEndTime: String) {
        _endTime.value = newEndTime
        _isEndTimeError.value = _endTime.value.isNullOrBlank()
    }

    fun onLecturerNameChange(newLecturerName: String) {
        _lecturerName.value = newLecturerName
        _isLecturerNameError.value = _lecturerName.value.isNullOrBlank()
    }

    fun onLecturerEmailChange(newLecturerEmail: String) {
        _lecturerEmail.value = newLecturerEmail
        _isLecturerEmailError.value = _lecturerEmail.value.isNullOrBlank()
    }
    fun isValidationSuccess(): Boolean {
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
            semester = _semester.value.toString(),
            subject = _subject.value.toString(),
            location = _location.value.toString(),
            startDate = _startDate.value.toString(),
            startTime = _startTime.value.toString(),
            endDate = _endDate.value.toString(),
            endTime = _endTime.value.toString(),
            organizer = User(
                name = "admin",
            ),
            lecturer = User(
                name = _lecturerName.value.toString(),
                email = _lecturerEmail.value.toString()
            ),
            lectureStatus = LectureStatus(statusName = "")
        )
        val respose=""
        viewModelScope.launch {
            val respose = lectureRepo.saveLecture(lecture = newLecture)
            Log.d("oyasumi", "Saved lecture says: " + respose)
        }
        return respose
    }
}