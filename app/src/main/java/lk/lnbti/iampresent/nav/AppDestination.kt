package lk.lnbti.iampresent.nav

/**
 * Interface representing a destination within the application navigation.
 * Implementations of this interface define a unique route that can be used
 * for navigating to the specific destination.
 */
interface AppDestination {
    /**
     * Gets the unique route associated with this destination.
     *
     * The route is used for navigation within the application and should be
     * unique to each destination.
     *
     * @return The route string for this destination.
     */
    val route: String
}

