package co.aquidigital.features.movies.presentation.mapper

import co.aquidigital.features.movies.domain.model.SimilarMovieDomainModel
import co.aquidigital.features.movies.domain.model.TopMovieDomainModel
import co.aquidigital.features.movies.presentation.model.TopMovieUi
import junit.framework.TestCase.assertEquals
import kotlin.test.Test


class MoviesMapperTest {

    @Test
    fun `given TopMovieDomainModel, when toUiModel is called, then returns TopMovieUi`() {
        val domainModel = TopMovieDomainModel(
            id = 1,
            title = "Movie Title",
            overview = "Movie Overview",
            posterPath = "/path",
            backdropPath = "/backdrop",
            voteAverage = 8.0
        )

        val uiModel = domainModel .toUiModel()

        assertEquals(domainModel.id, uiModel.id)
        assertEquals(domainModel.title, uiModel.title)
        assertEquals(domainModel.overview, uiModel.overview)
        assertEquals(domainModel.posterPath, uiModel.posterPath)
        assertEquals(domainModel.backdropPath, uiModel.backdropPath)
        assertEquals(domainModel.voteAverage, uiModel.voteAverage)
    }

    @Test
    fun `given SimilarMovieDomainModel, when toUiModel is called, then returns SimilarMovieUiModel`() {
        val domainModel = SimilarMovieDomainModel(
            id = 1,
            title = "Similar Movie Title",
            poster_path = "/similar_path",
            vote_average = 7.5,
            genre_ids = listOf(1, 2, 3)
        )

        val uiModel = domainModel.toUiModel()

        assertEquals(domainModel.id, uiModel.id)
        assertEquals(domainModel.title, uiModel.title)
        assertEquals(domainModel.poster_path, uiModel.poster_path)
        assertEquals(domainModel.vote_average, uiModel.vote_average)
        assertEquals(domainModel.genre_ids, uiModel.genre_ids)
    }

    @Test
    fun `given TopMovieUi, when toDomainModel is called, then returns TopMovieDomainModel`() {
        val uiModel = TopMovieUi(
            id = 1,
            title = "Movie Title",
            overview = "Movie Overview",
            posterPath = "/path",
            backdropPath = "/backdrop",
            voteAverage = 8.0
        )

        val domainModel = uiModel.toDomainModel()

        assertEquals(uiModel.id, domainModel.id)
        assertEquals(uiModel.title, domainModel.title)
        assertEquals(uiModel.overview, domainModel.overview)
        assertEquals(uiModel.posterPath, domainModel.posterPath)
        assertEquals(uiModel.backdropPath, domainModel.backdropPath)
        assertEquals(uiModel.voteAverage, domainModel.voteAverage)
    }
}