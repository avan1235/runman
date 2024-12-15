package `in`.procyk.runman.vm

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.procyk.runman.PlayingState
import `in`.procyk.runman.isPlaying
import `in`.procyk.runman.radio.RadioSource
import `in`.procyk.runman.radio.RadioSourceFactory
import `in`.procyk.runman.radio.play
import `in`.procyk.runman.radio.rmf.RMFRadioSourceFactory
import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.reflect.KProperty0

internal class RadioViewModel : ViewModel(), KoinComponent {

    private val client by inject<HttpClient>()

    val knownRadioSources: List<RadioSource> =
        KNOWN_RADIO_SOURCE_FACTORIES.map { it.create(client, viewModelScope) }

    private val _radioSource = MutableStateFlow(knownRadioSources.first())

    private val _radioName = MutableStateFlow<String?>(null)
    val radioName: StateFlow<String?> = _radioName.asStateFlow()

    private val _radioCoverUrl = MutableStateFlow<String?>(null)
    val radioCoverUrl: StateFlow<String?> = _radioCoverUrl.asStateFlow()

    private val _radioAudioUrl = MutableStateFlow<String?>(null)
    val radioAudioUrl: StateFlow<String?> = _radioAudioUrl.asStateFlow()

    private var _playingState = MutableStateFlow<PlayingState>(PlayingState.Stopped)
    val playingState: StateFlow<PlayingState> = _playingState.asStateFlow()

    init {
        viewModelScope.launch {
            var job: Job? = null
            _radioSource.collect { radio ->
                job?.cancel()
                job = launch {

                    launchCollectingAsState(radio.getName(), ::_radioName)
                    launchCollectingAsState(radio.getAudioUrl(), ::_radioAudioUrl)
                    launchCollectingAsState(radio.getCoverUrl(), ::_radioCoverUrl)
                }
            }
        }
    }

    fun onSourceSelected(source: RadioSource) {
        if (_radioSource.value == source) return

        val isPlaying = _playingState.value.isPlaying
        if (isPlaying) onPlayStop()

        _radioSource.update { source }
        if (isPlaying) onPlayStop()
    }

    fun onPlayStop() {
        val sourceUrl = _radioAudioUrl.value ?: return
        _playingState.update { state ->
            when (state) {
                PlayingState.Stopped -> PlayingState.Starting(
                    job = viewModelScope.launch {
                        play(
                            sourceUrl = sourceUrl,
                            onStartPlaying = { _playingState.update { it.onStartPlaying() } },
                            onStopPlaying = { _playingState.update { onStopPlaying() } },
                        )
                    },
                )

                is PlayingState.Playing, is PlayingState.Starting -> onStopPlaying()
            }
        }
    }

    private fun onStopPlaying(): PlayingState.Stopped {
        _playingState.value.onStopPlaying()
        return PlayingState.Stopped
    }
}

private inline fun <T : Any> CoroutineScope.launchCollectingAsState(
    flow: Flow<T>,
    prop: KProperty0<MutableStateFlow<T?>>,
) {
    launch {
        flow.collectLatest {
            prop.get().value = it
        }
    }
}

private val KNOWN_RADIO_SOURCE_FACTORIES: List<RadioSourceFactory> = listOf(
    RMFRadioSourceFactory(
        name = "RMF FM",
        soundtrackUrl = "https://rs103-krk.rmfstream.pl/RMFFM48",
        coverUpdateUrl = "https://www.rmfon.pl/stacje/playlista_5.json.txt",
    ),
    RMFRadioSourceFactory(
        name = "RMF MAXX",
        soundtrackUrl = "https://rs201-krk-cyfronet.rmfstream.pl/RMFMAXXX48",
        coverUpdateUrl = "https://www.rmfon.pl/stacje/playlista_6.json.txt",
    ),
    RMFRadioSourceFactory(
        name = "RMF ROCK",
        soundtrackUrl = "https://rs201-krk-cyfronet.rmfstream.pl/ROCK",
        coverUpdateUrl = "https://www.rmfon.pl/stacje/playlista_6.json.txt",
    ),
)
