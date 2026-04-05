package android.natanel.flightliveupdate.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.natanel.flightliveupdate.model.FlightFormState
import android.natanel.flightliveupdate.model.FlightStatus
import android.natanel.flightliveupdate.model.SeatType
import android.natanel.flightliveupdate.notification.FlightNotificationHelper
import android.natanel.flightliveupdate.ui.theme.FlightLiveUpdateTheme
import android.natanel.flightliveupdate.viewmodel.FlightViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightFormScreen(viewModel: FlightViewModel) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("פרטי טיסה") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item { SectionHeader(title = "מסלול") }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = state.origin,
                        onValueChange = viewModel::onOriginChange,
                        label = { Text("עיר מוצא") },
                        modifier = Modifier.weight(2f)
                    )
                    OutlinedTextField(
                        value = state.originCode,
                        onValueChange = viewModel::onOriginCodeChange,
                        label = { Text("קוד") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = state.destination,
                        onValueChange = viewModel::onDestinationChange,
                        label = { Text("עיר יעד") },
                        modifier = Modifier.weight(2f)
                    )
                    OutlinedTextField(
                        value = state.destinationCode,
                        onValueChange = viewModel::onDestinationCodeChange,
                        label = { Text("קוד") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item { SectionHeader(title = "לוח זמנים") }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = state.departureDate,
                        onValueChange = viewModel::onDepartureDateChange,
                        label = { Text("תאריך יציאה") },
                        placeholder = { Text("YYYY-MM-DD") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = state.returnDate,
                        onValueChange = viewModel::onReturnDateChange,
                        label = { Text("תאריך חזרה") },
                        placeholder = { Text("YYYY-MM-DD") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = state.departureTime,
                        onValueChange = viewModel::onDepartureTimeChange,
                        label = { Text("שעת יציאה") },
                        placeholder = { Text("HH:MM") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = state.minutesUntilDeparture,
                        onValueChange = viewModel::onMinutesUntilDepartureChange,
                        label = { Text("יוצא בעוד (דק')") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item { SectionHeader(title = "פרטי הטיסה") }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = state.flightNumber,
                        onValueChange = viewModel::onFlightNumberChange,
                        label = { Text("מספר טיסה") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = state.gate,
                        onValueChange = viewModel::onGateChange,
                        label = { Text("שער") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = state.seat,
                        onValueChange = viewModel::onSeatChange,
                        label = { Text("מושב") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                SeatTypeSelector(
                    selected = state.seatType,
                    onSelect = viewModel::onSeatTypeChange
                )
            }

            item {
                FlightStatusSelector(
                    selected = state.status,
                    onSelect = viewModel::onStatusChange
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                Button(
                    onClick = { FlightNotificationHelper.showNotification(context, state) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("הצג התראה")
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeatTypeSelector(
    selected: SeatType,
    onSelect: (SeatType) -> Unit
) {
    val options = SeatType.entries
    Column {
        Text(
            text = "סוג מושב",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, seatType ->
                SegmentedButton(
                    selected = selected == seatType,
                    onClick = { onSelect(seatType) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    label = {
                        Text(
                            text = when (seatType) {
                                SeatType.WINDOW -> "חלון"
                                SeatType.MIDDLE -> "אמצע"
                                SeatType.AISLE -> "מעבר"
                            }
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FlightStatusSelector(
    selected: FlightStatus,
    onSelect: (FlightStatus) -> Unit
) {
    val options = FlightStatus.entries
    Column {
        Text(
            text = "סטטוס טיסה",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, status ->
                SegmentedButton(
                    selected = selected == status,
                    onClick = { onSelect(status) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    label = {
                        Text(
                            text = when (status) {
                                FlightStatus.ON_TIME -> "בזמן"
                                FlightStatus.DELAYED -> "מאוחר"
                                FlightStatus.CANCELLED -> "מבוטל"
                            }
                        )
                    }
                )
            }
        }
    }
}

// region Previews

@Preview(showBackground = true, locale = "iw")
@Composable
fun FlightFormScreenPreview() {
    FlightLiveUpdateTheme {
        FlightFormScreen(viewModel = FlightViewModel())
    }
}

@Preview(showBackground = true, locale = "iw")
@Composable
fun SectionHeaderPreview() {
    FlightLiveUpdateTheme {
        SectionHeader(title = "מסלול")
    }
}

@Preview(showBackground = true, locale = "iw")
@Composable
fun SeatTypeSelectorPreview() {
    FlightLiveUpdateTheme {
        SeatTypeSelector(
            selected = SeatType.WINDOW,
            onSelect = {}
        )
    }
}

@Preview(showBackground = true, locale = "iw")
@Composable
fun FlightStatusSelectorPreview() {
    FlightLiveUpdateTheme {
        FlightStatusSelector(
            selected = FlightStatus.ON_TIME,
            onSelect = {}
        )
    }
}

// endregion
