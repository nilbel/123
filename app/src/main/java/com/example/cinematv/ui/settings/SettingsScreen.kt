package com.example.cinematv.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.Text
import com.example.cinematv.data.repository.TorrentRepository
import com.example.cinematv.data.settings.AppSettings
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(settings: AppSettings, torrentRepository: TorrentRepository) {
    val scope = rememberCoroutineScope()

    var torrServerUrl by remember { mutableStateOf("") }
    var tmdbKey by remember { mutableStateOf("") }
    var connectionStatus by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        settings.torrServerUrl.collect { torrServerUrl = it }
    }
    LaunchedEffect(Unit) {
        settings.tmdbApiKey.collect { tmdbKey = it }
    }

    Column(Modifier.fillMaxSize().padding(24.dp)) {
        Text("Настройки")
        Spacer(Modifier.height(24.dp))

        Text("Адрес TorrServer (например http://192.168.1.50:8090/)")
        androidx.compose.material3.OutlinedTextField(
            value = torrServerUrl,
            onValueChange = { torrServerUrl = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Row {
            Button(onClick = { scope.launch { settings.setTorrServerUrl(torrServerUrl) } }) {
                Text("Сохранить адрес")
            }
            Spacer(Modifier.width(12.dp))
            Button(onClick = {
                scope.launch {
                    settings.setTorrServerUrl(torrServerUrl)
                    connectionStatus = if (torrentRepository.checkConnection()) "Подключено ✓" else "Не удалось подключиться"
                }
            }) {
                Text("Проверить соединение")
            }
        }
        connectionStatus?.let { Text(it) }

        Spacer(Modifier.height(32.dp))

        Text("Ключ TMDB API (получить бесплатно на themoviedb.org/settings/api)")
        androidx.compose.material3.OutlinedTextField(
            value = tmdbKey,
            onValueChange = { tmdbKey = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = { scope.launch { settings.setTmdbApiKey(tmdbKey) } }) {
            Text("Сохранить ключ")
        }
    }
}
