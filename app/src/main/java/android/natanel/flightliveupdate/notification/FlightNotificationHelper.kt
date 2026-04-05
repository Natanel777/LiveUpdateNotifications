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
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat

/*
 * ============================================================
 *  TWO WAYS TO BUILD AN EXPANDED NOTIFICATION
 * ============================================================
 *
 *  APPROACH 1 — NotificationCompat styles (no XML layout)
 *  -------------------------------------------------------
 *  Use a built-in style like BigTextStyle or InboxStyle directly
 *  inside NotificationCompat.Builder. Simple and fast, but:
 *  - BigTextStyle: plain text only, bold via SpannableString is
 *    stripped by most OEM notification shades (Samsung, MIUI…).
 *  - InboxStyle: each addLine() is one line — truncates with "…"
 *    if the text is too long, no wrapping possible.
 *
 *  Example:
 *      .setStyle(
 *          NotificationCompat.BigTextStyle().bigText("your text")
 *      )
 *      // or
 *      .setStyle(
 *          NotificationCompat.InboxStyle()
 *              .addLine("line 1")
 *              .addLine("line 2")
 *      )
 *
 *  APPROACH 2 — RemoteViews with a custom XML layout (current)
 *  ------------------------------------------------------------
 *  Define the expanded view in res/layout/notification_flight_expanded.xml.
 *  Full control over every TextView: bold via android:textStyle="bold",
 *  multi-line wrapping, dividers, spacing — works on ALL devices.
 *  Requires pairing with DecoratedCustomViewStyle() so the system
 *  still draws the standard header (app icon, name, timestamp).
 *
 *  Example:
 *      val expandedView = RemoteViews(packageName, R.layout.notification_flight_expanded)
 *      expandedView.setTextViewText(R.id.tv_departure, "14:30")
 *      builder
 *          .setCustomBigContentView(expandedView)
 *          .setStyle(NotificationCompat.DecoratedCustomViewStyle())
 * ============================================================
 */
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

        val collapsedText = "${state.destination} • שער ${state.gate} • יוצא בעוד ${state.minutesUntilDeparture} דק "

        // Custom expanded view using RemoteViews for full text wrapping + bold labels
        val expandedView = RemoteViews(context.packageName, R.layout.notification_flight_expanded).apply {
            setTextViewText(
                R.id.tv_announcement,
                "טיסה ${state.flightNumber}\nמ - ${state.origin} (${state.originCode})\nל - ${state.destination} (${state.destinationCode})\nמתחילה עלייה למטוס. "
            )
            setTextViewText(R.id.tv_departure, state.departureTime)
            setTextViewText(R.id.tv_gate, state.gate)
            setTextViewText(R.id.tv_seat, "${state.seat} (${state.seatType.displayName()})")
            setTextViewText(R.id.tv_status, state.status.displayName())
            setOnClickPendingIntent(R.id.notification_root, pendingIntent)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("✈️ טיסה ${state.flightNumber} — עלייה למטוס בקרוב")
            .setContentText(collapsedText)
            .setCustomBigContentView(expandedView)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
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
