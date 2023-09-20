package lk.lnbti.iampresent.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.repo.LectureRepo
import lk.lnbti.iampresent.ui_state.LectureListUiState
import javax.inject.Inject

@HiltViewModel
class LectureListViewModel @Inject constructor(
    private val lectureRepo: LectureRepo,
    private val lectureListUiState: LectureListUiState
) : ViewModel() {

    val lectureList: LiveData<List<Lecture>> = lectureListUiState.lectureList

    init {
        findLectureList()
    }

    fun findLectureList() {
        viewModelScope.launch {
            lectureListUiState.loadLectureList(lectureRepo.findLectureList())
        }
    }
}