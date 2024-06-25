package co.aquidigital.features.movies.presentation.ui.moviesList

sealed class MoviesListEvent {
    data class OpenMovieDetails(val id: Int) : MoviesListEvent()

    data object refreshMoviesList: MoviesListEvent()
}
