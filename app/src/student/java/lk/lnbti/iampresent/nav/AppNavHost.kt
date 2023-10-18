package lk.lnbti.iampresent.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import lk.lnbti.iampresent.ui.view.AttendanceListScreen
import lk.lnbti.iampresent.ui.view.LectureInfoScreen
import lk.lnbti.iampresent.ui.view.NewLectureScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AttendanceListDestination.route) {
        composable(route = AttendanceListDestination.route) {
            AttendanceListScreen(
                onLectureItemClicked = { lectureId ->
                    navController.navigateToLectureInfo(lectureId)
                },
                onNewLectureClicked = { navController.navigateSingleTopTo(NewAttendanceDestination.route) }
            )
        }
        composable(route = NewAttendanceDestination.route) {
            NewLectureScreen(
                onYesButtonClicked={navController.navigateSingleTopTo(AttendanceListDestination.route)},
                onRetryButtonClicked = {navController.navigateSingleTopTo(NewAttendanceDestination.route)}
            )
        }
        composable(
            route = AttendLectureInfoDestination.routeWithArgs,
            arguments = AttendLectureInfoDestination.arguments
        ) { navBackStackEntry ->
            val lectureId =
                navBackStackEntry.arguments?.getString(AttendLectureInfoDestination.attendLectureIdArg)
            LectureInfoScreen(
                lectureId=lectureId,
                onCancelButtonClicked={navController.navigateSingleTopTo(AttendanceListDestination.route)},
                onDeleteButtonClicked={navController.navigateSingleTopTo(AttendanceListDestination.route)},
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
    this.navigateSingleTopTo("${AttendLectureInfoDestination.route}/$lectureId")
}