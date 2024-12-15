package `in`.procyk.runman.radio

import kotlinx.coroutines.flow.Flow

internal interface RadioSource {
    fun getAudioUrl(): Flow<String>

    fun getCoverUrl(): Flow<String>

    fun getName(): Flow<String>
}