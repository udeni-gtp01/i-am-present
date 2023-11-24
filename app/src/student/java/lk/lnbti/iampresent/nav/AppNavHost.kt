package lk.lnbti.iampresent.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import lk.lnbti.iampresent.ui.compose.AttendanceInfoScreen
import lk.lnbti.iampresent.ui.compose.AttendanceListScreen
import lk.lnbti.iampresent.ui.compose.NewLectureScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AttendanceListDestination.route) {
        composable(route = AttendanceListDestination.route) {
            AttendanceListScreen(
                onAttendanceItemClicked = { lectureId ->
                    navController.navigateToLectureInfo(lectureId)
                },
                onNewAttendanceClicked = {
                    navController.navigateSingleTopTo(
                        NewAttendanceDestination.route
                    )
                }
            )
        }
        composable(route = NewAttendanceDestination.route) {
            NewLectureScreen(
                onYesButtonClicked = { navController.navigateSingleTopTo(AttendanceListDestination.route) },
                onRetryButtonClicked = { navController.navigateSingleTopTo(NewAttendanceDestination.route) }
            )
        }
        composable(
            route = AttendLectureInfoDestination.routeWithArgs,
            arguments = AttendLectureInfoDestination.arguments
        ) { navBackStackEntry ->
            val lectureId =
                navBackStackEntry.arguments?.getString(AttendLectureInfoDestination.attendLectureIdArg)
            AttendanceInfoScreen(
                lectureId = lectureId,
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