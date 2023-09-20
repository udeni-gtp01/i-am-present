package lk.lnbti.iampresent.ui_state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import lk.lnbti.iampresent.data.Lecture

class LectureListUiState {
    private val _lectureList: MutableLiveData<List<Lecture>> = MutableLiveData(emptyList())
    val lectureList: LiveData<List<Lecture>> = _lectureList

    fun loadLectureList(lectureList: List<Lecture>) {
        _lectureList.value = lectureList
    }
}