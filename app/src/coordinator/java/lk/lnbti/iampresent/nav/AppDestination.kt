package lk.lnbti.iampresent.nav

import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Defines the destination for the general lecture list.
 */
object LectureListDestination : AppDestination {
    override val route = "lecture_list"
}

/**
 * Defines the destination for today's lecture list.
 */
object TodaysLectureListDestination : AppDestination {
    override val route = "lecture_list_today"
}

/**
 * Defines the destination for detailed lecture information.
 */
object LectureInfoDestination : AppDestination {
    /** The route for the lecture information destination. */
    override val route = "lecture_info"

    /** The argument key for passing the lecture ID. */
    const val lectureIdArg = "lecture_id"

    /** The route with the lecture ID argument placeholder. */
    val routeWithArgs = "$route/{$lectureIdArg}"

    /** The list of navigation arguments for this destination. */
    val arguments = listOf(
        navArgument(lectureIdArg) { type = NavType.StringType }
    )
}

/**
 * Defines the destination for creating a new lecture.
 */
object NewLectureDestination : AppDestination {
    override val route = "new_lecture"
}

/**
 * Defines the destination for viewing reports.
 */
object ReportsDestination : AppDestination {
    override val route = "reports"
}