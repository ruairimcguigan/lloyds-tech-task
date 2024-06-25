package co.aquidigital.features.movies.presentation.ui.moviesList

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import co.aquidigital.features.movies.presentation.model.TopMovieUi
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class MoviesListUiState(
    val isLoading: Boolean = false,
    val topMovies: List<TopMovieUi> = emptyList(),
    val isError: Boolean = false,
) : Parcelable {

    sealed class PartialState {
        data object Loading : PartialState()
        data class Fetched(val list: List<TopMovieUi>) : PartialState()
        data class Error(val throwable: Throwable) : PartialState()
    }
}
