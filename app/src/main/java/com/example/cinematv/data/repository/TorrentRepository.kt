package com.example.cinematv.data.repository

import com.example.cinematv.data.remote.AddTorrentRequest
import com.example.cinematv.data.remote.TorrServerServiceFactory
import com.example.cinematv.data.remote.buildStreamUrl
import com.example.cinematv.data.settings.AppSettings
import kotlinx.coroutines.flow.first

class TorrentRepository(private val settings: AppSettings) {

    private suspend fun baseUrl(): String {
        val url = settings.torrServerUrl.first()
        require(url.isNotBlank()) { "TorrServer address is not set — open Settings first" }
        return url
    }

    suspend fun checkConnection(): Boolean = try {
        TorrServerServiceFactory.create(baseUrl()).ping()
        true
    } catch (_: Exception) {
        false
    }

    /**
     * Adds a magnet/torrent link, picks the largest file in it (heuristic: the movie file,
     * not a sample/subtitle), and returns a ready-to-play stream URL.
     * A real "parser" module would call this with the magnet it found on a tracker.
     */
    suspend fun resolveStreamUrl(magnetOrTorrentLink: String): String {
        val url = baseUrl()
        val api = TorrServerServiceFactory.create(url)
        val info = api.addTorrent(AddTorrentRequest(link = magnetOrTorrentLink))
        val biggestFile = info.file_stats
            ?.maxByOrNull { it.length }
            ?: error("TorrServer returned no files for this torrent yet — metadata may still be loading, try again in a few seconds")

        return buildStreamUrl(url, info.hash, biggestFile.id, biggestFile.path.substringAfterLast('/'))
    }
}
