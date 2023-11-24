package lk.lnbti.iampresent.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import lk.lnbti.iampresent.ui.compose.AttendanceInfoScreen
import lk.lnbti.iampresent.ui.compose.AttendanceListScreen
import lk.lnbti.iampresent.ui.compose.NewAttendanceScreen

/**
 * Composable function representing the main navigation host for the student app.
 *
 * @param navController Navigation controller used for navigating between destinations.
 */
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = AttendanceListDestination.route) {
        // Attendance List Screen
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
        // New Attendance Screen
        composable(route = NewAttendanceDestination.route) {
            NewAttendanceScreen(
                onYesButtonClicked = { navController.navigateSingleTopTo(AttendanceListDestination.route) },
                onRetryButtonClicked = { navController.navigateSingleTopTo(NewAttendanceDestination.route) }
            )
        }
        // Attendance Info Screen
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

/**
 * Extension function for the [NavHostController] to navigate to a destination in single top mode.
 *
 * @param route The route to navigate to.
 */
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

/**
 * Extension function for [NavHostController] to navigate to the lecture info destination in single top mode.
 *
 * @param lectureId The ID of the lecture to navigate to.
 */
private fun NavHostController.navigateToLectureInfo(lectureId: String) {
    this.navigateSingleTopTo("${AttendLectureInfoDestination.route}/$lectureId")
}