package co.aquidigital.features.movies.domain.di

import co.aquidigital.features.movies.data.repository.MoviesRepositoryImpl
import co.aquidigital.features.movies.domain.interactors.GetMovieDetailsInteractor
import co.aquidigital.features.movies.domain.interactors.GetSimilarMoviesInteractor
import co.aquidigital.features.movies.domain.interactors.GetTopRatedMoviesInteractor
import co.aquidigital.features.movies.domain.interactors.getMovieDetails
import co.aquidigital.features.movies.domain.interactors.getSimilarMovies
import co.aquidigital.features.movies.domain.interactors.getTopRatedMovies
import co.aquidigital.features.movies.domain.repository.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Provides
    fun provideGetTopRatedMoviesInteractor(repository: MoviesRepository): GetTopRatedMoviesInteractor {
        return GetTopRatedMoviesInteractor {
            getTopRatedMovies(repository)
        }
    }

    @Provides
    fun provideGetMovieDetailsInteractor(
        repository: MoviesRepository
    ): GetMovieDetailsInteractor {
        return GetMovieDetailsInteractor { movieId ->
            getMovieDetails(movieId, repository)
        }
    }

    @Provides
    fun provideGetSimilarMoviesInteractor(repository: MoviesRepository): GetSimilarMoviesInteractor {
        return GetSimilarMoviesInteractor { movieId ->
            getSimilarMovies(movieId = movieId, repository = repository)
        }
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {

        @Binds
        @Singleton
        fun bindMoviesRepository(impl: MoviesRepositoryImpl): MoviesRepository
    }
}