package lk.lnbti.iampresent.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lk.lnbti.app_student.data.Attendance
import lk.lnbti.app_student.repo.AttendanceRepo
import lk.lnbti.iampresent.ui_state.LectureListUiState
import javax.inject.Inject

@HiltViewModel
class LectureListViewModel @Inject constructor(
    private val attendanceRepo: AttendanceRepo,
    private val lectureListUiState: LectureListUiState
) : ViewModel() {

    val lectureList: LiveData<List<Attendance>> = lectureListUiState.lectureList

    init {
        findLectureList()
    }

    fun findLectureList() {
        viewModelScope.launch {
            lectureListUiState.loadLectureList(attendanceRepo.getAttendanceList())
        }
    }
}