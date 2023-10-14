package lk.lnbti.iampresent.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lk.lnbti.iampresent.dao.AttendanceDao
import lk.lnbti.iampresent.data.Attendance
import lk.lnbti.iampresent.data.Result
import java.io.IOException
import javax.inject.Inject

class AttendanceRepo @Inject constructor(private val attendanceDao: AttendanceDao) {
    suspend fun getAttendanceList(): Result<List<Attendance>> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val response = attendanceDao.findAttendanceList()
                if (response.isSuccessful) {
                    Result.Success(response.body() ?: emptyList())
                } else {
                    Result.Error("Failed to fetch attendance list")
                }
            } catch (e: IOException) {
                Result.Error("Network error: ${e.message}")
            }
        }
    }
}