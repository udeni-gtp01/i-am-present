package lk.lnbti.iampresent.view_model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.repo.LectureRepo
import lk.lnbti.iampresent.ui_state.LectureInfoUiState
import java.util.Calendar
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

@HiltViewModel
class LectureInfoViewModel @Inject constructor(
    private val lectureRepo: LectureRepo,
    private val lectureInfoUiState: LectureInfoUiState
) : ViewModel() {

    val lecture: LiveData<Lecture> = lectureInfoUiState.lecture

    private var qrCoroutine: Job? = null

    private val _qrText: MutableLiveData<String?> = MutableLiveData(null)
    val qrText: LiveData<String?> = _qrText

    private val qrKey = "BYT765#76GHFaunY"
    private fun setQrText() {
        qrCoroutine = qrCoroutine?:viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                lecture.value?.let {
                    while (isActive) {
                        _qrText.value = encrypt(generateQRText())
                        Log.d("oyasumi", "decrypted- ${_qrText.value?.let { decrypt(it) }}")
                        delay(10000)
                    }
                }
            }
        }
    }

    private fun generateQRText(): String {
        var qrOriginalText = ""
        lecture.value?.let {
            var timeNow = Calendar.getInstance().timeInMillis.toString()
            qrOriginalText =
                "${timeNow}@@${it.lectureId.toString()}@@${it.batch}@@${it.subject}@@${it.location}"
            Log.d("oyasumi", "original- $qrOriginalText")
        }
        return qrOriginalText
    }

    fun openForAttendance(lectureId: Int) {
        viewModelScope.launch {
            val lecture: Lecture? = lectureRepo.openLectureForAttendance(lectureId)
            lecture?.let {
                lectureInfoUiState.loadLecture(it)
                setQrText()
            }
        }
    }

    fun closeForAttendance(lectureId: Int) {
        viewModelScope.launch {
            val lecture: Lecture? = lectureRepo.closeLectureForAttendance(lectureId)
            lecture?.let {
                lectureInfoUiState.loadLecture(it)
                _qrText.value = null
            }
        }
        qrCoroutine?.cancel()
        qrCoroutine=null
    }

    fun findLecture(lectureId: String) {
        viewModelScope.launch {
            lectureInfoUiState.loadLecture(lectureRepo.findLectureById(lectureId))
            if (lecture.value?.lectureStatus?.lectureStatusId == 2) {
                setQrText()
            }
        }
    }
    fun deleteLecture(lectureId: Int) {
        Log.d("oyasumi","del lec id: $lectureId")
        viewModelScope.launch {
            lectureRepo.deleteLecture(lectureId)
        }
    }
    private fun encrypt(stringToEncrypt: String): String {
        val aesKey = SecretKeySpec(qrKey.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, aesKey)
        val encryptedText = cipher.doFinal(stringToEncrypt.toByteArray(charset = Charsets.UTF_8))
        return java.util.Base64.getEncoder().encodeToString(encryptedText)
    }

    private fun decrypt(dataToDecrypt: String): String {
        val aesKey = SecretKeySpec(qrKey.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, aesKey)
        var decryptedText = java.util.Base64.getDecoder().decode(dataToDecrypt)
        return String(cipher.doFinal(decryptedText), charset = Charsets.UTF_8)
    }
}
