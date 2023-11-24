package lk.lnbti.iampresent.dao

import lk.lnbti.iampresent.constant.Constant
import lk.lnbti.iampresent.data.Attendance
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


/**
 * Interface for handling attendance-related operations.
 */
interface AttendanceDao {

    /**
     * Retrieves a list of all attendance records.
     *
     * @return A [Response] containing a list of [Attendance] objects.
     */
    @GET(Constant.ENDPOINT_ATTENDANCE_ALL)
    suspend fun findAttendanceList(): Response<List<Attendance>>

    /**
     * Saves a new attendance record.
     *
     * @param attendance The [Attendance] object to be saved.
     * @return A [Response] containing the saved [Attendance] object or null if unsuccessful.
     */
    @POST(Constant.ENDPOINT_ATTENDANCE_SAVE)
    suspend fun saveAttendance(@Body attendance: Attendance): Response<Attendance?>
}