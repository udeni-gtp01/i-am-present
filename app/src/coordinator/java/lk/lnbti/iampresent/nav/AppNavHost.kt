package lk.lnbti.iampresent.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import lk.lnbti.iampresent.ui.compose.LectureInfoScreen
import lk.lnbti.iampresent.ui.compose.LectureListScreen
import lk.lnbti.iampresent.ui.compose.NewLectureScreen
import lk.lnbti.iampresent.ui.compose.ReportsScreen
import lk.lnbti.iampresent.ui.compose.TodaysLectureListScreen

/**
 * Composable function representing the navigation host for the coordinator application.
 * It defines the navigation graph using Jetpack Compose Navigation.
 *
 * @param navController The navigation controller responsible for handling navigation within the app.
 */
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = TodaysLectureListDestination.route) {

        // Define composable for the LectureListDestination
        composable(route = LectureListDestination.route) {
            LectureListScreen(
                onLectureItemClicked = { lectureId ->
                    navController.navigateToLectureInfo(lectureId)
                },
                onNewLectureClicked = { navController.navigateSingleTopTo(NewLectureDestination.route) },
                onTodayNavButtonClicked = {
                    navController.navigateSingleTopTo(
                        TodaysLectureListDestination.route
                    )
                },
                onReportsNavButtonClicked = { navController.navigateSingleTopTo(ReportsDestination.route) },
                onAllNavButtonClicked = { navController.navigateSingleTopTo(LectureListDestination.route) }
            )
        }

        // Define composable for the TodaysLectureListDestination
        composable(route = TodaysLectureListDestination.route) {
            TodaysLectureListScreen(
                onLectureItemClicked = { lectureId ->
                    navController.navigateToLectureInfo(lectureId)
                },
                onNewLectureClicked = { navController.navigateSingleTopTo(NewLectureDestination.route) },
                onTodayNavButtonClicked = {
                    navController.navigateSingleTopTo(
                        TodaysLectureListDestination.route
                    )
                },
                onReportsNavButtonClicked = { navController.navigateSingleTopTo(ReportsDestination.route) },
                onAllNavButtonClicked = { navController.navigateSingleTopTo(LectureListDestination.route) }
            )
        }

        // Define composable for the NewLectureDestination
        composable(route = NewLectureDestination.route) {
            NewLectureScreen(
                onSuccessfulSave = { lectureId ->
                    navController.navigateToLectureInfo(lectureId)
                },
                onTodayNavButtonClicked =
                { navController.navigateSingleTopTo(TodaysLectureListDestination.route) },
                onReportsNavButtonClicked = { navController.navigateSingleTopTo(ReportsDestination.route) },
                onAllNavButtonClicked = { navController.navigateSingleTopTo(LectureListDestination.route) }
            )
        }

        // Define composable for the ReportsDestination
        composable(route = ReportsDestination.route) {
            ReportsScreen(
                onTodayNavButtonClicked =
                { navController.navigateSingleTopTo(TodaysLectureListDestination.route) },
                onReportsNavButtonClicked = { navController.navigateSingleTopTo(ReportsDestination.route) },
                onAllNavButtonClicked = { navController.navigateSingleTopTo(LectureListDestination.route) }
            )
        }

        // Define composable for LectureInfoDestination with arguments
        composable(
            route = LectureInfoDestination.routeWithArgs,
            arguments = LectureInfoDestination.arguments
        ) { navBackStackEntry ->
            // Extract lectureId from arguments
            val lectureId =
                navBackStackEntry.arguments?.getString(LectureInfoDestination.lectureIdArg)
            // Create LectureInfoScreen with relevant callbacks
            LectureInfoScreen(
                lectureId = lectureId,
                onCancelButtonClicked = { navController.navigateSingleTopTo(LectureListDestination.route) },
                onDeleteButtonClicked = { navController.navigateSingleTopTo(LectureListDestination.route) },
                onEditButtonClicked = {},
                onTodayNavButtonClicked = {
                    navController.navigateSingleTopTo(
                        TodaysLectureListDestination.route
                    )
                },
                onReportsNavButtonClicked = { navController.navigateSingleTopTo(ReportsDestination.route) },
                onAllNavButtonClicked = { navController.navigateSingleTopTo(LectureListDestination.route) }
            )
        }
    }
}

/**
 * Extension function for [NavHostController] that navigates to a destination using SingleTop behavior.
 *
 * @param route The destination route to navigate to.
 */
fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = false
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }

/**
 * Extension function for [NavHostController] that navigates to the LectureInfo destination using SingleTop behavior.
 *
 * @param lectureId The ID of the lecture to navigate to within the LectureInfo destination.
 */
private fun NavHostController.navigateToLectureInfo(lectureId: String) {
    this.navigateSingleTopTo("${LectureInfoDestination.route}/$lectureId")
}