package co.aquidigital.database

import android.content.Context
import androidx.room.Room
import co.aquidigital.features.movies.data.local.MoviesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val APP_DATABASE_NAME = "movies_db"

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            APP_DATABASE_NAME,
        ).build()
    }

    @Singleton
    @Provides
    fun provideDao(database: AppDatabase): MoviesDao {
        return database.movieDao()
    }
}
