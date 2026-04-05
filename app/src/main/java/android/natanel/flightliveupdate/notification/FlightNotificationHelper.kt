package android.natanel.flightliveupdate.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.natanel.flightliveupdate.MainActivity
import android.natanel.flightliveupdate.R
import android.natanel.flightliveupdate.model.FlightFormState
import android.natanel.flightliveupdate.model.FlightStatus
import android.natanel.flightliveupdate.model.SeatType
import android.os.Build
import androidx.core.app.NotificationCompat

object FlightNotificationHelper {

    private const val CHANNEL_ID = "flight_updates"
    private const val CHANNEL_NAME = "עדכוני טיסה"
    private const val NOTIFICATION_ID = 1001

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "עדכונים חיים לטיסתך"
            }
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context, state: FlightFormState) {
        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val collapsedText = "${state.origin} ← ${state.destination} • שער ${state.gate} • יוצא בעוד ${state.minutesUntilDeparture} דק'"

        val expandedText = buildString {
            appendLine("טיסה ${state.flightNumber} מ${state.origin} (${state.originCode}) ל${state.destination} (${state.destinationCode}) מתחילה עלייה למטוס.")
            appendLine()
            appendLine("🛫  יציאה:   ${state.departureTime}")
            appendLine("🚪  שער:     ${state.gate}")
            appendLine("💺  מושב:    ${state.seat} (${state.seatType.displayName()})")
            append("📋  סטטוס:   ${state.status.displayName()}")
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("✈️ טיסה ${state.flightNumber} — עלייה למטוס בקרוב")
            .setContentText(collapsedText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(expandedText))
            .setContentIntent(pendingIntent)
            .addAction(0, "לחץ לפרטים נוספים", pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, notification)
    }

    private fun SeatType.displayName() = when (this) {
        SeatType.WINDOW -> "חלון"
        SeatType.MIDDLE -> "אמצע"
        SeatType.AISLE -> "מעבר"
    }

    private fun FlightStatus.displayName() = when (this) {
        FlightStatus.ON_TIME -> "בזמן"
        FlightStatus.DELAYED -> "מאוחר"
        FlightStatus.CANCELLED -> "מבוטל"
    }
}
