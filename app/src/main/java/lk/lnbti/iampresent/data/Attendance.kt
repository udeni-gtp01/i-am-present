package lk.lnbti.iampresent.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a list of attendance items.
 *
 * @property items List of [Attendance] items.
 */
@Parcelize
data class AttendanceList(
    val items: List<Attendance>
) : Parcelable

/**
 * Represents attendance information for a specific student in a lecture.
 *
 * @property attendanceid Unique identifier for the attendance record.
 * @property lecture The [Lecture] associated with the attendance.
 * @property student The [User] associated with the attendance.
 * @property ispresent Flag indicating whether the student is present (1) or not (0).
 * @property checkindate Date when the student checked in for the lecture.
 * @property checkintime Time when the student checked in for the lecture.
 */
@Parcelize
data class Attendance(
    val attendanceid: Long = 0,
    val lecture: Lecture,
    val student: User,
    val ispresent: Int,
    val checkindate: String,
    val checkintime: String
) : Parcelable