package lk.lnbti.iampresent.service

import lk.lnbti.iampresent.constant.Constant
import lk.lnbti.iampresent.data.Lecture
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface LectureService {
    //@Headers(Constant.HEADER_1)
    @GET(Constant.ENDPOINT_LECTURE_ALL)
    suspend fun findLectureList(): Response<List<Lecture>>

    @POST(Constant.ENDPOINT_LECTURE_SAVE)
    suspend fun saveLecture(@Body lecture: Lecture): Response<Lecture?>?

    @GET(Constant.ENDPOINT_LECTURE_FIND)
    suspend fun findLectureById(@Path(value = "id") id: Int): Response<Lecture>

    @PUT(Constant.ENDPOINT_LECTURE_OPEN_FOR_ATTENDANCE)
    suspend fun openLectureForAttendance(@Path(value = "id") id: Int): Response<Lecture>
}