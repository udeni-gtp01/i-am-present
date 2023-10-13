package lk.lnbti.iampresent.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lk.lnbti.app_student.data.Lecture
import lk.lnbti.iampresent.dao.LectureService
import javax.inject.Inject

class LectureRepo @Inject constructor(private val lectureService: LectureService) {
    suspend fun findLectureList(): List<Lecture> {
        return withContext(Dispatchers.IO) {
            return@withContext getLectureList()
        }
    }

    private suspend fun getLectureList(): List<Lecture> {
        var lectureList: List<Lecture> = emptyList()
        val response = lectureService.findLectureList()
        if (response.isSuccessful) {
            response.body()?.let {
                lectureList = response.body()!!
            }
        }
        return lectureList
    }

    suspend fun saveLecture(lecture: Lecture): Lecture? {
        return withContext(Dispatchers.IO) {
            return@withContext setLecture(lecture = lecture)
        }
    }

    private suspend fun setLecture(lecture: Lecture): Lecture? {

        val response = lectureService.saveLecture(lecture)
        var lecturenew: Lecture? = null
        if (response!!.isSuccessful) {
            lecturenew = response.body()
        }
        return lecturenew
    }

    suspend fun findLectureById(lectureId: String): Lecture {
        return withContext(Dispatchers.IO) {
            return@withContext getLecture(lectureId = lectureId)
        }
    }

    private suspend fun getLecture(lectureId: String): Lecture {
        val response = lectureService.findLectureById(lectureId.toInt())
        lateinit var lecture: Lecture
        if (response.isSuccessful) {
            lecture = response.body()!!
        }
        return lecture
    }

    suspend fun openLectureForAttendance(lectureId: Int): Lecture? {
        return withContext(Dispatchers.IO) {
            return@withContext updateLectureStatus(lectureId = lectureId)
        }
    }

    private suspend fun updateLectureStatus(lectureId: Int): Lecture? {
        val response = lectureService.openLectureForAttendance(lectureId)
        lateinit var lecture: Lecture
        if (response.isSuccessful) {
            lecture = response.body()!!
        }
        return lecture
    }
}