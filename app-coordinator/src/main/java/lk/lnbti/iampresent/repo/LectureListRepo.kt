package lk.lnbti.iampresent.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.service.LectureListService
import retrofit2.Call
import retrofit2.Callback
import javax.inject.Inject

class LectureListRepo @Inject constructor(private val lectureListService: LectureListService) {

        suspend fun searchLectureList(): List<Lecture>? {
            return withContext(Dispatchers.IO) {
                return@withContext getLectureList()
            }
        }
        suspend fun saveLecture(lecture: Lecture): Lecture? {
            return withContext(Dispatchers.IO) {
                return@withContext setLecture(lecture = lecture)
            }
        }
        private suspend fun getLectureList(): List<Lecture>? {
            var lectureList: List<Lecture>? = null
            val response = lectureListService.searchLectureList()
            if (response.isSuccessful) {
                lectureList = response.body()
            }
            return lectureList
        }
    private suspend fun setLecture(lecture: Lecture): Lecture? {

        val response = lectureListService.saveLecture(lecture)
        var lecturenew: Lecture? = null
        if (response!!.isSuccessful) {
            lecturenew = response.body()
        }
        return lecturenew
    }
}