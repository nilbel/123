package com.example.cinematv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.cinematv.ui.navigation.CinemaNavGraph
import com.example.cinematv.ui.theme.CinemaTvTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as CinemaApp

        setContent {
            CinemaTvTheme {
                CinemaNavGraph(
                    modifier = Modifier.fillMaxSize(),
                    movieRepository = app.movieRepository,
                    torrentRepository = app.torrentRepository,
                    settings = app.settings
                )
            }
        }
    }
}
