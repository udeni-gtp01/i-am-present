package lk.lnbti.iampresent.nav

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface AppDestination {
    val route: String
}

object LectureListDestination : AppDestination{
    override val route="lecture_list"
}
object LectureInfoDestination : AppDestination {
    override val route = "lecture_info"
    private const val lectureIdArg = "lecture_id"
    val routeWithArgs = "$route/{$lectureIdArg}"
    val arguments = listOf(
        navArgument(lectureIdArg) { type = NavType.StringType }
    )
}
object NewLectureDestination : AppDestination {
    override val route = "new_lecture"
}

