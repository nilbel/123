package com.example.cinematv.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.cinematv.data.repository.MovieRepository
import com.example.cinematv.data.repository.TorrentRepository
import com.example.cinematv.data.settings.AppSettings
import com.example.cinematv.ui.details.DetailsScreen
import com.example.cinematv.ui.home.HomeScreen
import com.example.cinematv.ui.player.PlayerScreen
import com.example.cinematv.ui.search.SearchScreen
import com.example.cinematv.ui.settings.SettingsScreen
import java.net.URLDecoder
import java.net.URLEncoder

object Routes {
    const val HOME = "home"
    const val SEARCH = "search"
    const val SETTINGS = "settings"
    const val DETAILS = "details/{movieId}"
    const val PLAYER = "player/{streamUrl}"

    fun details(movieId: Int) = "details/$movieId"
    fun player(streamUrl: String) = "player/${URLEncoder.encode(streamUrl, "UTF-8")}"
}

@Composable
fun CinemaNavGraph(
    modifier: Modifier = Modifier,
    movieRepository: MovieRepository,
    torrentRepository: TorrentRepository,
    settings: AppSettings,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Routes.HOME, modifier = modifier) {

        composable(Routes.HOME) {
            HomeScreen(
                repository = movieRepository,
                onMovieClick = { movie -> navController.navigate(Routes.details(movie.id)) },
                onSearchClick = { navController.navigate(Routes.SEARCH) },
                onSettingsClick = { navController.navigate(Routes.SETTINGS) }
            )
        }

        composable(Routes.SEARCH) {
            SearchScreen(
                repository = movieRepository,
                onMovieClick = { movie -> navController.navigate(Routes.details(movie.id)) }
            )
        }

        composable(
            route = Routes.DETAILS,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
            DetailsScreen(
                movieId = movieId,
                movieRepository = movieRepository,
                torrentRepository = torrentRepository,
                onPlay = { streamUrl -> navController.navigate(Routes.player(streamUrl)) }
            )
        }

        composable(
            route = Routes.PLAYER,
            arguments = listOf(navArgument("streamUrl") { type = NavType.StringType })
        ) { backStackEntry ->
            val encoded = backStackEntry.arguments?.getString("streamUrl") ?: return@composable
            PlayerScreen(streamUrl = URLDecoder.decode(encoded, "UTF-8"))
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(settings = settings, torrentRepository = torrentRepository)
        }
    }
}
