package lk.lnbti.iampresent.service

import lk.lnbti.iampresent.constant.Constant
import lk.lnbti.iampresent.data.Lecture
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LectureListService {
    //@Headers(Constant.HEADER_1)
    @GET(Constant.ENDPOINT_LECTURE_ALL)
    suspend fun searchLectureList(): Response<List<Lecture>>

    @POST(Constant.ENDPOINT_LECTURE_SAVE)
    suspend fun saveLecture(@Body lecture: Lecture):Response<Lecture?>?
}