package lk.lnbti.app_student.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import lk.lnbti.iampresent.data.User

@Parcelize
data class AttendanceList(
    val items:List<Attendance>
) : Parcelable
@Parcelize
data class Attendance(
    val attendanceid: Int=0,
    val lecture: Lecture,
    val student: User,
    val ispresent: Int
): Parcelable
