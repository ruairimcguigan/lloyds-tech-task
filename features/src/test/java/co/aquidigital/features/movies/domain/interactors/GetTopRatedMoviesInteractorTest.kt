package co.aquidigital.features.movies.domain.interactors

import co.aquidigital.features.movies.data.remote.MoviesApi
import co.aquidigital.features.movies.data.repository.MoviesRepositoryImpl
import co.aquidigital.features.movies.domain.model.TopMovieDomainModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

@OptIn(ExperimentalSerializationApi::class)
@ExperimentalCoroutinesApi
class GetTopRatedMoviesInteractorTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var moviesApi: MoviesApi
    private lateinit var moviesRepository: MoviesRepositoryImpl
    private lateinit var getTopRatedMoviesInteractor: GetTopRatedMoviesInteractor

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
        getTopRatedMoviesInteractor = GetTopRatedMoviesInteractor {
            getTopRatedMovies(moviesRepository)
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `given success response, when getTopRatedMovies is called, then emit success result`() = runTest {
        val moviesJson = """
            {
                "results": [
                    {
                        "id": 1,
                        "title": "Movie Title",
                        "overview": "Movie Overview",
                        "poster_path": "/path",
                        "backdrop_path": "/backdrop",
                        "vote_average": 8.0
                    }
                ]
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(moviesJson).setResponseCode(200))

        val results = mutableListOf<Result<List<TopMovieDomainModel>>>()
        getTopRatedMoviesInteractor.invoke().toList(results)

        val expectedMovies = listOf(
            TopMovieDomainModel(
                id = 1,
                title = "Movie Title",
                overview = "Movie Overview",
                posterPath = "/path",
                backdropPath = "/backdrop",
                voteAverage = 8.0
            )
        )

        assertEquals(1, results.size)
        assertEquals(Result.success(expectedMovies), results[0])

        val request: RecordedRequest = mockWebServer.takeRequest()
        assertEquals("/movie/top_rated", request.path)
    }

    @Test
    fun `given error response, when getTopRatedMovies is called, then emit failure result`() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        val results = mutableListOf<Result<List<TopMovieDomainModel>>>()
        getTopRatedMoviesInteractor.invoke().toList(results)

        assertEquals(1, results.size)
        assert(results[0].isFailure)

        val request: RecordedRequest = mockWebServer.takeRequest()
        assertEquals("/movie/top_rated", request.path)
    }

    @Test
    fun `given IOException, when getTopRatedMovies is called, then retry and emit failure result`() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        val results = mutableListOf<Result<List<TopMovieDomainModel>>>()
        getTopRatedMoviesInteractor.invoke().toList(results)

        assertEquals(1, results.size)
        assert(results[0].isFailure)

        val request: RecordedRequest = mockWebServer.takeRequest()
        assertEquals("/movie/top_rated", request.path)
    }
}
