package lk.lnbti.app_student.service

import lk.lnbti.app_student.data.Attendance
import lk.lnbti.iampresent.constant.Constant
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AttendanceService {
    @GET(Constant.ENDPOINT_ATTENDANCE_ALL)
    suspend fun findAttendanceList(): Response<List<Attendance>>

    @POST(Constant.ENDPOINT_ATTENDANCE_SAVE)
    suspend fun saveAttendance(@Body attendance: Attendance): Response<Attendance?>?
}