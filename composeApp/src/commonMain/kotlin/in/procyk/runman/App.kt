package `in`.procyk.runman

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import compose.icons.FeatherIcons
import compose.icons.feathericons.Play
import compose.icons.feathericons.Square
import `in`.procyk.runman.PlayingState.*
import `in`.procyk.runman.theme.AppTheme
import `in`.procyk.runman.vm.RadioViewModel
import kotlinx.coroutines.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun App() = AppTheme {
    val vm = remember { RadioViewModel() }

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        val radioCoverUrl by vm.radioCoverUrl.collectAsState(null)

        AsyncImage(
            model = radioCoverUrl.also { println("radioCoverUrl = $radioCoverUrl") },
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    renderEffect = BlurEffect(radiusX = 20f, radiusY = 20f, edgeTreatment = TileMode.Mirror)
                },
            contentDescription = null,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Text(
                text = "Runman",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White,
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(2f, 2f),
                        blurRadius = 2f,
                    ),
                ),
                fontWeight = FontWeight.Bold,
            )

            Spacer(Modifier.height(32.dp))

            AsyncImage(
                model = radioCoverUrl,
                contentScale = ContentScale.Crop,
                modifier = Modifier.sizeIn(maxHeight = 256.dp, maxWidth = 256.dp),
                contentDescription = null,
            )

            Spacer(Modifier.height(16.dp))

            val playingState by vm.playingState.collectAsState()
            var expanded by remember { mutableStateOf(false) }
            val radioName by vm.radioName.collectAsState(null)

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
                modifier = Modifier.widthIn(max = 600.dp),
            ) {
                TextField(
                    value = radioName ?: "Loading radio name…",
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
                    vm.knownRadioSources.forEach { item ->
                        val name by item.getName().collectAsState("Loading radio name…")
                        DropdownMenuItem(
                            text = { Text(name) },
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
}

internal sealed class PlayingState {
    abstract fun onStartPlaying(): PlayingState
    abstract fun onStopPlaying()

    class Starting(private val job: Job) : PlayingState() {
        override fun onStartPlaying() = Playing(job)
        override fun onStopPlaying() = job.cancel()
    }

    class Playing(private val job: Job) : PlayingState() {
        override fun onStartPlaying() = Stopped
        override fun onStopPlaying() = job.cancel()
    }

    data object Stopped : PlayingState() {
        override fun onStartPlaying() = Stopped
        override fun onStopPlaying() {}
    }
}

internal val PlayingState.isPlaying: Boolean
    get() = when (this) {
        is Playing -> true
        is Starting, Stopped -> false
    }
