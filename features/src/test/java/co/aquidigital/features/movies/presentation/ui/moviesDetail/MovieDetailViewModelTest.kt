package co.aquidigital.features.movies.presentation.ui.moviesDetail

import co.aquidigital.features.movies.data.models.MoviesWithGenresResponse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import co.aquidigital.features.movies.domain.interactors.GetMovieDetailsInteractor
import co.aquidigital.features.movies.domain.interactors.GetSimilarMoviesInteractor
import co.aquidigital.features.movies.presentation.model.TopMovieUi
import co.aquidigital.features.movies.data.models.MovieWithGenre
import co.aquidigital.features.movies.presentation.mapper.toDomainModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.rules.TestRule
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class MovieDetailViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val dispatcher = StandardTestDispatcher()
    private val savedStateHandle = SavedStateHandle()
    private val getMovieDetailsInteractor: GetMovieDetailsInteractor = mockk()
    private val getSimilarMoviesInteractor: GetSimilarMoviesInteractor = mockk()

    private lateinit var viewModel: MovieDetailViewModel


    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = MovieDetailViewModel(
            getMovieDetailsInteractor,
            getSimilarMoviesInteractor,
            savedStateHandle,
            MovieDetailsUiState()
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given successful movie details fetch, when observeMovieDetails is called, then uiState is updated`() = runTest {
        // given
        val movieId = 1
        val movieUi = TopMovieUi(id = 1, title = "Movie 1", overview = "Overview", posterPath = "path", backdropPath = "backdrop", voteAverage = 8.0)
        coEvery { getMovieDetailsInteractor.invoke(movieId) } returns flowOf(Result.success(movieUi.toDomainModel()))

        // when
        viewModel.observeMovieDetails(movieId)

        // then
        val uiState = viewModel.uiState.value
        assertEquals(false, uiState.isLoading)
        assertEquals(movieUi, uiState.movie)
        assertEquals(false, uiState.isError)
    }

    @Test
    fun `given failed movie details fetch, when observeMovieDetails is called, then uiState shows error`() = runTest {
        // given
        val movieId = 1
        val exception = RuntimeException("Fetch error")
        coEvery { getMovieDetailsInteractor.invoke(movieId) } returns flowOf(Result.failure(exception))

        // when
        viewModel.observeMovieDetails(movieId)

        // then
        val uiState = viewModel.uiState.value
        assertEquals(false, uiState.isLoading)
        assertEquals(true, uiState.isError)
    }

    @Test
    fun `given successful similar movies fetch, when observeSimilarMovies is called, then uiState is updated`() = runTest {
        // given
        val movieId = 1
        val movieWithGenre = MovieWithGenre(id = 1L, title = "Movie 1", posterPath = "path", voteAverage = 8.0, genreNames = listOf("Action"))
        val similarMoviesResponse = MoviesWithGenresResponse(listOf(movieWithGenre))
        coEvery { getSimilarMoviesInteractor.invoke(movieId) } returns flowOf(Result.success(similarMoviesResponse))

        // when
        viewModel.observeSimilarMovies(movieId)

        // then
        val uiState = viewModel.uiState.value
        assertEquals(false, uiState.isSimilarMoviesLoading)
        assertEquals(listOf(movieWithGenre), uiState.similarMovies)
        assertEquals(false, uiState.isSimilarMoviesError)
    }

    @Test
    fun `given failed similar movies fetch, when observeSimilarMovies is called, then uiState shows error`() = runTest {
        // given
        val movieId = 1
        val exception = RuntimeException("Fetch error")
        coEvery { getSimilarMoviesInteractor.invoke(movieId) } returns flowOf(Result.failure(exception))

        // when
        viewModel.observeSimilarMovies(movieId)

        // then
        val uiState = viewModel.uiState.value
        assertEquals(false, uiState.isSimilarMoviesLoading)
        assertEquals(true, uiState.isSimilarMoviesError)
    }
}
