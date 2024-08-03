package data.database.serializers

import data.database.model.charts.AccountChartEntries
import data.database.model.charts.ChartData
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import org.lighthousegames.logging.logging
import roundTo2Digits

class AccountChartEntriesSerializer : KSerializer<AccountChartEntries> {
    private val log = logging()

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("AccountChartEntries", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): AccountChartEntries {
        val jsonDecoder = decoder as? JsonDecoder
        val json = jsonDecoder?.decodeJsonElement() as? JsonObject ?: JsonObject(emptyMap())
        val iterator = json.keys.iterator()
        val chartEntryList = mutableListOf<ChartData>()
        while (iterator.hasNext()) {
            val key = iterator.next()
            try {
                val value = (json[key] as? JsonPrimitive)?.contentOrNull?.toString() ?: continue
                chartEntryList.add(
                    ChartData(
                        DateSerializer.fromApiFormatter.parse(key).toLocalDateTime(),
                        value.toDouble().roundTo2Digits()
                    )
                )
//            } catch (exception: JSONException) {
            } catch (exception: Exception) {
                log.e { "exception:$exception" }
                return AccountChartEntries(emptyList())
            }
        }
        return AccountChartEntries(chartEntryList)
    }

    override fun serialize(
        encoder: Encoder,
        value: AccountChartEntries
    ) {
//        val chatDataList = value.entries
//
//        Json.encodeToString(value.entries.map {
//            it.date.format(LocalDateTime.Formats.ISO) to it.value
//        })
////            JsonObject()
//        val json = JsonObject()
//        for (chartData in chatDataList) {
//            json.put(
//                chartData.date.format(LocalDateTime.Formats.ISO),
//                chartData.value
//            )
//        }
        encoder.encodeString(
            Json.encodeToString(
                value.entries.map {
                    it.date.format(LocalDateTime.Formats.ISO) to it.value
                }
            )
        )
    }
}