package co.aquidigital.features.movies.domain.interactors

import co.aquidigital.features.movies.data.models.MoviesWithGenresResponse
import co.aquidigital.features.movies.data.models.MovieWithGenre
import co.aquidigital.features.movies.data.remote.MoviesApi
import co.aquidigital.features.movies.data.repository.MoviesRepositoryImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import okhttp3.MediaType.Companion.toMediaType

@OptIn(ExperimentalSerializationApi::class)
@ExperimentalCoroutinesApi
class GetSimilarMoviesInteractorTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var moviesApi: MoviesApi
    private lateinit var moviesRepository: MoviesRepositoryImpl
    private lateinit var getSimilarMoviesInteractor: GetSimilarMoviesInteractor

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }

        moviesApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(MoviesApi::class.java)

        moviesRepository = MoviesRepositoryImpl(moviesApi, mockk(relaxed = true))
        getSimilarMoviesInteractor = GetSimilarMoviesInteractor { movieId ->
            getSimilarMovies(movieId, moviesRepository)
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `given success response, when getSimilarMovies is called, then emit success result`() = runBlockingTest {
        val movieId = 1
        val similarMoviesJson = """
            {
                "movies": [
                    {
                        "id": 1,
                        "title": "Movie Title",
                        "posterPath": "/path",
                        "voteAverage": 8.0,
                        "genreNames": ["Action", "Adventure"]
                    }
                ]
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(similarMoviesJson).setResponseCode(200))

        val results = mutableListOf<Result<MoviesWithGenresResponse>>()
        getSimilarMoviesInteractor.invoke(movieId).toList(results)

        val expectedResponse = MoviesWithGenresResponse(
            movies = listOf(
                MovieWithGenre(
                    id = 1L,
                    title = "Movie Title",
                    posterPath = "/path",
                    voteAverage = 8.0,
                    genreNames = listOf("Action", "Adventure")
                )
            )
        )

        assertEquals(1, results.size)
        assertEquals(Result.success(expectedResponse), results[0])

        val request: RecordedRequest = mockWebServer.takeRequest()
        assertEquals("/movie/$movieId/similar", request.path)
    }

    @Test
    fun `given error response, when getSimilarMovies is called, then emit failure result`() = runBlockingTest {
        val movieId = 1
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        val results = mutableListOf<Result<MoviesWithGenresResponse>>()
        getSimilarMoviesInteractor.invoke(movieId).toList(results)

        assertEquals(1, results.size)
        assert(results[0].isFailure)

        val request: RecordedRequest = mockWebServer.takeRequest()
        assertEquals("/movie/$movieId/similar", request.path)
    }

    @Test
    fun `given IOException, when getSimilarMovies is called, then retry and emit failure result`() = runBlockingTest {
        val movieId = 1
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        val results = mutableListOf<Result<MoviesWithGenresResponse>>()
        getSimilarMoviesInteractor.invoke(movieId).toList(results)

        assertEquals(1, results.size)
        assert(results[0].isFailure)

        val request: RecordedRequest = mockWebServer.takeRequest()
        assertEquals("/movie/$movieId/similar", request.path)
    }
}
