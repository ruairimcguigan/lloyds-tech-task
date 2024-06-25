package co.aquidigital.features.movies.presentation.di

import co.aquidigital.features.movies.presentation.ui.moviesList.MoviesListUiState
import co.aquidigital.features.movies.presentation.ui.moviesDetail.MovieDetailsUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal object ViewModelModule {

    @Provides
    fun provideMovieUiState(): MoviesListUiState = MoviesListUiState()

    @Provides
    fun provideMovieDetailState(): MovieDetailsUiState = MovieDetailsUiState()
}