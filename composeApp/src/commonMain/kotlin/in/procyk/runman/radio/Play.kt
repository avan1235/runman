package `in`.procyk.runman.radio

internal expect suspend fun play(
    sourceUrl: String,
    onStartPlaying: () -> Unit,
    onStopPlaying: () -> Unit,
)