package lk.lnbti.iampresent.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable
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
    @SerializedName("lecturestatusid") val lectureStatusId: Int?=0,
    @SerializedName("statusname") val statusName: String=""
) : Parcelable
@Parcelize
data class Lecture(
    @SerializedName("lectureid") val lectureId: Int=0,
    @SerializedName("venue") val location: String,
    @SerializedName("startdate") val startDate: String="",
    @SerializedName("starttime") val startTime: String="",
    @SerializedName("enddate") val endDate: String="",
    @SerializedName("endtime") val endTime: String="",
    @SerializedName("semester") val semester: Int=0,
    @SerializedName("subject") val subject: String,
    @SerializedName("batch") val batch: String,
    @SerializedName("organizerid") val organizer: User=User(),
    @SerializedName("lecturerid") val lecturer: User=User(),
    @SerializedName("lecturestatusid") val lectureStatus: LectureStatus=LectureStatus()
) : Parcelable

