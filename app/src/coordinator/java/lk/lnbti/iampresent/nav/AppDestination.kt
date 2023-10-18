package lk.lnbti.iampresent.coordinator.nav

import androidx.navigation.NavType
import androidx.navigation.navArgument
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.nav.AppDestination

object LectureListDestination : AppDestination {
    override val route="lecture_list"
}
object TodaysLectureListDestination : AppDestination {
    override val route="lecture_list_today"
}
object LectureInfoDestination : AppDestination {
    override val route = "lecture_info"
    const val lectureIdArg = "lecture_id"
    val routeWithArgs = "$route/{$lectureIdArg}"
    val arguments = listOf(
        navArgument(lectureIdArg) { type = NavType.StringType }
    )
}
object NewLectureDestination : AppDestination {
    override val route = "new_lecture"
}
object ReportsDestination : AppDestination {
    override val route = "reports"
}

