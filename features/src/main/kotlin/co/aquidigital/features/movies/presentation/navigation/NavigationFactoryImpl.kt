package co.aquidigital.features.movies.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import co.aquidigital.core.navigation.NavigationDestination
import co.aquidigital.core.navigation.NavigationFactory
import co.aquidigital.features.movies.presentation.ui.moviesDetail.MovieDetailsRoute
import co.aquidigital.features.movies.presentation.ui.moviesList.MoviesListRoute
import javax.inject.Inject

class NavigationFactoryImpl @Inject constructor() : NavigationFactory {

    override fun create(builder: NavGraphBuilder, navController: NavController) {
        builder.composable(NavigationDestination.MovieListRoute.route) {
            MoviesListRoute(navController = navController)
        }
        builder.composable(
            route = NavigationDestination.MovieDetailRoute.route,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
            MovieDetailsRoute(movieId = movieId)
        }
    }
}
