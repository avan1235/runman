package `in`.procyk.runman.radio

import kotlinx.coroutines.await
import kotlinx.coroutines.awaitCancellation
import org.w3c.dom.Audio

internal actual suspend fun play(sourceUrl: String, onStartPlaying: () -> Unit, onStopPlaying: () -> Unit) {
    val audio = Audio(sourceUrl)
    audio.play().await<Nothing?>()
    onStartPlaying()
    try {
        awaitCancellation()
    } finally {
        audio.pause()
    }
}