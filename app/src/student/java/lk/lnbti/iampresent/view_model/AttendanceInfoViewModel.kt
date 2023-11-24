package lk.lnbti.app_student.view_model

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
import lk.lnbti.iampresent.constant.Constant
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.repo.LectureRepo
import java.util.Calendar
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

/**
 * ViewModel class for managing attendance information. Uses Hilt for dependency injection.
 *
 * @param lectureRepo Repository for accessing lecture data.
 * @param attendanceInfoUiState UI state for managing attendance information.
 */
@HiltViewModel
class AttendanceInfoViewModel @Inject constructor(
    private val lectureRepo: LectureRepo,
    private val attendanceInfoUiState: AttendanceInfoUiState
) : ViewModel() {
    // LiveData for the current lecture information.
    val lecture: LiveData<Lecture> = attendanceInfoUiState.lecture

    // Coroutine job for updating QR code text.
    private var qrCoroutine: Job? = null

    // MutableLiveData for holding the QR code text.
    private val _qrtext: MutableLiveData<String?> = MutableLiveData(null)
    val qrText: LiveData<String?> = _qrtext

    /**
     * Finds and loads the lecture information based on the provided lecture ID.
     * If the lecture status is 'Ongoing' sets up the QR code text updates.
     *
     * @param lectureId The ID of the lecture to find.
     */
    fun findLecture(lectureId: String) {
        viewModelScope.launch {
            attendanceInfoUiState.loadLecture(lectureRepo.findLectureById(lectureId))
            if (lecture.value?.lectureStatus?.lectureStatusId == 2) {
                setQrText()
            } else {
                _qrtext.value = null
            }
        }
    }

    /**
     * Sets up the coroutine to update the QR code text periodically.
     */
    private fun setQrText() {
        qrCoroutine = qrCoroutine ?: viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                lecture.value?.let {
                    while (isActive) {
                        _qrtext.value = encrypt(generateQRText())
                        delay(10000)
                    }
                }
            }
        }
    }

    /**
     * Generates the text for the QR code based on the current lecture information.
     *
     * @return The generated QR code text.
     */
    private fun generateQRText(): String {
        var qrOriginalText = ""
        lecture.value?.let {
            val timeNow = Calendar.getInstance().timeInMillis.toString()
            qrOriginalText =
                "${timeNow}@@${it.lectureId}@@${it.batch}@@${it.subject}@@${it.location}@@${it.endDate}@@${it.endTime}"
        }
        return qrOriginalText
    }

    /**
     * Encrypts the provided string using AES encryption.
     *
     * @param stringToEncrypt The string to encrypt.
     * @return The encrypted string.
     */
    private fun encrypt(stringToEncrypt: String): String {
        val aesKey = SecretKeySpec(Constant.QR_KEY.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, aesKey)
        val encryptedText = cipher.doFinal(stringToEncrypt.toByteArray(charset = Charsets.UTF_8))
        return java.util.Base64.getEncoder().encodeToString(encryptedText)
    }

    /**
     * Decrypts the provided data using AES decryption.
     *
     * @param dataToDecrypt The data to decrypt.
     * @return The decrypted string.
     */
    private fun decrypt(dataToDecrypt: String): String {
        val aesKey = SecretKeySpec(Constant.QR_KEY.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, aesKey)
        val decryptedText = java.util.Base64.getDecoder().decode(dataToDecrypt)
        return String(cipher.doFinal(decryptedText), charset = Charsets.UTF_8)
    }
}

class AttendanceInfoUiState {
    /**
     * MutableLiveData object for holding the current lecture information.
     */
    private val _lecture: MutableLiveData<Lecture> = MutableLiveData(null)
    val lecture: LiveData<Lecture> = _lecture

    /**
     * Loads the provided lecture into the UI state.
     *
     * @param lecture The lecture to load into the UI state.
     */
    fun loadLecture(lecture: Lecture?) {
        _lecture.value = lecture
    }
}
