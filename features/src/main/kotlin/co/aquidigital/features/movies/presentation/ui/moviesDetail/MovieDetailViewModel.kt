package co.aquidigital.features.movies.presentation.ui.moviesDetail

import androidx.lifecycle.SavedStateHandle
import co.aquidigital.core.presentation.mvi.BaseViewModel
import co.aquidigital.features.movies.domain.interactors.GetMovieDetailsInteractor
import co.aquidigital.features.movies.domain.interactors.GetSimilarMoviesInteractor
import co.aquidigital.features.movies.presentation.ui.moviesDetail.MovieDetailsUiState.PartialState
import co.aquidigital.features.movies.presentation.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieDetailsInteractor: GetMovieDetailsInteractor,
    private val similarMoviesInteractor: GetSimilarMoviesInteractor,
    savedStateHandle: SavedStateHandle,
    moviesInitialState: MovieDetailsUiState,
) : BaseViewModel<MovieDetailsUiState, PartialState, MovieDetailEvent, MovieDetailIntent>(
    savedStateHandle = savedStateHandle,
    initialState = moviesInitialState
) {

    internal fun observeMovieDetails(movieId: Int) {
        acceptChanges(
            movieDetailsInteractor(movieId)
                .map { result ->
                    result.fold(
                        onSuccess = { movie ->
                            PartialState.Fetched(movie.toUiModel())
                        },
                        onFailure = {
                            PartialState.Error(it)
                        }
                    )
                }
                .onStart {
                    emit(PartialState.Loading)
                }
        )
    }

    internal fun observeSimilarMovies(movieId: Int) {
        acceptChanges(
            similarMoviesInteractor(movieId)
                .map { result ->
                    result.fold(
                        onSuccess = { movies ->
                            PartialState.SimilarMoviesFetched(movies.movies)
                        },
                        onFailure = {
                            PartialState.SimilarMoviesError(it)
                        }
                    )
                }
                .onStart {
                    emit(PartialState.SimilarMoviesLoading)
                }
        )
    }

    override fun reduceUiState(
        previousState: MovieDetailsUiState,
        partialState: PartialState
    ): MovieDetailsUiState = when(partialState) {
        is PartialState.Loading -> previousState.copy(
            isLoading = true,
            isError = false
        )
        is PartialState.Fetched -> previousState.copy(
            isLoading = false,
            movie = partialState.movie,
            isError = false
        )
        is PartialState.Error -> previousState.copy(
            isLoading = false,
            isError = true
        )
        is PartialState.SimilarMoviesLoading -> previousState.copy(
            isSimilarMoviesLoading = true,
            isSimilarMoviesError = false
        )
        is PartialState.SimilarMoviesFetched -> previousState.copy(
            isSimilarMoviesLoading = false,
            similarMovies = partialState.similarMovies,
            isSimilarMoviesError = false
        )
        is PartialState.SimilarMoviesError -> previousState.copy(
            isSimilarMoviesLoading = false,
            isSimilarMoviesError = true
        )
    }

    override fun mapIntents(intent: MovieDetailIntent): Flow<PartialState> {
        TODO("Not yet implemented")
    }

}