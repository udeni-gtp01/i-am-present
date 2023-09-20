package lk.lnbti.iampresent.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.repo.LectureRepo
import lk.lnbti.iampresent.ui_state.LectureInfoUiState
import javax.inject.Inject

@HiltViewModel
class LectureInfoViewModel @Inject constructor(
    private val lectureRepo: LectureRepo,
    private val lectureInfoUiState: LectureInfoUiState
) : ViewModel() {

    val lecture: LiveData<Lecture> = lectureInfoUiState.lecture

    private val _qrtext: MutableLiveData<String> = MutableLiveData("")
    val qrText: LiveData<String> = _qrtext

    fun setQrText() {
        _qrtext.value = lecture.value?.lectureId.toString()
    }

    fun openForAttendance(lectureId: Int) {
        viewModelScope.launch {
            val lecture: Lecture? = lectureRepo.openLectureForAttendance(lectureId)
            Log.d("oyasumi", "opening for attendance 2 : ${lecture?.lectureId}")
            lecture?.let { lectureInfoUiState.loadLecture(it) }
        }
    }

    fun findLecture(lectureId: String) {
        viewModelScope.launch {
            lectureInfoUiState.loadLecture(lectureRepo.findLectureById(lectureId))
        }
    }
}
