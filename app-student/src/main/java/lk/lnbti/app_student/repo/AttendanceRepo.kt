package lk.lnbti.app_student.repo

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lk.lnbti.app_student.data.Attendance
import lk.lnbti.app_student.service.AttendanceService
import javax.inject.Inject

class AttendanceRepo@Inject constructor(private val attendanceService: AttendanceService) {
    suspend fun getAttendanceList(): List<Attendance> {
        var attendanceList: List<Attendance> = emptyList()
        val response = attendanceService.findAttendanceList()
        if (response.isSuccessful) {
            response.body()?.let {
                attendanceList = response.body()!!
            }
        }
        return attendanceList
    }

    suspend fun saveAttendance(attendance: Attendance): Attendance? {
        return withContext(Dispatchers.IO) {
            return@withContext setAttendance(attendance = attendance)
        }
    }
    private suspend fun setAttendance(attendance: Attendance): Attendance? {

        val response = attendanceService.saveAttendance(attendance)
        var attendancenew: Attendance? = null
        if (response!!.isSuccessful) {
            attendancenew = response.body()
            Log.d("oyasumi","response on save: ${response.body().toString()}")
        }
        return attendancenew
    }
}