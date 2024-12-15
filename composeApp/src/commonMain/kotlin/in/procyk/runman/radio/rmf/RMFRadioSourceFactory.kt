package `in`.procyk.runman.radio.rmf

import `in`.procyk.runman.radio.RadioSourceFactory
import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope

internal class RMFRadioSourceFactory(
    private val name: String,
    private val soundtrackUrl: String,
    private val coverUpdateUrl: String,
) : RadioSourceFactory {
    override fun create(client: HttpClient, viewmodelScope: CoroutineScope) =
        RMFRadioSource(name, soundtrackUrl, coverUpdateUrl, client, viewmodelScope)
}