package lk.lnbti.iampresent.view_model

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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

/**
 * ViewModel class for managing the UI-related data for the LectureInfoFragment.
 *
 * @param lectureRepo The repository for accessing lecture-related data.
 * @param lectureInfoUiState The UI state object for storing lecture information.
 */
@HiltViewModel
class LectureInfoViewModel @Inject constructor(
    private val lectureRepo: LectureRepo,
    private val lectureInfoUiState: LectureInfoUiState
) : ViewModel() {

    /**
     * LiveData object representing the current lecture information.
     */
    val lecture: LiveData<Lecture> = lectureInfoUiState.lecture

    /**
     * Job instance for managing the coroutine responsible for updating the QR code text.
     */
    private var qrCoroutine: Job? = null

    /**
     * MutableLiveData for storing the QR code text.
     */
    private val _qrText: MutableLiveData<String?> = MutableLiveData(null)
    val qrText: LiveData<String?> = _qrText

    /**
     * Sets up the coroutine to update the QR code text periodically.
     */
    private fun setQrText() {
        qrCoroutine = qrCoroutine ?: viewModelScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                lecture.value?.let {
                    while (isActive) {
                        _qrText.value = encrypt(generateQRText())
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
     * Opens the lecture for attendance and sets up the QR code text updates.
     *
     * @param lectureId The ID of the lecture to open for attendance.
     */
    fun openForAttendance(lectureId: Long) {
        lecture.value?.startTime?.let {
            val currentLecture=lecture.value
            currentLecture?.let {
                val dateTimeFormat =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val currentDateTime = Calendar.getInstance().time
                val startDateTime = dateTimeFormat.parse("${currentLecture.startDate} ${currentLecture.startTime}")
                if (startDateTime.before(currentDateTime)) {
                    viewModelScope.launch {
                        val lecture: Lecture? = lectureRepo.openLectureForAttendance(lectureId)
                        lecture?.let {
                            lectureInfoUiState.loadLecture(it)
                            setQrText()
                        }
                    }
                }
            }
        }
}

/**
 * Closes the lecture for attendance and cancels the QR code update coroutine.
 *
 * @param lectureId The ID of the lecture to close for attendance.
 */
fun closeForAttendance(lectureId: Long) {
    viewModelScope.launch {
        val lecture: Lecture? = lectureRepo.closeLectureForAttendance(lectureId)
        lecture?.let {
            lectureInfoUiState.loadLecture(it)
            _qrText.value = null
        }
    }
    qrCoroutine?.cancel()
    qrCoroutine = null
}

/**
 * Finds and loads the lecture information based on the provided lecture ID.
 * If the lecture status is 'In Progress,' sets up the QR code text updates.
 *
 * @param lectureId The ID of the lecture to find.
 */
fun findLecture(lectureId: String) {
    viewModelScope.launch {
        lectureInfoUiState.loadLecture(lectureRepo.findLectureById(lectureId))
        if (lecture.value?.lectureStatus?.lectureStatusId == 2) {
            setQrText()
        }
    }
}

/**
 * Deletes the lecture with the specified ID.
 *
 * @param lectureId The ID of the lecture to delete.
 */
fun deleteLecture(lectureId: Long) {
    viewModelScope.launch {
        lectureRepo.deleteLecture(lectureId)
    }
}

/**
 * Encrypts the provided string using AES encryption.
 *
 * @param stringToEncrypt The string to encrypt.
 * @return The encrypted string.
 */
private fun encrypt(stringToEncrypt: String): String {
    val aesKey = SecretKeySpec(Constant.qrKey.toByteArray(), "AES")
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
    val aesKey = SecretKeySpec(Constant.qrKey.toByteArray(), "AES")
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.DECRYPT_MODE, aesKey)
    val decryptedText = java.util.Base64.getDecoder().decode(dataToDecrypt)
    return String(cipher.doFinal(decryptedText), charset = Charsets.UTF_8)
}
}

/**
 * UI state class for storing lecture information.
 */
class LectureInfoUiState {
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