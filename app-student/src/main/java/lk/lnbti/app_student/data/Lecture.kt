package lk.lnbti.app_student.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import lk.lnbti.iampresent.data.User

@Parcelize
data class LectureList(
    // @SerializedName("lecturestatusid") val lecturestatusid: Int,
    // @SerializedName("statusname") val statusname: String?=null,
    val items:List<Lecture>
) : Parcelable

@Parcelize
data class LectureStatus(
    @SerializedName("lecturestatusid") val lectureStatusId: Int?=0,
    @SerializedName("statusname") val statusName: String=""
) : Parcelable
@Parcelize
data class Lecture(
    @SerializedName("lectureid") val lectureId: Int=0,
    @SerializedName("venue") val location: String,
    @SerializedName("subject") val subject: String,
    @SerializedName("batch") val batch: String,
) : Parcelable

