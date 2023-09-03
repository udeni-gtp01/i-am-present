import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import lk.lnbti.iampresent.nav.AddLectureDestination
import lk.lnbti.iampresent.nav.LectureInfoDestination
import lk.lnbti.iampresent.nav.LectureListDestination
import lk.lnbti.iampresent.ui.view.LectureListScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = LectureListDestination.route) {
        composable(route = LectureListDestination.route) {
            LectureListScreen(
                onLectureItemClicked = { lectureId ->
                    navController.navigateToLectureInfo(lectureId)
                },
                onNewLectureClicked = { navController.navigateSingleTopTo(AddLectureDestination.route) }
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
private fun NavHostController.navigateToLectureInfo(lectureId: String) {
    this.navigateSingleTopTo("${LectureInfoDestination.route}/$lectureId")
}