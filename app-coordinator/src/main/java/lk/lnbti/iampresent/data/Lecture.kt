package lk.lnbti.iampresent.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.sql.Date
import java.sql.Time

@Parcelize
data class LectureList(
   // @SerializedName("lecturestatusid") val lecturestatusid: Int,
   // @SerializedName("statusname") val statusname: String?=null,
    val items:List<Lecture>
) : Parcelable

@Parcelize
data class LectureStatus(
    @SerializedName("lecturestatusid") val lecturestatusid: Int,
    @SerializedName("statusname") val statusname: String=""
) : Parcelable
@Parcelize
data class Lecture(
    @SerializedName("lectureid") val lectureid: Int?=null,
    @SerializedName("venue") val venue: String,
    @SerializedName("startdate") val startdate: String,
    @SerializedName("starttime") val starttime: String,
    @SerializedName("enddate") val enddate: String,
    @SerializedName("endtime") val endtime: String,
    @SerializedName("semester") val semester: Int,
    @SerializedName("subject") val subject: String,
    @SerializedName("batch") val batch: String,
    @SerializedName("organizerid") val organizerid: User,
    @SerializedName("lecturerid") val lecturerid: User,
    @SerializedName("lecturestatusid") val lecturestatusid: LectureStatus
) : Parcelable

