package co.aquidigital.features.movies.presentation.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.aquidigital.features.R
import co.aquidigital.features.movies.data.models.MovieWithGenre
import co.aquidigital.features.movies.presentation.ui.moviesDetail.buildMovieImage
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieCarousel(similarMovies: List<MovieWithGenre>) {
    val pagerState = rememberPagerState(pageCount = { similarMovies.size })

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
    ) { page ->
        val movie = similarMovies[page]
        Column(
            modifier = Modifier
                .padding(8.dp),
        ) {
            AsyncImage(
                model = buildMovieImage(movie.posterPath),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_xxxlarge))
                    .height(200.dp)  // Set a fixed height for the image
            )
            Text(
                text = movie.title,
                style = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_xxxlarge))
                    .padding(top = dimensionResource(id = R.dimen.dimen_medium))
            )
            Text(
                text = "Rating: ${movie.voteAverage}",
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .padding(vertical = dimensionResource(id = R.dimen.dimen_small))
                    .padding(horizontal = dimensionResource(id = R.dimen.dimen_xxxlarge))
            )
        }
    }
}
