package `in`.procyk.runman.radio

import io.ktor.utils.io.*

internal expect suspend fun play(bytes: ByteReadChannel, onStartPlaying: () -> Unit)