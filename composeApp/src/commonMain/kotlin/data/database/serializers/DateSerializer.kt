package data.database.serializers

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

//@Serializer(forClass = LocalDateTime::class)
class DateSerializer : KSerializer<LocalDateTime> {
    private val formatter = LocalDateTime.Formats.ISO

    override fun serialize(
        encoder: Encoder,
        value: LocalDateTime
    ) {
        encoder.encodeString(value.format(formatter))
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("DateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return fromApiFormatter.parse(decoder.decodeString()).toLocalDateTime()
    }

    companion object {
        val isoFormat = LocalDateTime.Formats.ISO

        //yyyy-MM-dd'T'HH:mm:ssZ
        val fromApiFormatter = DateTimeComponents.Formats.ISO_DATE_TIME_OFFSET

        val displayFormat = LocalDateTime.Format {
            //h:mma MM-dd-yyyy
            amPmHour();
            char(':');
            minute();
            amPmMarker(
                "AM",
                "PM"
            );
            char(' ');
            monthName(MonthNames.ENGLISH_ABBREVIATED);
            char(' ');
            dayOfMonth();
            chars(", ");
            year()
        }
        val transactionDisplayFormat = LocalDateTime.Format {
            //MM-dd-yyyy
            monthNumber();char('-');dayOfMonth();char('-');year()
        }
        val balanceDisplayFormat = LocalDateTime.Format {
            //dd MMM yy
            dayOfMonth();char(' ');monthNumber();char(' ');year()
        }

        val sendToApiFormat = LocalDateTime.Format {
            //yyyy-MM-dd
            year();char('-'); monthNumber();char('-');dayOfMonth()
        }

        val chartMonthFormat = LocalDateTime.Format {
            //MMM
            monthName(MonthNames.ENGLISH_ABBREVIATED);
        }

        val chartMonthYearFormat = LocalDateTime.Format {
            //MMM yyyy
            monthName(MonthNames.ENGLISH_ABBREVIATED); char(' '); year()
        }
        val chartDayMonthFormat = LocalDateTime.Format {
            //dd MMM
            dayOfMonth(); char(' '); monthName(MonthNames.ENGLISH_ABBREVIATED)
        }
        val chartDayMonthYearFormat = LocalDateTime.Format {
            //dd MMM yyyy
            dayOfMonth(); char(' '); monthName(MonthNames.ENGLISH_ABBREVIATED); char(' '); year()
        }
    }
}