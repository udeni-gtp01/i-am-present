package lk.lnbti.iampresent.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewLectureViewModel : ViewModel() {
    private val _batch: MutableLiveData<String> = MutableLiveData("")
    val batch: LiveData<String> = _batch
    var isBatchError:Boolean=false
    fun onBatchChange(newBatch: String) {
        _batch.value = newBatch
    }
    fun checkFieldValidation(){
        isBatchError=_batch.value.isNullOrBlank()
    }
}