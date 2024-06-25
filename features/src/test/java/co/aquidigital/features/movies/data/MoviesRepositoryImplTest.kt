package co.aquidigital.features.movies.data

import co.aquidigital.core.BuildConfig
import co.aquidigital.features.movies.data.local.MoviesDao
import co.aquidigital.features.movies.data.mapper.DataMapper.toEntityModel
import co.aquidigital.features.movies.data.models.MovieWithGenre
import co.aquidigital.features.movies.data.models.MoviesWithGenresResponse
import co.aquidigital.features.movies.data.remote.MoviesApi
import co.aquidigital.features.movies.data.repository.MoviesRepositoryImpl
import co.aquidigital.features.movies.domain.model.TopMovieDomainModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
class MoviesRepositoryImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var moviesApi: MoviesApi
    private lateinit var moviesRepository: MoviesRepositoryImpl
    private val moviesDao: MoviesDao = mockk(relaxed = true)

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

        moviesRepository = MoviesRepositoryImpl(moviesApi, moviesDao)
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

        val result = moviesRepository.getTopRatedMovies().first()
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

        assertEquals(expectedMovies, result)

        val request: RecordedRequest = mockWebServer.takeRequest()
        assertEquals("/movie/top_rated", request.path)
    }

    @Test
    fun `given success response, when getMovieDetails is called, then emit success result`() = runTest {
        val movieId = 1
        val movieJson = """
            {
                "id": 1,
                "title": "Movie Title",
                "overview": "Movie Overview",
                "poster_path": "/path",
                "backdrop_path": "/backdrop",
                "vote_average": 8.0
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(movieJson).setResponseCode(200))

        val result = moviesRepository.getMovieDetails(movieId).first()
        val expectedMovie = TopMovieDomainModel(
            id = 1,
            title = "Movie Title",
            overview = "Movie Overview",
            posterPath = "/path",
            backdropPath = "/backdrop",
            voteAverage = 8.0
        )

        assertEquals(expectedMovie, result)

        val request: RecordedRequest = mockWebServer.takeRequest()
        assertEquals("/movie/$movieId", request.path)
    }

    @Test
    fun `given success response, when getSimilarMovies is called, then emit success result`() = runTest {
        val movieId = 1
        val similarMoviesJson = """
            {
                "results": [
                    {
                        "id": 1,
                        "title": "Movie Title",
                        "poster_path": "/path",
                        "vote_average": 8.0,
                        "genre_ids": [28, 12]
                    }
                ]
            }
        """.trimIndent()

        val genresJson = """
            {
                "genres": [
                    { "id": 28, "name": "Action" },
                    { "id": 12, "name": "Adventure" }
                ]
            }
        """.trimIndent()

        mockWebServer.enqueue(MockResponse().setBody(genresJson).setResponseCode(200))
        mockWebServer.enqueue(MockResponse().setBody(similarMoviesJson).setResponseCode(200))

        val result = moviesRepository.getSimilarMovies(movieId).first()
        val expectedMoviesWithGenresResponse = MoviesWithGenresResponse(
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

        assertEquals(expectedMoviesWithGenresResponse, result)

        val requestGenres: RecordedRequest = mockWebServer.takeRequest()
        assertEquals("/genre/movie/list?api_key=${BuildConfig.API_KEY}&language=en-US", requestGenres.path)

        val requestSimilarMovies: RecordedRequest = mockWebServer.takeRequest()
        assertEquals("/movie/$movieId/similar?api_key=${BuildConfig.API_KEY}", requestSimilarMovies.path)
    }

    @Test
    fun `given success response, when refreshTopRated is called, then save movies to dao`() = runTest {
        val topRatedMoviesJson = """
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

        mockWebServer.enqueue(MockResponse().setBody(topRatedMoviesJson).setResponseCode(200))

        moviesRepository.refreshTopRated()

        val expectedMovies = listOf(
            TopMovieDomainModel(
                id = 1,
                title = "Movie Title",
                overview = "Movie Overview",
                posterPath = "/path",
                backdropPath = "/backdrop",
                voteAverage = 8.0
            ).toEntityModel()
        )

        coVerify { moviesDao.saveTopRatedMovies(expectedMovies) }

        val request: RecordedRequest = mockWebServer.takeRequest()
        assertEquals("/movie/top_rated", request.path)
    }
}