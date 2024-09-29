package `in`.procyk.runman

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import compose.icons.FeatherIcons
import compose.icons.feathericons.Play
import compose.icons.feathericons.Square
import `in`.procyk.runman.PlayingState.*
import `in`.procyk.runman.radio.play
import `in`.procyk.runman.theme.AppTheme
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
        Text("Runman", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)

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

            is Playing, is Stopped -> ElevatedButton(
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

                        is Starting, Stopped -> {
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
    fun onStopPlaying()
    class Starting(private val job: Job) : PlayingState {
        override fun onStartPlaying() = Playing(job)
        override fun onStopPlaying() = job.cancel()
    }

    class Playing(private val job: Job) : PlayingState {
        override fun onStartPlaying() = Stopped
        override fun onStopPlaying() = job.cancel()
    }

    data object Stopped : PlayingState {
        override fun onStartPlaying() = Stopped
        override fun onStopPlaying() {}
    }
}

internal val PlayingState.isPlaying: Boolean
    get() = when (this) {
        is Playing -> true
        is Starting, Stopped -> false
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
    private val _source = MutableStateFlow(RadioSource.KNOWN.first())
    val source: StateFlow<RadioSource> = _source.asStateFlow()

    private var _playingState = MutableStateFlow<PlayingState>(Stopped)
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
                Stopped -> Starting(
                    job = viewModelScope.launch {
                        play(
                            sourceUrl = _source.value.url,
                            onStartPlaying = { _playingState.update { it.onStartPlaying() } },
                            onStopPlaying = { _playingState.update { onStopPlaying() } },
                        )
                    },
                )

                is Playing, is Starting -> onStopPlaying()
            }
        }
    }

    private fun onStopPlaying(): Stopped {
        _playingState.value.onStopPlaying()
        return Stopped
    }
}
