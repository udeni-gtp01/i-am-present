package lk.lnbti.iampresent.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lk.lnbti.iampresent.data.Attendance
import lk.lnbti.iampresent.data.Result
import lk.lnbti.iampresent.repo.AttendanceRepo
import javax.inject.Inject

/**
 * ViewModel class for managing the UI-related data of the Attendance List screen.
 *
 * This class is annotated with @HiltViewModel to enable Dagger Hilt for dependency injection.
 *
 * @property attendanceRepo The repository responsible for handling attendance data.
 * @property attendanceListUiState The UI state for the Attendance List.
 */
@HiltViewModel
class AttendanceListViewModel @Inject constructor(
    private val attendanceRepo: AttendanceRepo,
    private val attendanceListUiState: AttendanceListUiState
) : ViewModel() {

    /**
     * LiveData representing the list of lectures to be observed by the UI.
     */
    private val attendanceList: LiveData<List<Attendance>> = attendanceListUiState.attendanceList

    // LiveData to hold the grouped attendance list based on specific criteria.
    private val _groupedAttendanceList = MutableLiveData<Map<String, List<Attendance>>>()
    val groupedAttendanceList: LiveData<Map<String, List<Attendance>>> = _groupedAttendanceList

    /**
     * Initializes the ViewModel by loading the initial lecture list.
     */
    init {
        findAttendanceList()
    }

    /**
     * Asynchronously retrieves the lecture list from the repository and updates the UI state.
     */
    private fun findAttendanceList() {
        viewModelScope.launch {
            val result = attendanceRepo.getAttendanceList()
            if (result is Result.Success) {
                attendanceListUiState.loadAttendanceList(result.data)
                groupAttendanceListByDate()
            }
        }
    }

    /**
     * Group the attendance list by lecture date.
     */
    fun groupAttendanceListByDate() {
        _groupedAttendanceList.value =
            attendanceList.value?.sortedByDescending { it.lecture.startDate }?.groupBy { it.lecture.startDate }
    }

    /**
     * Group the attendance list by lecture status.
     */
    fun groupAttendanceListByLectureStatus() {
        _groupedAttendanceList.value =
            attendanceList.value?.sortedByDescending { it.lecture.lectureStatus.statusName }
                ?.groupBy { it.lecture.lectureStatus.statusName }
    }

    /**
     * Group the attendance list by subject.
     */
    fun groupAttendanceListBySubject() {
        _groupedAttendanceList.value =
            attendanceList.value?.sortedByDescending { it.lecture.subject }?.groupBy { it.lecture.subject }
    }

    /**
     * Group the attendance list by lecturer.
     */
    fun groupAttendanceListByLecturer() {
        _groupedAttendanceList.value =
            attendanceList.value?.sortedByDescending { it.lecture.lecturer.name }?.groupBy { it.lecture.lecturer.name }
    }

    /**
     * Group the attendance list by lecture location.
     */
    fun groupAttendanceListByLectureLocation() {
        _groupedAttendanceList.value =
            attendanceList.value?.sortedByDescending { it.lecture.location }?.groupBy { it.lecture.location }
    }
}

/**
 * UI state class for managing the list of attendance data to be displayed on the UI.
 */
class AttendanceListUiState {
    /**
     * MutableLiveData holding the list of attendance data.
     * This property is private to ensure that it can only be modified within this class.
     */
    private val _attendanceList: MutableLiveData<List<Attendance>> = MutableLiveData(emptyList())

    /**
     * LiveData representing the list of attendance data to be observed by the UI.
     */
    val attendanceList: LiveData<List<Attendance>> = _attendanceList

    /**
     * Updates the internal MutableLiveData with the provided list of attendance data.
     *
     * @param attendanceList The list of attendance data to be displayed on the UI.
     */
    fun loadAttendanceList(attendanceList: List<Attendance>) {
        _attendanceList.value = attendanceList
    }
}