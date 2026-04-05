package android.natanel.flightliveupdate

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import android.natanel.flightliveupdate.notification.FlightNotificationHelper
import android.natanel.flightliveupdate.ui.screen.FlightFormScreen
import android.natanel.flightliveupdate.ui.theme.FlightLiveUpdateTheme
import android.natanel.flightliveupdate.viewmodel.FlightViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FlightNotificationHelper.createChannel(this)
        enableEdgeToEdge()
        setContent {
            FlightLiveUpdateTheme {
                val flightViewModel: FlightViewModel = viewModel()
                RequestNotificationPermission()
                FlightFormScreen(viewModel = flightViewModel)
            }
        }
    }
}

@Composable
private fun RequestNotificationPermission() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { /* permission result handled silently */ }

    LaunchedEffect(Unit) {
        val alreadyGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!alreadyGranted) {
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

@Preview(showBackground = true, locale = "iw")
@Composable
fun RequestNotificationPermissionPreview() {
    FlightLiveUpdateTheme {
        // No visual output — permission request composable
    }
}
