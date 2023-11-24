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
 * ViewModel class for managing the list of lectures. Uses Hilt for dependency injection.
 *
 * @property lectureRepo The repository for accessing lecture data.
 * @property lectureListUiState The UI state for the list of lectures.
 */
@HiltViewModel
class LectureListViewModel @Inject constructor(
    private val lectureRepo: LectureRepo,
    private val lectureListUiState: LectureListUiState
) : ViewModel() {

    // LiveData to hold the result of the lecture list retrieval.
    private val _lectureListResult = MutableLiveData<Result<List<Lecture>>>()
    val lectureListResult: LiveData<Result<List<Lecture>>> = _lectureListResult

    // LiveData to observe the current list of lectures from the UI state.
    private val lectureList: LiveData<List<Lecture>> = lectureListUiState.lectureList

    // LiveData to hold the grouped lecture list based on specific criteria.
    private val _groupedLectureList = MutableLiveData<Map<String, List<Lecture>>>()
    val groupedLectureList: LiveData<Map<String, List<Lecture>>> = _groupedLectureList

    /**
     * Initialization block to trigger the retrieval of the lecture list.
     */
    init {
        findLectureList()
    }

    /**
     * Function to find and load the lecture list asynchronously.
     */
    fun findLectureList() {
        _lectureListResult.value = Result.Loading
        viewModelScope.launch {
            val result: Result<List<Lecture>> = lectureRepo.findLectureList()
            _lectureListResult.value = result
            when (result) {
                is Result.Success -> {
                    lectureListUiState.loadLectureList(result.data)
                    groupLectureListByStartDate()
                }

                else -> {
                    lectureListUiState.loadLectureList(emptyList())
                }
            }
        }
    }

    /**
     * Group the lecture list by start date.
     */
    fun groupLectureListByStartDate() {
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
     * Group the lecture list by lecturer.
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
 * UI state class for managing the list of lectures.
 */
class LectureListUiState {
    // LiveData to hold the current list of lectures.
    private val _lectureList: MutableLiveData<List<Lecture>> = MutableLiveData(emptyList())
    val lectureList: LiveData<List<Lecture>> = _lectureList

    /**
     * Function to load a new list of lectures into the UI state.
     *
     * @param lectureList The list of lectures to be loaded.
     */
    fun loadLectureList(lectureList: List<Lecture>) {
        _lectureList.value = lectureList
    }
}