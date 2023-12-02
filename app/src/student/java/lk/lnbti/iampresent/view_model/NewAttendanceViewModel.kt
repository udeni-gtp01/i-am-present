package lk.lnbti.iampresent.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lk.lnbti.iampresent.constant.Constant
import lk.lnbti.iampresent.data.Attendance
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.data.Result
import lk.lnbti.iampresent.data.TestData
import lk.lnbti.iampresent.data.User
import lk.lnbti.iampresent.repo.AttendanceRepo
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

/**
 * ViewModel class for managing new attendance creation. Uses Hilt for dependency injection.
 *
 * @param attendanceRepo Repository for accessing attendance data.
 * @param attendanceListUiState UI state for managing attendance list data.
 */
@HiltViewModel
class NewAttendanceViewModel @Inject constructor(
    private val attendanceRepo: AttendanceRepo,
    private val attendanceListUiState: AttendanceListUiState
) : ViewModel() {
    // MutableLiveData for holding the QR code data.
    private val _qrData: MutableLiveData<String> = MutableLiveData(null)
    val qrData: LiveData<String> = _qrData

    // MutableLiveData for holding the result of saving attendance.
    private val _saveAttendanceResult = MutableLiveData<Result<Attendance?>>()
    val saveAttendanceResult: LiveData<Result<Attendance?>> = _saveAttendanceResult

    // Variables for holding lecture and QR code information.
    var time: String = ""
    var lectureId = ""
    var batch: String = ""
    var subject: String = ""
    var location: String = ""
    var lectureEndDate: String = ""
    var lectureEndTime: String = ""
    var checkInDate1: String = ""
    var checkInTime1: String = ""
    var timeDuration: Long = 10000

    /**
     * Validates the scanned QR code data and checks if it is within the specified time duration.
     *
     * @param scannedQrData The scanned QR code data.
     * @return True if the QR code data is valid, false otherwise.
     */
    fun isValidQr(scannedQrData: String): Boolean {
        formatDecryptedData(decrypt(scannedQrData))
        return isTimeValid(time, timeDuration)
    }

    /**
     * Updates the QR code data LiveData when the QR code data changes.
     */
    fun onQrDataChange() {
        _qrData.value = """
            Batch: $batch
            Subject: $subject
            Location: $location
        """.trimIndent()
    }

    /**
     * Checks if the given QR code time is valid within the specified duration.
     *
     * @param qrTime The time extracted from the QR code.
     * @param durationInMilliSecond The specified time duration in milliseconds.
     * @return True if the QR code time is valid, false otherwise.
     */
    private fun isTimeValid(qrTime: String, durationInMilliSecond: Long): Boolean {
        val timeNow = Calendar.getInstance().timeInMillis
        val timeDifference = timeNow - qrTime.toLong()
        //return whether current time is within time duration
        return timeDifference <= durationInMilliSecond
    }

    /**
     * Saves the attendance information. Checks if the lecture has ended before saving.
     */
    fun saveAttendance() {
        _saveAttendanceResult.value = Result.Loading
        //check if lecture has ended
        val sqlDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        val sqlTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        val endDate = sqlDateFormat.parse(lectureEndDate);
        val endTime = sqlTimeFormat.parse(lectureEndTime);
        val cutoff = Date(
            endDate.getYear(),
            endDate.getMonth(),
            endDate.getDate(),
            endTime.getHours(),
            endDate.getMinutes()
        );
        checkInDate1 = getCheckInDate()
        checkInTime1 = getCheckInTime()

        val checkinDate = sqlDateFormat.parse(checkInDate1);
        val checkinTime = sqlTimeFormat.parse(checkInTime1);
        val checkin = Date(
            checkinDate.getYear(),
            checkinDate.getMonth(),
            checkinDate.getDate(),
            checkinTime.getHours(),
            checkinTime.getMinutes()
        );

        if (checkin.before(cutoff)) {
            val newAttendance: Attendance = Attendance(
                lecture = Lecture(
                    lectureId = lectureId.toLong(),
                    location = location,
                    subject = subject,
                    batch = batch,
                    lecturer = User(email = ""),
                    organizer = User(email = "")
                ),
                ispresent = 1,
                student = User(email = TestData.TEST_EMAIL),
                checkintime = checkInTime1,
                checkindate = checkInDate1
            )
            viewModelScope.launch {
                val result: Result<Attendance?> =
                    attendanceRepo.saveAttendance(attendance = newAttendance)
                _saveAttendanceResult.value = result
                if (result is Result.Success) {
                    findAttendanceList()
                }
            }
        } else {
            _saveAttendanceResult.value = Result.Error("Lecture has already ended")
        }
    }

    /**
     * Retrieves the attendance list and updates the UI state.
     */
    private fun findAttendanceList() {
        viewModelScope.launch {
            val result = attendanceRepo.getAttendanceList()
            if (result is Result.Success) {
                attendanceListUiState.loadAttendanceList(result.data)
            }
        }
    }

    /**
     * Formats the current time as a string in the "HH:mm:ss" format.
     *
     * @return The formatted check-in time.
     */
    private fun getCheckInTime(): String {
        val sqlTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val timeNow = Calendar.getInstance().timeInMillis
        return sqlTimeFormat.format(timeNow)
    }

    /**
     * Formats the current date as a string in the "yyyy-MM-dd" format.
     *
     * @return The formatted check-in date.
     */
    private fun getCheckInDate(): String {
        val sqlTimeFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeNow = Calendar.getInstance().timeInMillis
        return sqlTimeFormat.format(timeNow)
    }

    /**
     * Decrypts the provided string using AES decryption.
     *
     * @param dataToDecrypt The string to decrypt.
     * @return The decrypted string.
     */
    private fun decrypt(dataToDecrypt: String): String {
        val aesKey = SecretKeySpec(Constant.QR_KEY.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, aesKey)
        var decryptedText = java.util.Base64.getDecoder().decode(dataToDecrypt)
        return String(cipher.doFinal(decryptedText), charset = Charsets.UTF_8)
    }

    /**
     * Formats the decrypted QR code data and sets the corresponding variables.
     *
     * @param decryptedData The decrypted QR code data.
     */
    private fun formatDecryptedData(decryptedData: String) {
        val allText = decryptedData.split("@@")
        time = allText[0]
        lectureId = allText[1]
        batch = allText[2]
        subject = allText[3]
        location = allText[4]
        lectureEndDate = allText[5]
        lectureEndTime = allText[6]
    }

    /**
     * Resets the result of saving attendance.
     */
    fun resetSaveAttendanceResult() {
        _saveAttendanceResult.value = null
    }
}