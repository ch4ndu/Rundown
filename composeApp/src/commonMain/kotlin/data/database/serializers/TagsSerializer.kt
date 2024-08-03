package data.database.serializers

import data.database.model.transaction.Tag
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class TagsSerializer : KSerializer<Tag> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Tag", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Tag {
        val tag = decoder.decodeString()
        return Tag(tag)
    }

    override fun serialize(
        encoder: Encoder,
        value: Tag
    ) {
        encoder.encodeString(value.tag)
    }
}