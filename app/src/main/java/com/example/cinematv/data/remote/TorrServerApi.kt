package com.example.cinematv.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Client for a self-hosted TorrServer instance (https://github.com/YouROK/TorrServer).
 * The base URL is NOT hardcoded — it's whatever the user enters in Settings
 * (e.g. http://192.168.1.50:8090/ or a remote address if they run it on a VPS).
 * See TorrServerServiceFactory for how the base URL is swapped at runtime.
 */
interface TorrServerApi {

    /** Simple reachability check, hits TorrServer's /echo endpoint. */
    @GET("echo")
    suspend fun ping(): String

    /**
     * Adds a magnet/torrent link and asks TorrServer to start fetching metadata.
     * Response contains the torrent "hash" you need to build a stream URL.
     */
    @POST("torrents")
    suspend fun addTorrent(@Body request: AddTorrentRequest): TorrentInfo

    /** Lists files inside an already-added torrent so the user can pick the right one
     *  (useful when a release contains multiple episodes/qualities). */
    @POST("torrents")
    suspend fun getTorrentInfo(@Body request: GetTorrentRequest): TorrentInfo
}

data class AddTorrentRequest(
    val action: String = "add",
    val link: String,     // magnet: link or .torrent URL
    val save: Boolean = true
)

data class GetTorrentRequest(
    val action: String = "get",
    val hash: String
)

data class TorrentInfo(
    val hash: String,
    val title: String?,
    val stat: Int?,
    val file_stats: List<FileStat>?
)

data class FileStat(
    val id: Int,
    val path: String,
    val length: Long
)

/** Builds the direct-play stream URL TorrServer exposes once a torrent is added.
 *  This is what gets handed straight to ExoPlayer. */
fun buildStreamUrl(baseUrl: String, hash: String, fileIndex: Int, fileName: String): String {
    val normalizedBase = baseUrl.trimEnd('/')
    val encodedName = java.net.URLEncoder.encode(fileName, "UTF-8")
    return "$normalizedBase/stream/$encodedName?link=$hash&index=$fileIndex&play"
}
