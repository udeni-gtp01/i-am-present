package lk.lnbti.iampresent.view_model

import androidx.compose.material3.Text
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.repo.LectureRepo
import lk.lnbti.iampresent.ui_state.LectureListUiState
import javax.inject.Inject
import lk.lnbti.iampresent.data.Result

@HiltViewModel
class LectureListViewModel @Inject constructor(
    private val lectureRepo: LectureRepo,
    private val lectureListUiState: LectureListUiState
) : ViewModel() {

    private val _lectureListResult = MutableLiveData<Result<List<Lecture>>>()
    val lectureListResult: LiveData<Result<List<Lecture>>> = _lectureListResult

    val lectureList: LiveData<List<Lecture>> = lectureListUiState.lectureList

    init {
        findLectureList()
    }

     fun findLectureList() {
        _lectureListResult.value = Result.Loading
        viewModelScope.launch {
            val result:Result<List<Lecture>> = lectureRepo.findLectureList()
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