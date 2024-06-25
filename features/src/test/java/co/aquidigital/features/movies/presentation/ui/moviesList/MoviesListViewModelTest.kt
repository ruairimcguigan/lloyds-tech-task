package co.aquidigital.features.movies.presentation.ui.moviesList

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import co.aquidigital.features.movies.domain.interactors.GetTopRatedMoviesInteractor
import co.aquidigital.features.movies.presentation.model.TopMovieUi
import co.aquidigital.features.movies.presentation.mapper.toDomainModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class MoviesListViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()
    private val savedStateHandle = SavedStateHandle()
    private val getTopRatedMoviesInteractor: GetTopRatedMoviesInteractor = mockk()

    private lateinit var viewModel: MoviesListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = MoviesListViewModel(
            getTopRatedMoviesInteractor,
            savedStateHandle,
            MoviesListUiState()
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given successful movie fetch, when observeMovies is called, then uiState is updated`() = runTest {
        // given
        val movies = listOf(TopMovieUi(id = 1, title = "Movie 1", overview = "", posterPath = "", backdropPath = "", voteAverage = 0.0))
        val moviesDomain = movies.map { it.toDomainModel() }
        coEvery { getTopRatedMoviesInteractor.invoke() } returns flowOf(Result.success(moviesDomain))

        // when
        viewModel.observeMovies()

        // then
        val uiState = viewModel.uiState.value
        assertEquals(false, uiState.isLoading)
        assertEquals(movies, uiState.topMovies)
        assertEquals(false, uiState.isError)
    }

    @Test
    fun `given failed movie fetch, when observeMovies is called, then uiState shows error`() = runBlockingTest {
        // given
        val exception = RuntimeException("Fetch error")
        coEvery { getTopRatedMoviesInteractor.invoke() } returns flowOf(Result.failure(exception))

        // when
        viewModel.observeMovies()

        // then
        val uiState = viewModel.uiState.value
        assertEquals(false, uiState.isLoading)
        assertEquals(true, viewModel.uiState.value.isError)
    }
}
