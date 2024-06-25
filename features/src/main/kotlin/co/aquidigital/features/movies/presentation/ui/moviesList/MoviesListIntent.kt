package co.aquidigital.features.movies.presentation.ui.moviesList

sealed class MoviesListIntent {

    data object RefreshMovies: MoviesListIntent()

    data class OnMovieClicked(val id: Int): MoviesListIntent()

}
