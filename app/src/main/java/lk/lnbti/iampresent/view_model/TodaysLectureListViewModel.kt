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
import javax.inject.Inject

/**
 * ViewModel class for managing today's lecture list. Uses Hilt for dependency injection.
 *
 * @param lectureRepo Repository for accessing lecture data.
 * @param lectureListUiState UI state for managing lecture list data.
 */
@HiltViewModel
class TodaysLectureListViewModel @Inject constructor(
    private val lectureRepo: LectureRepo,
    private val lectureListUiState: TodaysLectureListUiState
) : ViewModel() {
    private val _lectureListResult = MutableLiveData<Result<List<Lecture>>>()
    val lectureListResult: LiveData<Result<List<Lecture>>> = _lectureListResult

    val lectureList: LiveData<List<Lecture>> = lectureListUiState.lectureList

    private val _groupedLectureList = MutableLiveData<Map<String, List<Lecture>>>()
    val groupedLectureList: LiveData<Map<String, List<Lecture>>> = _groupedLectureList

    /**
     * Initializes the ViewModel and triggers the retrieval of today's lecture list.
     */
    init {
        findTodaysLectureList()
    }

    /**
     * Retrieves today's lecture list from the repository.
     */
    fun findTodaysLectureList() {
        _lectureListResult.value = Result.Loading
        viewModelScope.launch {
            val result: Result<List<Lecture>> = lectureRepo.findTodaysLectureList()
            _lectureListResult.value = result
            when (result) {
                is Result.Success -> {
                    lectureListUiState.loadLectureList((result).data)
                }

                else -> {
                    lectureListUiState.loadLectureList(emptyList())
                }
            }
        }
    }

    /**
     * Group the lecture list by start time.
     */
    fun groupLectureListByStartTime() {
        _groupedLectureList.value =
            lectureList.value?.sortedByDescending { it.startDate }?.groupBy { it.startDate }
    }

    /**
     * Group the lecture list by lecture status.
     */
    fun groupLectureListByLectureStatus() {
        _groupedLectureList.value =
            lectureList.value?.sortedByDescending { it.lectureStatus.statusName }
                ?.groupBy { it.lectureStatus.statusName }
    }

    /**
     * Group the lecture list by batch.
     */
    fun groupLectureListByBatch() {
        _groupedLectureList.value =
            lectureList.value?.sortedByDescending { it.batch }?.groupBy { it.batch }
    }

    /**
     * Group the lecture list by subject.
     */
    fun groupLectureListBySubject() {
        _groupedLectureList.value =
            lectureList.value?.sortedByDescending { it.subject }?.groupBy { it.subject }
    }

    /**
     * Group the lecture list by lecturer name.
     */
    fun groupLectureListByLecturer() {
        _groupedLectureList.value =
            lectureList.value?.sortedByDescending { it.lecturer.name }?.groupBy { it.lecturer.name }
    }

    /**
     * Group the lecture list by location.
     */
    fun groupLectureListByLocation() {
        _groupedLectureList.value =
            lectureList.value?.sortedByDescending { it.location }?.groupBy { it.location }
    }
}

/**
 * UI state class for managing the lecture list data in [TodaysLectureListViewModel].
 */
class TodaysLectureListUiState {
    private val _lectureList: MutableLiveData<List<Lecture>> = MutableLiveData(emptyList())
    val lectureList: LiveData<List<Lecture>> = _lectureList

    /**
     * Loads the provided lecture list into the UI state.
     *
     * @param lectureList The list of lectures to be loaded.
     */
    fun loadLectureList(lectureList: List<Lecture>) {
        _lectureList.value = lectureList
    }
}