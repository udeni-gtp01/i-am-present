package lk.lnbti.iampresent.view_model

import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lk.lnbti.iampresent.data.Attendance
import lk.lnbti.iampresent.data.Result
import lk.lnbti.iampresent.repo.AttendanceRepo
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

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

    fun generateExcelReport(data: List<Attendance>) {
        viewModelScope.launch(Dispatchers.Default) {
            if (data != null) {
                val downloadsDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val downloadsFolderPath = downloadsDir.absolutePath
                val csv = File(downloadsFolderPath, "test1.csv")
                try {
                    var fileOutputStream = FileOutputStream(csv).apply { writeCsv(data) }
                    fileOutputStream.close()
                    fileOutputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun OutputStream.writeCsv(data: List<Attendance>) {
        val writer = bufferedWriter()
        writer.write(""""Batch", "Semester", "Subject", "Date", "Time", "Student email"""")
        writer.newLine()
        data.forEach {
            writer.write("\"${it.lecture.batch}\", \"${it.lecture.semester}\", \"${it.lecture.subject}\",\"${it.lecture.startDate}\",\"${it.lecture.startTime}\",\"${it.student.email}\"")
            writer.newLine()
        }
        writer.flush()
    }
}
