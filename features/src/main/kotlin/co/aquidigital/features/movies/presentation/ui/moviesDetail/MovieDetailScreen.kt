package co.aquidigital.features.movies.presentation.ui.moviesDetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.aquidigital.core.BuildConfig
import co.aquidigital.features.R
import co.aquidigital.features.movies.presentation.composable.ErrorState
import co.aquidigital.features.movies.presentation.composable.LoadingState
import co.aquidigital.features.movies.presentation.composable.MovieCarousel
import coil.compose.AsyncImage

@Composable
fun MovieDetailsScreen(uiState: MovieDetailsUiState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.dimen_medium))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_medium)))
            Text(
                text = uiState.movie.title,
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_medium)))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_xlarge)))
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(bottom = dimensionResource(id = R.dimen.dimen_large)),
                model = uiState.movie.posterPath?.let { buildMovieImage(it) },
                contentScale = ContentScale.Fit,
                contentDescription = null,
            )
            Text(
                text = "Overview",
                style = TextStyle(fontSize = 21.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_medium)))
            HorizontalDivider()
            Text(
                text = uiState.movie.overview,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.dimen_large))
            )
        }
    }
}
@Composable
fun MovieDetailsRoute(movieId: Int, viewModel: MovieDetailViewModel = hiltViewModel()) {
    viewModel.observeMovieDetails(movieId)
    viewModel.observeSimilarMovies(movieId)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(it)
        ) {
            when {
                uiState.isLoading -> LoadingState()
                uiState.isError -> ErrorState(message = "Error loading movie details")
                else -> {
                    uiState.movie.let { movie ->
                        MovieDetailsScreen(uiState = uiState)
                        SimilarMoviesSection(uiState)
                    }
                }
            }
        }
    }
}

@Composable
fun SimilarMoviesSection(uiState: MovieDetailsUiState) {
    when {
        uiState.isSimilarMoviesLoading -> LoadingState()
        uiState.isSimilarMoviesError -> ErrorState(message = "Error loading similar movies")
        uiState.similarMovies.isNotEmpty() -> {
            Text(
                text = "Similar Movies",
                style = TextStyle(fontSize = 21.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            HorizontalDivider()
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_medium)))
            MovieCarousel(uiState.similarMovies)
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dimen_xxxlarge)))
        }
    }
}

fun buildMovieImage(posterPath: String?): String = buildString {
    append(BuildConfig.BASE_IMAGE_URL)
    append(posterPath)
}
