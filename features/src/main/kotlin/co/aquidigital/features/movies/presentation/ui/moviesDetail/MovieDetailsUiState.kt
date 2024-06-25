package co.aquidigital.features.movies.presentation.ui.moviesDetail

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import co.aquidigital.features.movies.data.models.MovieWithGenre
import co.aquidigital.features.movies.presentation.model.SimilarMovieUiModel
import co.aquidigital.features.movies.presentation.model.TopMovieUi
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class MovieDetailsUiState(
    val isLoading: Boolean = false,
    val movie: TopMovieUi = TopMovieUi(),
    val isError: Boolean = false,
    val isSimilarMoviesLoading: Boolean = false,
    val similarMovies: List<MovieWithGenre> = emptyList(),
    val isSimilarMoviesError: Boolean = false
) : Parcelable {

    sealed class PartialState {
        data object Loading : PartialState()
        data class Fetched(val movie: TopMovieUi) : PartialState()
        data class Error(val throwable: Throwable) : PartialState()

        data object SimilarMoviesLoading : PartialState()
        data class SimilarMoviesFetched(val similarMovies: List<MovieWithGenre>) : PartialState()
        data class SimilarMoviesError(val error: Throwable) : PartialState()
    }
}
