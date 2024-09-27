package `in`.procyk.runman.radio

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.*
import org.koin.java.KoinJavaComponent.inject

internal actual suspend fun play(
    sourceUrl: String,
    onStartPlaying: () -> Unit,
    onStopPlaying: () -> Unit,
) {
    val context by inject<Context>(Context::class.java)
    val player = withContext(Dispatchers.Main) {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                val mediaItem = MediaItem.fromUri(sourceUrl)
                addListener(
                    object : Player.Listener {
                        override fun onIsPlayingChanged(isPlaying: Boolean) = when (isPlaying) {
                            true -> onStartPlaying()
                            false -> onStopPlaying()
                        }
                    },
                )
                addMediaItem(mediaItem)
                prepare()
                playWhenReady = true
            }
    }
    try {
        awaitCancellation()
    } finally {
        GlobalScope.launch(Dispatchers.Main) {
            player.release()
        }
    }
}