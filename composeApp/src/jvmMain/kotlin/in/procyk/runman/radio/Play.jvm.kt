package `in`.procyk.runman.radio

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import net.sourceforge.jaad.SampleBuffer
import net.sourceforge.jaad.aac.Decoder
import net.sourceforge.jaad.adts.ADTSDemultiplexer
import javax.sound.sampled.AudioSystem

internal actual suspend fun play(
    sourceUrl: String,
    onStartPlaying: () -> Unit,
    onStopPlaying: () -> Unit,
): Unit = withContext(Dispatchers.Default) {
    try {
        val client = HttpClient()

        val statement = client.prepareGet(sourceUrl)
        val bytes = statement.body<ByteReadChannel>()

        val adts = ADTSDemultiplexer(bytes.toInputStream())
        val decoder = Decoder.create(adts.decoderInfo)
        val format = decoder.audioFormat
        val buffer = SampleBuffer(format)

        AudioSystem.getSourceDataLine(format).use { line ->
            line.open()
            line.start()
            onStartPlaying()

            while (currentCoroutineContext().isActive) {
                val readFrame = adts.readNextFrame()
                decoder.decodeFrame(readFrame, buffer)
                val decodedFrame = buffer.data
                line.write(decodedFrame, 0, decodedFrame.size)
            }
        }
    } catch (_: Exception) {
        onStopPlaying()
    }
}