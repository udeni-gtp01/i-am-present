package lk.lnbti.iampresent.dao

import lk.lnbti.iampresent.constant.Constant
import lk.lnbti.iampresent.data.Lecture
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
/**
 * Interface for managing lectures.
 */
interface LectureDao {
    /**
     * Retrieves a list of all lectures.
     * @return A [Response] containing a list of [Lecture] objects.
     */
    @GET(Constant.ENDPOINT_LECTURE_ALL)
    suspend fun findLectureList(): Response<List<Lecture>>

    /**
     * Retrieves a list of today's lectures.
     * @return A [Response] containing a list of [Lecture] objects scheduled for today.
     */
    @GET(Constant.ENDPOINT_LECTURE_ALL_TODAY)
    suspend fun findTodaysLectureList(): Response<List<Lecture>>

    /**
     * Saves a new lecture.
     * @param lecture The [Lecture] object to be saved.
     * @return A [Response] containing the saved [Lecture] object.
     */
    @POST(Constant.ENDPOINT_LECTURE_SAVE)
    suspend fun saveLecture(@Body lecture: Lecture): Response<Lecture>

    /**
     * Retrieves a specific lecture by its ID.
     * @param id The ID of the lecture to be retrieved.
     * @return A [Response] containing the [Lecture] object with the specified ID.
     */
    @GET(Constant.ENDPOINT_LECTURE_FIND)
    suspend fun findLectureById(@Path(value = "id") id: Long): Response<Lecture>

    /**
     * Deletes a specific lecture by its ID.
     * @param id The ID of the lecture to be deleted.
     * @return A [Response] containing the deleted [Lecture] object.
     */
    @DELETE(Constant.ENDPOINT_LECTURE_DELETE)
    suspend fun deleteLectureById(@Path(value = "id") id: Long): Response<Lecture>

    /**
     * Opens a lecture for attendance by updating its status.
     * @param id The ID of the lecture to be opened for attendance.
     * @return A [Response] containing the updated [Lecture] object.
     */
    @PUT(Constant.ENDPOINT_LECTURE_OPEN_FOR_ATTENDANCE)
    suspend fun openLectureForAttendance(@Path(value = "id") id: Long): Response<Lecture>

    /**
     * Closes a lecture for attendance by updating its status.
     * @param id The ID of the lecture to be closed for attendance.
     * @return A [Response] containing the updated [Lecture] object.
     */
    @PUT(Constant.ENDPOINT_LECTURE_CLOSE_FOR_ATTENDANCE)
    suspend fun closeLectureForAttendance(@Path(value = "id") id: Long): Response<Lecture>
}