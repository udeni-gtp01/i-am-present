package lk.lnbti.iampresent.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lk.lnbti.iampresent.data.Contact
import lk.lnbti.iampresent.data.ContactData
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.repo.LectureListRepo
import javax.inject.Inject

@HiltViewModel
class LectureListViewModel @Inject constructor(
    private val lectureListRepo: LectureListRepo
): ViewModel() {
    private val _lectureList: MutableLiveData<List<Lecture>> = MutableLiveData(emptyList<Lecture>())
    val lectureList: LiveData<List<Lecture>> = _lectureList

    init {
        searchLectureList()
    }

    fun searchLectureList() {
        viewModelScope.launch {
            _lectureList.value = lectureListRepo.searchLectureList()
        }
    }

}