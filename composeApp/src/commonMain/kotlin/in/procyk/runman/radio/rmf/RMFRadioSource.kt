package `in`.procyk.runman.radio.rmf

import `in`.procyk.runman.radio.RadioSource
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

internal data class RMFRadioSource(
    private val name: String,
    private val soundtrackUrl: String,
    private val coverUpdateUrl: String,
    private val client: HttpClient,
    private val viewModelScope: CoroutineScope
) : RadioSource {
    private val dataUrl: SharedFlow<RMFPlaylistItem> = flow {
        while (currentCoroutineContext().isActive) {
            val response = client.get(coverUpdateUrl).body<String>()
            val playlist = Json.decodeFromString<RMFPlaylist>(response)
            val current = playlist.items
                .sortedBy { it.timestamp }
                .findLast { it.timestamp?.let { it <= Clock.System.now() } == true }
            if (current != null) {
                emit(current)
            }
            delay(1.seconds)
        }
    }.shareIn(viewModelScope, started = SharingStarted.WhileSubscribed())

    override fun getAudioUrl() =
        flowOf(soundtrackUrl)

    override fun getName() =
        flowOf(name)

    override fun getCoverUrl() =
        dataUrl.mapNotNull { it.coverBigUrl ?: it.coverUrl }
}