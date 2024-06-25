import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import co.aquidigital.core.BuildConfig
import co.aquidigital.core.design.Typography
import co.aquidigital.features.R
import co.aquidigital.features.movies.presentation.model.TopMovieUi
import coil.compose.AsyncImage

@Composable
fun MovieItem(
    movieUi: TopMovieUi,
    modifier: Modifier = Modifier,
    onMovieClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .background(color = Color.White)
            .padding(
                horizontal = dimensionResource(id = R.dimen.dimen_small),
                vertical = dimensionResource(id = R.dimen.dimen_xxxlarge),
            )
            .clickable { onMovieClicked() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier = Modifier
                .align(Alignment.Top)
                .padding(end = dimensionResource(id = R.dimen.dimen_large))
                .weight(1f),
            model = buildMovieImage(movieUi),
            contentScale = ContentScale.Fit,
            contentDescription = null,
        )

        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(id = R.dimen.dimen_small),
            ),
        ) {
            Text(
                text = movieUi.title,
                style = Typography.titleMedium,
            )
            Text(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.dimen_small)),
                text = movieUi.overview, // Use the actual value here
                style = Typography.bodyMedium,
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.dimen_medium))) // Adjust the radius as needed
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.dimen_medium),
                        vertical = dimensionResource(id = R.dimen.dimen_small)
                    ) 
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = movieUi.voteAverage?.toString() ?: "",
                        style = Typography.bodyMedium, color = Color.White,
                    )
                }
            }
        }
    }
}

private fun buildMovieImage(movie: TopMovieUi): String = buildString {
    append(BuildConfig.BASE_IMAGE_URL)
    append(movie.posterPath)
}