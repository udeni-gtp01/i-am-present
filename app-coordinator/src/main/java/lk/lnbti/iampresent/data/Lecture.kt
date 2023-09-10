package lk.lnbti.iampresent.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.sql.Date
import java.sql.Time

@Parcelize
data class LectureList(
    @SerializedName("lecturestatusid") val lecturestatusid: Int,
    @SerializedName("statusname") val statusname: String?=null
) : Parcelable

@Parcelize
data class LectureStatus(
    @SerializedName("lecturestatusid") val lecturestatusid: Int,
    @SerializedName("statusname") val statusname: String?=null
) : Parcelable

@Parcelize
data class Batch(
    @SerializedName("batchcode") val batchcode: String
) : Parcelable

@Parcelize
data class Lecture(
    @SerializedName("lectureid") val lectureid: Int?=null,
    @SerializedName("venueid") val venueid: Venue?,
    @SerializedName("startdate") val startdate: String,
    @SerializedName("starttime") val starttime: String,
    @SerializedName("enddate") val enddate: String,
    @SerializedName("endtime") val endtime: String,
    @SerializedName("semester") val semester: Int,
    @SerializedName("topic") val topic: String,
    @SerializedName("subjectid") val subjectid: Subject,
    @SerializedName("batchcode") val batchcode: Batch,
    @SerializedName("organizerid") val organizerid: User,
    @SerializedName("lecturerid") val lecturerid: User,
    @SerializedName("lecturestatusid") val lecturestatusid: LectureStatus
) : Parcelable

@Parcelize
data class Subject(
    @SerializedName("subjectid") val subjectid: Int,
    @SerializedName("subjectname") val subjectname: String?=null
) : Parcelable

@Parcelize
data class Venue(
    @SerializedName("venueid") val venueid: Int,
    @SerializedName("venuename") val venuename: String?=null
) : Parcelable
