package lk.lnbti.iampresent.nav

import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * A destination object representing the route for the Attendance List screen.
 */
object AttendanceListDestination : AppDestination {
    override val route = "attendance_list"
}

/**
 * A destination object representing the route for the Attend Lecture Info screen.
 * It includes a dynamic argument for the lecture ID.
 */
object AttendLectureInfoDestination : AppDestination {
    override val route = "attend_lecture_info"
    const val attendLectureIdArg = "attend_lecture_id"
    val routeWithArgs = "$route/{$attendLectureIdArg}"
    val arguments = listOf(
        navArgument(attendLectureIdArg) { type = NavType.StringType }
    )
}

/**
 * A destination object representing the route for the New Attendance screen.
 */
object NewAttendanceDestination : AppDestination {
    override val route = "new_attendance"
}

