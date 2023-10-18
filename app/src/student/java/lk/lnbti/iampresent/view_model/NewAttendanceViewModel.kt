package lk.lnbti.iampresent.view_model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lk.lnbti.iampresent.data.Attendance
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.data.Result
import lk.lnbti.iampresent.data.User
import lk.lnbti.iampresent.repo.AttendanceRepo
import lk.lnbti.iampresent.ui_state.LectureListUiState
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
    private val lectureListUiState: LectureListUiState
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
    var timeDuration: Long = 30000

    private val qrKey = "BYT765#76GHFaunY"

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
        return timeDifference <= durationInMilliSecond || timeDifference >= durationInMilliSecond
    }

    fun saveAttendance() {
        _saveAttendanceResult.value = Result.Loading
        val newAttendance: Attendance = Attendance(
            lecture = Lecture(
                lectureId = lectureId.toInt(),
                location = location,
                subject = subject,
                batch = batch,

                ),
            ispresent = 1,
            student = User(
                name = "admin",
            ),
        )
        val respose = ""
        viewModelScope.launch {
            val result: Result<Attendance?> =
                attendanceRepo.saveAttendance(attendance = newAttendance)
            _saveAttendanceResult.value = result
            when (result) {
                is Result.Success -> {
                    findLectureList()
                }

                else -> {}
            }
        }

    }

    fun findLectureList() {
        viewModelScope.launch {
            //lectureListUiState.loadLectureList(attendanceRepo.getAttendanceList())
        }
    }

    private fun convertDateToSqlFormat(myDate: String): String {
        val selectedDateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        val sqlDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val selectedDate: Date = selectedDateFormat.parse(myDate)
        return sqlDateFormat.format(selectedDate)
    }

    private fun convertTimeToSqlFormat(myTime: String): String {
        val selectedTimeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val sqlTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        val selectedTime: Date = selectedTimeFormat.parse(myTime)
        return sqlTimeFormat.format(selectedTime)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun decrypt(dataToDecrypt: String): String {
        val aesKey = SecretKeySpec(qrKey.toByteArray(), "AES")
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

    }
    fun resetSaveAttendanceResult(){
        _saveAttendanceResult.value=null
    }
}