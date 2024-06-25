package co.aquidigital.features.movies.presentation.ui.moviesList

import androidx.lifecycle.SavedStateHandle
import co.aquidigital.core.presentation.mvi.BaseViewModel
import co.aquidigital.features.movies.domain.interactors.GetTopRatedMoviesInteractor
import co.aquidigital.features.movies.presentation.ui.moviesList.MoviesListUiState.PartialState
import co.aquidigital.features.movies.presentation.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val getTopRatedMoviesInteractor: GetTopRatedMoviesInteractor,
    savedStateHandle: SavedStateHandle,
    moviesInitialState: MoviesListUiState,
) : BaseViewModel<MoviesListUiState, PartialState, MoviesListEvent, MoviesListIntent>(
    savedStateHandle = savedStateHandle,
    initialState = moviesInitialState,
) {

    init {
        observeMovies()
    }

    fun observeMovies() = acceptChanges(
        getTopRatedMoviesInteractor()
            .map { result ->
                result.fold(
                    onSuccess = { movieList ->
                        PartialState.Fetched(movieList.map { it.toUiModel() })
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

    override fun reduceUiState(
        previousState: MoviesListUiState,
        partialState: PartialState,
    ): MoviesListUiState = when (partialState) {

        is PartialState.Loading -> previousState.copy(
            isLoading = true,
            isError = false,
        )

        is PartialState.Fetched -> previousState.copy(
            isLoading = false,
            topMovies = partialState.list,
            isError = false,
        )

        is PartialState.Error -> previousState.copy(
            isLoading = false,
            isError = true,
        )
    }

    override fun mapIntents(intent: MoviesListIntent): Flow<PartialState> = when (intent) {
        is MoviesListIntent.OnMovieClicked -> openMovieDetails(intent.id)
        MoviesListIntent.RefreshMovies -> refreshMovies()
    }

    private fun openMovieDetails(movieId: Int): Flow<PartialState> = flow {
        setEvent(MoviesListEvent.OpenMovieDetails(movieId))
    }

    private fun refreshMovies(): Flow<PartialState> = flow {
        setEvent(MoviesListEvent.refreshMoviesList)
    }
}
