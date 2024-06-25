package co.aquidigital.features.movies.data.repository

import co.aquidigital.core.BuildConfig
import co.aquidigital.features.movies.data.local.MoviesDao
import co.aquidigital.features.movies.data.mapper.DataMapper.toDomainModel
import co.aquidigital.features.movies.data.mapper.DataMapper.toEntityModel
import co.aquidigital.features.movies.data.models.MovieWithGenre
import co.aquidigital.features.movies.data.models.MoviesWithGenresResponse
import co.aquidigital.features.movies.data.remote.MoviesApi
import co.aquidigital.features.movies.domain.model.TopMovieDomainModel
import co.aquidigital.features.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val movieApi: MoviesApi,
    private val movieDao: MoviesDao,
) : MoviesRepository {

    override fun getTopRatedMovies(): Flow<List<TopMovieDomainModel>> {
        return movieDao
            .getTopRatedMovies()
            .map { cached ->
                cached.map { it.toDomainModel() }
            }
            .onEach { movies ->
                if (movies.isEmpty()) {
                    refreshTopRated()
                }
            }
    }

    override fun getMovieDetails(movieId: Int): Flow<TopMovieDomainModel> {
        return flow {
            val movieDataModel = movieApi.getMovieById(movieId)
            emit(movieDataModel.toDomainModel())
        }
    }


    override fun getSimilarMovies(
        movieId: Int
    ): Flow<MoviesWithGenresResponse> = flow {
        val genres = movieApi.getMovieGenres(BuildConfig.API_KEY,"en-US").genres
        val genreMap: Map<Int, String?> = genres.associateBy({ it.id }, { it.name })

        val similarMovies = movieApi.getSimilarMovies(movieId, BuildConfig.API_KEY).results
        val moviesWithGenres = similarMovies.map { movie ->
            val genreNames = movie.genre_ids.map { genreId -> genreMap[genreId] }
            MovieWithGenre(
                id = movie.id.toLong(),
                title = movie.title,
                posterPath = movie.poster_path,
                voteAverage = movie.vote_average,
                genreNames = genreNames
            )
        }
        emit(MoviesWithGenresResponse(moviesWithGenres))
    }


    override suspend fun refreshTopRated() {
        movieApi.getTopRated().results
            .map {
                it.toDomainModel().toEntityModel()
            }
            .also {
                movieDao.saveTopRatedMovies(it)
            }
    }
}
