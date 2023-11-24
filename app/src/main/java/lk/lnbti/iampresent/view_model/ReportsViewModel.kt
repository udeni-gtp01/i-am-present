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
 * ViewModel class for managing and coordinating data related to reports.
 *
 * This class is annotated with HiltViewModel to enable dependency injection using Hilt.
 *
 * @property attendanceRepo The repository responsible for providing attendance-related data.
 * @property attendanceListUiState The UI state for managing the list of attendance items.
 */
@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val attendanceRepo: AttendanceRepo,
    private val attendanceListUiState: AttendanceListUiState
) : ViewModel() {

    private val _attendanceListResult = MutableLiveData<Result<List<Attendance>>>()
    val attendanceListResult: LiveData<Result<List<Attendance>>> = _attendanceListResult

    val attendanceList: LiveData<List<Attendance>> = attendanceListUiState.attendanceList

    init {
        findAttendanceList()
    }

    /**
     * Retrieves the attendance list from the repository.
     */
    fun findAttendanceList() {
        _attendanceListResult.value = Result.Loading
        viewModelScope.launch {
            val result: Result<List<Attendance>> = attendanceRepo.getAttendanceList()
            _attendanceListResult.value = result
            when (result) {
                is Result.Success -> {
                    attendanceListUiState.loadAttendanceList((result).data)
                }

                else -> {
                    attendanceListUiState.loadAttendanceList(emptyList())
                }
            }
        }
    }
}
