package lk.lnbti.iampresent.dao

import lk.lnbti.iampresent.constant.Constant
import lk.lnbti.iampresent.data.Attendance
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AttendanceDao {
    @GET(Constant.ENDPOINT_ATTENDANCE_ALL)
    suspend fun findAttendanceList(): Response<List<Attendance>>

    @POST(Constant.ENDPOINT_ATTENDANCE_SAVE)
    suspend fun saveAttendance(@Body attendance: Attendance): Response<Attendance?>
}