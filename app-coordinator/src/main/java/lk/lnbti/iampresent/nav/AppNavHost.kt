import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.nav.LectureInfoDestination
import lk.lnbti.iampresent.nav.LectureListDestination
import lk.lnbti.iampresent.nav.NewLectureDestination
import lk.lnbti.iampresent.ui.view.LectureInfoScreen
import lk.lnbti.iampresent.ui.view.LectureListScreen
import lk.lnbti.iampresent.ui.view.NewLectureScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = LectureListDestination.route) {
        composable(route = LectureListDestination.route) {
            LectureListScreen(
                onLectureItemClicked = { lectureId ->
                    navController.navigateToLectureInfo(lectureId)
                },
                onNewLectureClicked = { navController.navigateSingleTopTo(NewLectureDestination.route) }
            )
        }
        composable(route = NewLectureDestination.route) {
            NewLectureScreen(onSaveButtonClicked = {},
                onCancelButtonClicked = {}
            )
        }
        composable(
            route = LectureInfoDestination.routeWithArgs,
            arguments = LectureInfoDestination.arguments
        ) { navBackStackEntry ->
            val lectureId =
               navBackStackEntry.arguments?.getString(LectureInfoDestination.lectureIdArg)
            LectureInfoScreen(
                lectureId=lectureId,
                onCancelButtonClicked={navController.navigateSingleTopTo(LectureListDestination.route)},
                onDeleteButtonClicked={navController.navigateSingleTopTo(LectureListDestination.route)},
                onEditButtonClicked = {}
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