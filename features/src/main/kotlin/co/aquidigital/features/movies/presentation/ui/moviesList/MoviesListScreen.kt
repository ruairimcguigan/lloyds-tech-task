package co.aquidigital.features.movies.presentation.ui.moviesList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import co.aquidigital.core.navigation.NavigationDestination
import co.aquidigital.core.utils.collectWithLifecycle
import co.aquidigital.features.R
import co.aquidigital.features.movies.presentation.composable.ErrorContent
import co.aquidigital.features.movies.presentation.composable.LoadingState
import co.aquidigital.features.movies.presentation.composable.MoviesListContent
import kotlinx.coroutines.flow.Flow

@Composable
fun MoviesListRoute(viewModel: MoviesListViewModel = hiltViewModel(), navController: NavController) {
    HandleEvents(viewModel.getEvents(), navController)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MoviesListScreen(
        uiState = uiState,
        onIntent = viewModel::acceptIntent,
    )
}

@Composable
internal fun MoviesListScreen(
    uiState: MoviesListUiState,
    onIntent: (MoviesListIntent) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val pullToRefreshState = rememberPullToRefreshState()

    HandlePullToRefresh(
        pullState = pullToRefreshState,
        uiState = uiState,
        onIntent = onIntent,
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .nestedScroll(pullToRefreshState.nestedScrollConnection),
        ) {
            if (uiState.topMovies.isNotEmpty()) {
                MoviesAvailableContent(
                    snackbarHostState = snackbarHostState,
                    uiState = uiState,
                    onMovieClicked = { onIntent(MoviesListIntent.OnMovieClicked(it)) },
                )
            } else {
                MoviesNotAvailableContent(
                    uiState = uiState,
                )
            }

            PullToRefreshContainer(
                state = pullToRefreshState,
                modifier = Modifier
                    .align(Alignment.TopCenter),
            )
        }
    }
}

@Composable
private fun HandlePullToRefresh(
    pullState: PullToRefreshState,
    uiState: MoviesListUiState,
    onIntent: (MoviesListIntent) -> Unit,
) {
    if (pullState.isRefreshing) {
        LaunchedEffect(onIntent) {
            onIntent(MoviesListIntent.RefreshMovies)
        }
    }

    if (uiState.isLoading.not()) {
        LaunchedEffect(true) {
            pullState.endRefresh()
        }
    }
}

@Composable
private fun HandleEvents(events: Flow<MoviesListEvent>, navController: NavController) {
    events.collectWithLifecycle {
        when (it) {
            is MoviesListEvent.OpenMovieDetails -> {
                navController.navigate(NavigationDestination.MovieDetailRoute.createRoute(it.id))
            }
            MoviesListEvent.refreshMoviesList -> {
                // Handle refresh movies list event if needed
            }
        }
    }
}

@Composable
private fun MoviesAvailableContent(
    snackbarHostState: SnackbarHostState,
    uiState: MoviesListUiState,
    onMovieClicked: (Int) -> Unit,
) {
    if (uiState.isError) {
        val errorMessage = stringResource(R.string.movies_error_refreshing)

        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = errorMessage,
            )
        }
    }

    MoviesListContent(
        movieList = uiState.topMovies,
        onMovieClicked = onMovieClicked,
    )
}

@Composable
private fun MoviesNotAvailableContent(uiState: MoviesListUiState) {
    when {
        uiState.isLoading -> LoadingState()
        uiState.isError -> ErrorContent()
    }
}
