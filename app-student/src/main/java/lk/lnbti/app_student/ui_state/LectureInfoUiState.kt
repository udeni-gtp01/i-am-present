package lk.lnbti.iampresent.ui_state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import lk.lnbti.app_student.data.Lecture

class LectureInfoUiState {
    private val _lecture: MutableLiveData<Lecture> = MutableLiveData(null)
    val lecture: LiveData<Lecture> = _lecture

    fun loadLecture(lecture: Lecture) {
        _lecture.value = lecture
    }
}