package co.aquidigital.features.movies.presentation.di

import co.aquidigital.core.navigation.NavigationFactory
import co.aquidigital.features.movies.presentation.navigation.NavigationFactoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Singleton
    @Binds
    @IntoSet
    fun bindNavigationFactory(factory: NavigationFactoryImpl): NavigationFactory
}