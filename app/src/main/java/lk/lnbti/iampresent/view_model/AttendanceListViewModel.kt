package lk.lnbti.iampresent.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lk.lnbti.iampresent.data.Attendance
import lk.lnbti.iampresent.repo.AttendanceRepo
import javax.inject.Inject

@HiltViewModel
class AttendanceListViewModel @Inject constructor(
    private val attendanceRepo: AttendanceRepo,
    private val attendanceListUiState: AttendanceListUiState
) : ViewModel() {

    //       val lectureList: LiveData<List<Lecture>> = lectureListUiState.lectureList
    val lectureList: LiveData<List<Attendance>> = attendanceListUiState.attendanceList

    init {
        findLectureList()
    }

    fun findLectureList() {
        viewModelScope.launch {
            //lectureListUiState.loadLectureList(attendanceRepo.getAttendanceList())
        }
    }
}

class AttendanceListUiState {
    private val _attendanceList: MutableLiveData<List<Attendance>> = MutableLiveData(emptyList())
    val attendanceList: LiveData<List<Attendance>> = _attendanceList

    fun loadAttendanceList(attendanceList: List<Attendance>) {
        _attendanceList.value = attendanceList
    }
}