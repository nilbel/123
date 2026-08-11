package com.example.cinematv

import android.app.Application
import com.example.cinematv.data.remote.TmdbServiceFactory
import com.example.cinematv.data.repository.MovieRepository
import com.example.cinematv.data.repository.TorrentRepository
import com.example.cinematv.data.settings.AppSettings

class CinemaApp : Application() {

    lateinit var settings: AppSettings
        private set
    lateinit var movieRepository: MovieRepository
        private set
    lateinit var torrentRepository: TorrentRepository
        private set

    override fun onCreate() {
        super.onCreate()
        settings = AppSettings(this)
        movieRepository = MovieRepository(TmdbServiceFactory.create(), settings)
        torrentRepository = TorrentRepository(settings)
    }
}
