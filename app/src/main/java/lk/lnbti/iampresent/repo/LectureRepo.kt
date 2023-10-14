package lk.lnbti.iampresent.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.data.Result
import lk.lnbti.iampresent.dao.LectureDao
import java.io.IOException
import javax.inject.Inject

class LectureRepo @Inject constructor(private val lectureDao: LectureDao) {
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
    suspend fun saveLecture(lecture: Lecture): Result<Lecture?> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val response = lectureDao.saveLecture(lecture)
                if (response.isSuccessful) {
                    Result.Success(response.body()?:null)
                } else {
                    Result.Error("Failed to save lecture")
                }
            } catch (e: IOException) {
                Result.Error("Network error: ${e.message}")
            }
        }
    }

    suspend fun deleteLecture(lectureId: Int): Result<Lecture?> {
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
    suspend fun findLectureById(lectureId: String): Lecture? {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                getLecture(lectureId = lectureId)
            }catch (e:Exception){
                null
            }
        }
    }

    private suspend fun getLecture(lectureId: String): Lecture? {
        val response = lectureDao.findLectureById(lectureId.toInt())
        lateinit var lecture: Lecture
        if (response.isSuccessful) {
            lecture = response.body()!!
        }
        return lecture
    }

    suspend fun openLectureForAttendance(lectureId: Int): Lecture? {
        return withContext(Dispatchers.IO) {
            return@withContext updateLectureStatusToOpen(lectureId = lectureId)
        }
    }

    private suspend fun updateLectureStatusToOpen(lectureId: Int): Lecture? {
        val response = lectureDao.openLectureForAttendance(lectureId)
        lateinit var lecture: Lecture
        if (response.isSuccessful) {
            lecture = response.body()!!
        }
        return lecture
    }

    suspend fun closeLectureForAttendance(lectureId: Int): Lecture? {
        return withContext(Dispatchers.IO) {
            return@withContext updateLectureStatusToClose(lectureId = lectureId)
        }
    }

    private suspend fun updateLectureStatusToClose(lectureId: Int): Lecture? {
        val response = lectureDao.closeLectureForAttendance(lectureId)
        lateinit var lecture: Lecture
        if (response.isSuccessful) {
            lecture = response.body()!!
        }
        return lecture
    }
}