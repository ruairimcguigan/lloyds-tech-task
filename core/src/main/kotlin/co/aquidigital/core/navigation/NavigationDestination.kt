package co.aquidigital.core.navigation

sealed class NavigationDestination(val route: String) {

    data object Back : NavigationDestination("navigationBack")

    data object MovieListRoute : NavigationDestination("movieListDestination")

    data class MovieDetailRoute(val movieId: Int) : NavigationDestination("movieDetailDestination/$movieId") {
        companion object {
            const val route = "movieDetailDestination/{movieId}"
            fun createRoute(movieId: Int) = "movieDetailDestination/$movieId"
        }
    }
}
