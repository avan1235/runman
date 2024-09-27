package `in`.procyk.runman.radio

import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.isActive
import net.sourceforge.jaad.SampleBuffer
import net.sourceforge.jaad.aac.Decoder
import net.sourceforge.jaad.adts.ADTSDemultiplexer
import javax.sound.sampled.AudioSystem
import kotlin.coroutines.coroutineContext

internal actual suspend fun play(bytes: ByteReadChannel, onStartPlaying: () -> Unit) {
    val adts = ADTSDemultiplexer(bytes.toInputStream())
    val decoder = Decoder.create(adts.decoderInfo)
    val format = decoder.audioFormat
    val buffer = SampleBuffer(format)

    try {
        AudioSystem.getSourceDataLine(format).use { line ->
            line.open()
            line.start()
            onStartPlaying()

            while (coroutineContext.isActive) {
                val readFrame = adts.readNextFrame()
                decoder.decodeFrame(readFrame, buffer)
                val decodedFrame = buffer.data
                line.write(decodedFrame, 0, decodedFrame.size)
            }
        }
    } catch (ignored: Exception) {

    }
}