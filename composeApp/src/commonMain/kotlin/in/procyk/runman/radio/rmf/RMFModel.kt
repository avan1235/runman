package `in`.procyk.runman.radio.rmf

import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object InstantSecondsSerializer : KSerializer<Instant> {

    private val serializer = Long.serializer()

    override val descriptor
        get() = serializer.descriptor

    override fun deserialize(decoder: Decoder) =
        Instant.fromEpochSeconds(serializer.deserialize(decoder))

    override fun serialize(encoder: Encoder, value: Instant) =
        serializer.serialize(encoder, value.epochSeconds)
}

@Serializable
internal data class RMFPlaylistItem(
    val recordTitle: String? = null,
    val year: Int? = null,
    val author: String? = null,
    val trackId: Int? = null,
    val start: String? = null,
    val source: String? = null,
    val title: String? = null,
    val coverBigUrl: String? = null,
    val points: Int? = null,
    val uptime: Int? = null,
    val lenght: String? = null,
    val cover: Int? = null,
    val coverUrl: String? = null,
    val authorUrl: String? = null,
    val selector: String? = null,
    val votes: Int? = null,
    val order: Int? = null,
    @Serializable(with = InstantSecondsSerializer::class)
    val timestamp: Instant? = null,
)

internal object RMFPlaylistSerializer : KSerializer<RMFPlaylist> {
    private val serializer = ListSerializer(RMFPlaylistItem.serializer())

    override val descriptor
        get() = serializer.descriptor

    override fun deserialize(decoder: Decoder) =
        RMFPlaylist(serializer.deserialize(decoder))

    override fun serialize(encoder: Encoder, value: RMFPlaylist) =
        serializer.serialize(encoder, value.items)
}

@Serializable(with = RMFPlaylistSerializer::class)
internal data class RMFPlaylist(val items: List<RMFPlaylistItem> = emptyList())
