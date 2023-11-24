package lk.lnbti.iampresent.view_model

import android.os.Build
import androidx.annotation.RequiresApi
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
import lk.lnbti.iampresent.data.User
import lk.lnbti.iampresent.repo.AttendanceRepo
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

@HiltViewModel
class NewAttendanceViewModel @Inject constructor(
    private val attendanceRepo: AttendanceRepo,
    private val attendanceListUiState: AttendanceListUiState
) : ViewModel() {

    private val _qrData: MutableLiveData<String> = MutableLiveData(null)
    val qrData: LiveData<String> = _qrData

    private val _saveAttendanceResult = MutableLiveData<Result<Attendance?>>()
    val saveAttendanceResult: LiveData<Result<Attendance?>> = _saveAttendanceResult

    var time: String = ""
    var lectureId = ""
    var batch: String = ""
    var subject: String = ""
    var location: String = ""
    var lectureEndDate: String = ""
    var lectureEndTime: String = ""
    var checkInDate1: String = ""
    var checkInTime1: String = ""
    var timeDuration: Long = 30000

    fun isValidQr(scannedQrData: String): Boolean {
        formatDecryptedData(decrypt(scannedQrData))
        return isTimeValid(time, timeDuration)
    }

    fun onQrDataChange() {
        _qrData.value = """
            Batch: $batch
            Subject: $subject
            Location: $location
        """.trimIndent()
    }

    private fun isTimeValid(qrTime: String, durationInMilliSecond: Long): Boolean {
        val timeNow = Calendar.getInstance().timeInMillis
        val timeDifference = timeNow - qrTime.toLong()
        //return whether current time is within time duration
        return timeDifference <= durationInMilliSecond
    }

    fun saveAttendance() {
        _saveAttendanceResult.value = Result.Loading
        //check if lecture has been ended
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
                student = User(email = ""),
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
    private fun findAttendanceList() {
        viewModelScope.launch {
            val result = attendanceRepo.getAttendanceList()
            if (result is Result.Success) {
                attendanceListUiState.loadAttendanceList(result.data)
            }
        }
    }
    private fun getCheckInTime(): String {
        val sqlTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val timeNow = Calendar.getInstance().timeInMillis
        return sqlTimeFormat.format(timeNow)
    }

    private fun getCheckInDate(): String {
        val sqlTimeFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeNow = Calendar.getInstance().timeInMillis
        return sqlTimeFormat.format(timeNow)
    }

    private fun decrypt(dataToDecrypt: String): String {
        val aesKey = SecretKeySpec(Constant.QR_KEY.toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, aesKey)
        var decryptedText = java.util.Base64.getDecoder().decode(dataToDecrypt)
        return String(cipher.doFinal(decryptedText), charset = Charsets.UTF_8)
    }

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

    fun resetSaveAttendanceResult() {
        _saveAttendanceResult.value = null
    }
}