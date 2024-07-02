package co.aquidigital.features.movies.domain.interactors

import co.aquidigital.features.movies.domain.model.TopMovieDomainModel
import co.aquidigital.features.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException

private const val RETRY_TIME_IN_MILLIS = 5000L

fun interface GetTopRatedMoviesInteractor : () -> Flow<Result<List<TopMovieDomainModel>>>

fun getTopRatedMovies(
    repository: MoviesRepository
): Flow<Result<List<TopMovieDomainModel>>> =
    repository
        .getTopRatedMovies()
        .map {
            Result.success(it)
        }
        .retryWhen { cause, _ ->
            if (cause is IOException) {
                emit(Result.failure(cause))

                delay(RETRY_TIME_IN_MILLIS)
                true
            } else {
                false
            }
        }
        .catch { // for other than IOException but it will stop collecting Flow
            emit(Result.failure(it)) // also catch does re-throw CancellationException
        }