package co.aquidigital.features.movies.domain.interactors

import co.aquidigital.features.movies.domain.model.TopMovieDomainModel
import co.aquidigital.features.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import okio.IOException

fun interface GetMovieDetailsInteractor : (Int) -> Flow<Result<TopMovieDomainModel>>

fun getMovieDetails(
    movieId: Int,
    repository: MoviesRepository
): Flow<Result<TopMovieDomainModel>> =
    repository.getMovieDetails(movieId)
        .map { movieDomainModel ->
            Result.success(movieDomainModel)
        }
        .retryWhen { cause, _ ->
            if (cause is IOException) {
                emit(Result.failure(cause))
                delay(5000L)
                true
            } else {
                false
            }
        }
        .catch { throwable ->
            emit(Result.failure(throwable))
        }
