package lk.lnbti.iampresent.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Parcelable data class representing a list of lectures.
 *
 * @property items List of [Lecture] objects.
 */
@Parcelize
data class LectureList(
    val items: List<Lecture>
) : Parcelable

/**
 * Parcelable data class representing the status of a lecture.
 *
 * @property lectureStatusId Unique identifier for the lecture status.
 * @property statusName Name of the lecture status.
 */
@Parcelize
data class LectureStatus(
    @SerializedName("lecturestatusid") val lectureStatusId: Int? = 0,
    @SerializedName("statusname") val statusName: String = ""
) : Parcelable

/**
 * Parcelable data class representing a lecture.
 *
 * @property lectureId Unique identifier for the lecture.
 * @property location location of the lecture.
 * @property startDate Start date of the lecture in the format "yyyy-MM-dd".
 * @property startTime Start time of the lecture in the format "HH:mm:ss".
 * @property endDate End date of the lecture in the format "yyyy-MM-dd".
 * @property endTime End time of the lecture in the format "HH:mm:ss".
 * @property semester Semester of the lecture.
 * @property subject Subject of the lecture.
 * @property batch Batch associated with the lecture.
 * @property organizer Organizer of the lecture, represented by a [User] object.
 * @property lecturer Lecturer of the lecture, represented by a [User] object.
 * @property lectureStatus Status of the lecture, represented by a [LectureStatus] object.
 */
@Parcelize
data class Lecture(
    @SerializedName("lectureid") val lectureId: Long = 0,
    @SerializedName("venue") val location: String,
    @SerializedName("startdate") val startDate: String = "0000-00-00",
    @SerializedName("starttime") val startTime: String = "00:00:00",
    @SerializedName("enddate") val endDate: String = "0000-00-00",
    @SerializedName("endtime") val endTime: String = "00:00:00",
    @SerializedName("semester") val semester: Int = 0,
    @SerializedName("subject") val subject: String,
    @SerializedName("batch") val batch: String,
    @SerializedName("organizerid") val organizer: User = User(),
    @SerializedName("lecturerid") val lecturer: User = User(),
    @SerializedName("lecturestatusid") val lectureStatus: LectureStatus = LectureStatus()
) : Parcelable