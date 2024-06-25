package co.aquidigital.features.movies.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import co.aquidigital.features.movies.domain.model.TopMovieCachedModel
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    @Query("SELECT * FROM TopMovieCachedModel")
    fun getTopRatedMovies(): Flow<List<TopMovieCachedModel>>

    @Upsert
    suspend fun saveTopRatedMovies(movies: List<TopMovieCachedModel>)
}
