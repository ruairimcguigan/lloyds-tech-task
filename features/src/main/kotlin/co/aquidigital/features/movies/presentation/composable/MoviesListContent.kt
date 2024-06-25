package co.aquidigital.features.movies.presentation.composable

import MovieItem
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import co.aquidigital.features.R
import co.aquidigital.features.movies.presentation.model.TopMovieUi

const val MOVIE_DIVIDER_TEST_TAG = "movieDividerTestTag"

@Composable
fun MoviesListContent(
    movieList: List<TopMovieUi>,
    modifier: Modifier = Modifier,
    onMovieClicked: (Int) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1), // You can adjust the number of columns
        modifier = modifier
            .padding(
                horizontal = dimensionResource(id = R.dimen.dimen_medium),
            ),
    ) {
        itemsIndexed(
            items = movieList,
            key = { _, movieUi -> movieUi.id },
        ) { index, item ->
            MovieItem(
                movieUi = item,
                onMovieClicked = { onMovieClicked(item.id) },
            )

            if (index < movieList.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.testTag(MOVIE_DIVIDER_TEST_TAG),
                )
            }
        }
    }
}
