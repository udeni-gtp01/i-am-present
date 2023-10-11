package lk.lnbti.iampresent.ui_state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import lk.lnbti.app_student.data.Attendance

class LectureListUiState {
    private val _lectureList: MutableLiveData<List<Attendance>> = MutableLiveData(emptyList())
    val lectureList: LiveData<List<Attendance>> = _lectureList

    fun loadLectureList(lectureList: List<Attendance>) {
        _lectureList.value = lectureList
    }
}