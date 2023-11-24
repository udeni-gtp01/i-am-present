package lk.lnbti.iampresent.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lk.lnbti.iampresent.dao.AttendanceDao
import lk.lnbti.iampresent.data.Attendance
import lk.lnbti.iampresent.data.Result
import java.io.IOException
import javax.inject.Inject

/**
 * Repository class for managing attendance-related data operations.
 *
 * @property attendanceDao Data Access Object for attendance operations.
 */
class AttendanceRepo @Inject constructor(private val attendanceDao: AttendanceDao) {
    /**
     * Retrieves the list of attendance records in a suspend function.
     *
     * @return Result<List<Attendance>> Represents the result of the operation.
     *         - If successful, returns [Result.Success] with a list of [Attendance].
     *         - If unsuccessful, returns [Result.Error] with an error message.
     */
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

    /**
     * Saves attendance record in a suspend function.
     *
     * @param attendance Attendance object to be saved.
     * @return Result<Attendance?> Represents the result of the operation.
     *         - If successful, returns [Result.Success] with the saved [Attendance] object.
     *         - If unsuccessful, returns [Result.Error] with an error message.
     */
    suspend fun saveAttendance(attendance: Attendance): Result<Attendance?> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val response = attendanceDao.saveAttendance(attendance)
                var attendancenew: Attendance? = null
                if (response.isSuccessful) {
                    Result.Success(response.body() ?: null)
                } else {
                    Result.Error("Are you attending the lecture on time?")
                }
            } catch (e: IOException) {
                Result.Error("Network error: ${e.message}")
            }
        }
    }
}