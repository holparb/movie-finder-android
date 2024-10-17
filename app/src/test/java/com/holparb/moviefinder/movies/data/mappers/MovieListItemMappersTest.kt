package com.holparb.moviefinder.movies.data.mappers

import android.util.Log
import com.holparb.moviefinder.movies.data.datasource.remote.TmdbApi
import com.holparb.moviefinder.movies.data.dto.MovieListItemDto
import com.holparb.moviefinder.movies.data.entity.MovieEntity
import com.holparb.moviefinder.movies.domain.model.MovieListItem
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RunWith(JUnit4::class)
class MovieListItemMappersTest {

    private fun assertEntitiesAreEqual(entity1: MovieEntity, entity2: MovieEntity) {
        Assert.assertEquals(entity1.id, entity2.id)
        Assert.assertEquals(entity1.title, entity2.title)
        Assert.assertEquals(entity1.overview, entity2.overview)
        Assert.assertEquals(entity1.backdropPath, entity2.backdropPath)
        Assert.assertEquals(entity1.posterPath, entity2.posterPath)
        Assert.assertEquals(entity1.genreIds, entity2.genreIds)
        Assert.assertEquals(entity1.rating, entity2.rating)
        Assert.assertEquals(entity1.releaseDate, entity2.releaseDate)
    }

    @Test
    fun map_dto_to_entity_with_non_null_values() {
        val dto = MovieListItemDto(id = 1, title = "title", overview = "overview", backdropPath = "/path", posterPath = "/path", genreIds = listOf(1,2,3), rating = 1.0, releaseDate = "1992-02-23")
        val expectedEntity = MovieEntity(id = 1, title = "title", overview = "overview", backdropPath = "/path", posterPath = "/path", genreIds = listOf(1,2,3), rating = 1.0, releaseDate = "1992-02-23")

        assertEntitiesAreEqual(expectedEntity, dto.toMovieEntity())
    }

    @Test
    fun map_dto_to_entity_with_null_poster_and_backdrop_path() {
        val dto = MovieListItemDto(id = 1, title = "title", overview = "overview", genreIds = listOf(1,2,3), rating = 1.0, releaseDate = "1992-02-23")
        val expectedEntity = MovieEntity(id = 1, title = "title", overview = "overview", backdropPath = null, posterPath = null, genreIds = listOf(1,2,3), rating = 1.0, releaseDate = "1992-02-23")

        assertEntitiesAreEqual(expectedEntity, dto.toMovieEntity())
    }

    @Test
    fun map_dto_to_entity_with_null_release_date() {
        val dto = MovieListItemDto(id = 1, title = "title", overview = "overview", genreIds = listOf(1,2,3), rating = 1.0, releaseDate = null)
        val expectedEntity = MovieEntity(id = 1, title = "title", overview = "overview", backdropPath = null, posterPath = null, genreIds = listOf(1,2,3), rating = 1.0, releaseDate = null)

        assertEntitiesAreEqual(expectedEntity, dto.toMovieEntity())
    }

    @Test
    fun map_dto_to_entity_with_flags_set() {
        val dto = MovieListItemDto(id = 1, title = "title", overview = "overview", genreIds = listOf(1,2,3), rating = 1.0, releaseDate = null)

        var result = dto.toMovieEntity(isPopular = true)
        Assert.assertTrue(result.isPopular)

        result = dto.toMovieEntity(isTopRated = true)
        Assert.assertTrue(result.isTopRated)

        result = dto.toMovieEntity(isUpcoming = true)
        Assert.assertTrue(result.isUpcoming)
    }

    @Test
    fun dto_and_entity_are_mapped_to_same_model() {
        val dto = MovieListItemDto(id = 1, title = "title", overview = "overview", backdropPath = "/path", posterPath = "/path", genreIds = listOf(1,2,3), rating = 1.0, releaseDate = "1992-02-23")
        val entity = MovieEntity(id = 1, title = "title", overview = "overview", backdropPath = "/path", posterPath = "/path", genreIds = listOf(1,2,3), rating = 1.0, releaseDate = "1992-02-23")

        Assert.assertEquals(dto.toMovieListItem(), entity.toMovieListItem())
    }

    @Test
    fun dto_mapped_to_model_correctly() {
        val dto = MovieListItemDto(id = 1, title = "title", overview = "overview", backdropPath = "/path", posterPath = "/path", genreIds = listOf(1,2,3), rating = 1.0, releaseDate = "1992-02-23")
        val expectedModel = MovieListItem(id = 1, title = "title", overview = "overview", backdropPath = TmdbApi.IMAGE_URL_W780 + "/path", posterPath = TmdbApi.IMAGE_URL_W500 + "/path", releaseDate = LocalDate.parse("1992-02-23", DateTimeFormatter.ofPattern("yyyy-MM-dd")))

        Assert.assertEquals(expectedModel, dto.toMovieListItem())
    }

    @Test
    fun dto_mapped_to_model_with_null_backdrop_and_poster_path() {
        val dto = MovieListItemDto(id = 1, title = "title", overview = "overview", genreIds = listOf(1,2,3), rating = 1.0, releaseDate = "1992-02-23")
        val expectedModel = MovieListItem(id = 1, title = "title", overview = "overview", backdropPath = null, posterPath = null, releaseDate = LocalDate.parse("1992-02-23", DateTimeFormatter.ofPattern("yyyy-MM-dd")))

        Assert.assertEquals(expectedModel, dto.toMovieListItem())
    }

    @Test
    fun dto_mapped_to_model_with_null_release_date() {
        val dto = MovieListItemDto(id = 1, title = "title", overview = "overview", genreIds = listOf(1,2,3), rating = 1.0, releaseDate = null)
        val expectedModel = MovieListItem(id = 1, title = "title", overview = "overview", backdropPath = null, posterPath = null, releaseDate = null)

        Assert.assertEquals(expectedModel, dto.toMovieListItem())
    }

    @Test

    fun dto_with_invalid_release_date_is_mapped_minimally() {
        val dto = MovieListItemDto(id = 1, title = "title", overview = "overview", backdropPath = "/path", posterPath = "/path", genreIds = listOf(1,2,3), rating = 1.0, releaseDate = "invalid")
        val expectedModel = MovieListItem(id = 1, title = "title", overview = "overview", backdropPath = null, posterPath = null, releaseDate = null)
        mockkStatic(Log::class)
        every { Log.e(any(), any<String>()) } returns 0
        Assert.assertEquals(expectedModel, dto.toMovieListItem())
    }
}