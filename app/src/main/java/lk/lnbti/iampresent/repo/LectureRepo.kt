package lk.lnbti.iampresent.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lk.lnbti.iampresent.dao.LectureDao
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.data.Result
import java.io.IOException
import javax.inject.Inject

/**
 * Repository class for handling operations related to lectures.
 *
 * @property lectureDao Data Access Object for lectures.
 */
class LectureRepo @Inject constructor(private val lectureDao: LectureDao) {
    /**
     * Retrieves a list of all lectures.
     *
     * @return Result object containing either a list of lectures on success or an error message on failure.
     */
    suspend fun findLectureList(): Result<List<Lecture>> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val response = lectureDao.findLectureList()
                if (response.isSuccessful) {
                    Result.Success(response.body() ?: emptyList())
                } else {
                    Result.Error("Failed to fetch lecture list")
                }
            } catch (e: IOException) {
                Result.Error("Network error: ${e.message}")
            }
        }
    }

    /**
     * Retrieves a list of lectures scheduled for today.
     *
     * @return Result object containing either a list of lectures on success or an error message on failure.
     */
    suspend fun findTodaysLectureList(): Result<List<Lecture>> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val response = lectureDao.findTodaysLectureList()
                if (response.isSuccessful) {
                    Result.Success(response.body() ?: emptyList())
                } else {
                    Result.Error("Failed to fetch lecture list")
                }
            } catch (e: IOException) {
                Result.Error("Network error: ${e.message}")
            }
        }
    }

    /**
     * Saves a new lecture.
     *
     * @param lecture The lecture to be saved.
     * @return Result object containing either the saved lecture on success or an error message on failure.
     */
    suspend fun saveLecture(lecture: Lecture): Result<Lecture?> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val response = lectureDao.saveLecture(lecture)
                if (response.isSuccessful) {
                    Result.Success(response.body() ?: null)
                } else {
                    Result.Error("Failed to save lecture")
                }
            } catch (e: IOException) {
                Result.Error("Network error: ${e.message}")
            }
        }
    }

    /**
     * Deactivates a lecture by its ID.
     *
     * @param lectureId The ID of the lecture to be deactivated.
     * @return Result object containing either the deactivated lecture on success or an error message on failure.
     */
    suspend fun deleteLecture(lectureId: Long): Result<Lecture?> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val response = lectureDao.deleteLectureById(lectureId)
                if (response.isSuccessful) {
                    Result.Success(response.body())
                } else {
                    Result.Error("Failed to delete lecture")
                }
            } catch (e: IOException) {
                Result.Error("Network error: ${e.message}")
            }
        }
    }

    /**
     * Retrieves a lecture by its ID.
     *
     * @param lectureId The ID of the lecture to be retrieved.
     * @return The retrieved lecture or null if not found.
     */
    suspend fun findLectureById(lectureId: String): Lecture? {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                getLecture(lectureId = lectureId)
            } catch (e: Exception) {
                null
            }
        }
    }

    private suspend fun getLecture(lectureId: String): Lecture? {
        val response = lectureDao.findLectureById(lectureId.toLong())
        lateinit var lecture: Lecture
        if (response.isSuccessful) {
            lecture = response.body()!!
        }
        return lecture
    }

    /**
     * Opens a lecture for attendance by updating its status.
     *
     * @param lectureId The ID of the lecture to be opened for attendance.
     * @return The updated lecture or null on failure.
     */
    suspend fun openLectureForAttendance(lectureId: Long): Lecture? {
        return withContext(Dispatchers.IO) {
            return@withContext updateLectureStatusToOpen(lectureId = lectureId)
        }
    }

    private suspend fun updateLectureStatusToOpen(lectureId: Long): Lecture? {
        val response = lectureDao.openLectureForAttendance(lectureId)
        lateinit var lecture: Lecture
        if (response.isSuccessful) {
            lecture = response.body()!!
        }
        return lecture
    }

    /**
     * Closes a lecture for attendance by updating its status.
     *
     * @param lectureId The ID of the lecture to be closed for attendance.
     * @return The updated lecture or null on failure.
     */
    suspend fun closeLectureForAttendance(lectureId: Long): Lecture? {
        return withContext(Dispatchers.IO) {
            return@withContext updateLectureStatusToClose(lectureId = lectureId)
        }
    }

    private suspend fun updateLectureStatusToClose(lectureId: Long): Lecture? {
        val response = lectureDao.closeLectureForAttendance(lectureId)
        lateinit var lecture: Lecture
        if (response.isSuccessful) {
            lecture = response.body()!!
        }
        return lecture
    }
}