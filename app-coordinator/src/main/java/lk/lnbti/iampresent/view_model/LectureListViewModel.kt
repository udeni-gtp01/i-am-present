package lk.lnbti.iampresent.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import lk.lnbti.iampresent.data.Contact
import lk.lnbti.iampresent.data.ContactData

class LectureListViewModel : ViewModel() {
    private val _lectureList: MutableLiveData<List<Contact>> = MutableLiveData(emptyList<Contact>())
    val lectureList: LiveData<List<Contact>> = _lectureList

    init {
        setContacts()
    }

    fun setContacts() {
        _lectureList.value = ContactData.contacts
    }

}