package `in`.procyk.runman

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import compose.icons.FeatherIcons
import compose.icons.feathericons.Play
import compose.icons.feathericons.Square
import `in`.procyk.runman.PlayingState.*
import `in`.procyk.runman.radio.play
import `in`.procyk.runman.theme.AppTheme
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun App() = AppTheme {
    val vm = remember { MyViewModel() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val playingState by vm.playingState.collectAsState()
        val source by vm.source.collectAsState()
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            TextField(
                value = source.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Source") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true)
                    .fillMaxWidth(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                RadioSource.KNOWN.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item.name) },
                        onClick = {
                            vm.onSourceSelected(item)
                            expanded = false
                        },
                    )
                }
            }
        }
        when (playingState) {
            is Starting -> Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }

            is Playing, is Canceled -> ElevatedButton(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .widthIn(min = 200.dp),
                onClick = vm::onPlayStop,
                content = {
                    when (playingState) {
                        is Playing -> {
                            Icon(FeatherIcons.Square, contentDescription = "Stop")
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text("Stop")
                        }

                        is Starting, Canceled -> {
                            Icon(FeatherIcons.Play, contentDescription = "Play")
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text("Play")
                        }
                    }
                },
            )
        }
    }
}

internal sealed interface PlayingState {
    fun onStartPlaying(): PlayingState
    fun cancel()
    class Starting(private val job: Job) : PlayingState {
        override fun onStartPlaying() = Playing(job)
        override fun cancel() = job.cancel()
    }

    class Playing(private val job: Job) : PlayingState {
        override fun onStartPlaying() = Canceled
        override fun cancel() = job.cancel()
    }

    data object Canceled : PlayingState {
        override fun onStartPlaying() = Canceled
        override fun cancel() {}
    }
}

internal val PlayingState.isPlaying: Boolean
    get() = when (this) {
        is Playing -> true
        is Starting, Canceled -> false
    }

internal class RadioSource(
    val name: String,
    val url: String,
) {
    companion object {
        val KNOWN: List<RadioSource> = listOf(
            RadioSource("RMF FM", "https://rs103-krk.rmfstream.pl/RMFFM48"),
            RadioSource("RMF MAXX", "https://rs201-krk-cyfronet.rmfstream.pl/RMFMAXXX48"),
            RadioSource("RMF ROCK", "https://rs201-krk-cyfronet.rmfstream.pl/ROCK"),
        )
    }
}

internal class MyViewModel : ViewModel() {
    private val client = HttpClient()

    private val _source = MutableStateFlow(RadioSource.KNOWN.first())
    val source: StateFlow<RadioSource> = _source.asStateFlow()

    private var _playingState = MutableStateFlow<PlayingState>(Canceled)
    val playingState: StateFlow<PlayingState> = _playingState.asStateFlow()

    fun onSourceSelected(source: RadioSource) {
        if (_source.value == source) return

        val isPlaying = _playingState.value.isPlaying
        if (isPlaying) onPlayStop()

        _source.update { source }
        if (isPlaying) onPlayStop()
    }

    fun onPlayStop() {
        _playingState.update { state ->
            when (state) {
                Canceled -> Starting(
                    job = viewModelScope.launch(Dispatchers.Default) {
                        val statement = client.prepareGet(_source.value.url)
                        val result = statement.body<ByteReadChannel>()

                        try {
                            play(
                                bytes = result,
                                onStartPlaying = {
                                    _playingState.update { it.onStartPlaying() }
                                },
                            )
                        } finally {
                            result.cancel()
                        }
                    },
                )

                is Playing -> state.cancel().let { Canceled }
                is Starting -> state.cancel().let { Canceled }
            }
        }

    }
}
