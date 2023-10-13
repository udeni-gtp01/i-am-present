package lk.lnbti.iampresent.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.data.Result
import lk.lnbti.iampresent.repo.LectureRepo
import lk.lnbti.iampresent.ui_state.LectureListUiState
import javax.inject.Inject

@HiltViewModel
class TodaysLectureListViewModel @Inject constructor(
    private val lectureRepo: LectureRepo,
    private val lectureListUiState: TodaysLectureListUiState
) : ViewModel() {
    private val _lectureListResult = MutableLiveData<Result<List<Lecture>>>()
    val lectureListResult: LiveData<Result<List<Lecture>>> = _lectureListResult

    val lectureList: LiveData<List<Lecture>> = lectureListUiState.lectureList

    init {
        findTodaysLectureList()
    }

    fun findTodaysLectureList() {
        _lectureListResult.value = Result.Loading
        viewModelScope.launch {
            val result: Result<List<Lecture>> = lectureRepo.findTodaysLectureList()
            _lectureListResult.value = result
            when (result) {
                is Result.Success -> {
                    lectureListUiState.loadLectureList((result).data)
                }
                else -> {lectureListUiState.loadLectureList(emptyList())}
            }
        }
    }
}
class TodaysLectureListUiState {
    private val _lectureList: MutableLiveData<List<Lecture>> = MutableLiveData(emptyList())
    val lectureList: LiveData<List<Lecture>> = _lectureList

    fun loadLectureList(lectureList: List<Lecture>) {
        _lectureList.value = lectureList
    }
}