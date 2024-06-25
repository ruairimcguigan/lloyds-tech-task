package co.aquidigital.database

import androidx.room.Database
import androidx.room.RoomDatabase
import co.aquidigital.features.movies.data.local.MoviesDao
import co.aquidigital.features.movies.domain.model.TopMovieCachedModel

@Database(
    entities = [TopMovieCachedModel::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MoviesDao
}
