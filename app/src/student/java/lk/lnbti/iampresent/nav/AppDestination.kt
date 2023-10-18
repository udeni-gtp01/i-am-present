package lk.lnbti.iampresent.nav

import androidx.navigation.NavType
import androidx.navigation.navArgument
import lk.lnbti.iampresent.nav.AppDestination

object AttendanceListDestination : AppDestination{
    override val route="attendance_list"
}
object AttendLectureInfoDestination : AppDestination {
    override val route = "attend_lecture_info"
    const val attendLectureIdArg = "attend_lecture_id"
    val routeWithArgs = "$route/{$attendLectureIdArg}"
    val arguments = listOf(
        navArgument(attendLectureIdArg) { type = NavType.StringType }
    )
}
object NewAttendanceDestination : AppDestination {
    override val route = "new_attendance"
}

