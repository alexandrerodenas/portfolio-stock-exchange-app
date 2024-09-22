import android.os.Build
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.formatter.ValueFormatter

private const val DATE_PATTERN = "dd/MM"

class DateValueFormatter(private val timestamps: List<Long>) : ValueFormatter() {

    override fun getFormattedValue(value: Float): String =
        value.toLong()
            .let { timestamp ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    formatTimestamp(timestamp)
                else
                    formatTimestampLegacy(timestamp)
            }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatTimestamp(timestamp: Long): String =
        java.time.Instant.ofEpochSecond(timestamp)
            .let { instant ->
                java.time.format.DateTimeFormatter
                    .ofPattern(DATE_PATTERN)
                    .withZone(java.time.ZoneId.systemDefault())
                    .format(instant)
            }

    // Legacy formatter for older devices
    private fun formatTimestampLegacy(timestamp: Long): String =
        java.text.SimpleDateFormat(DATE_PATTERN, java.util.Locale.getDefault())
            .format(java.util.Date(timestamp * 1000)) // Convert to milliseconds
}
