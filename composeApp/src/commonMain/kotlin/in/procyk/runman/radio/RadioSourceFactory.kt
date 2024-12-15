package `in`.procyk.runman.radio

import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope

internal interface RadioSourceFactory {
    fun create(client: HttpClient, viewmodelScope: CoroutineScope): RadioSource
}