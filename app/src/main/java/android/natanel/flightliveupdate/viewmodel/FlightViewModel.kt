package android.natanel.flightliveupdate.viewmodel

import androidx.lifecycle.ViewModel
import android.natanel.flightliveupdate.model.FlightFormState
import android.natanel.flightliveupdate.model.FlightStatus
import android.natanel.flightliveupdate.model.SeatType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FlightViewModel : ViewModel() {

    private val _state = MutableStateFlow(FlightFormState())
    val state: StateFlow<FlightFormState> = _state.asStateFlow()

    fun onFlightNumberChange(value: String) = _state.update { it.copy(flightNumber = value) }
    fun onOriginChange(value: String) = _state.update { it.copy(origin = value) }
    fun onOriginCodeChange(value: String) = _state.update { it.copy(originCode = value) }
    fun onDestinationChange(value: String) = _state.update { it.copy(destination = value) }
    fun onDestinationCodeChange(value: String) = _state.update { it.copy(destinationCode = value) }
    fun onDepartureDateChange(value: String) = _state.update { it.copy(departureDate = value) }
    fun onReturnDateChange(value: String) = _state.update { it.copy(returnDate = value) }
    fun onDepartureTimeChange(value: String) = _state.update { it.copy(departureTime = value) }
    fun onGateChange(value: String) = _state.update { it.copy(gate = value) }
    fun onSeatChange(value: String) = _state.update { it.copy(seat = value) }
    fun onSeatTypeChange(value: SeatType) = _state.update { it.copy(seatType = value) }
    fun onStatusChange(value: FlightStatus) = _state.update { it.copy(status = value) }
    fun onMinutesUntilDepartureChange(value: String) = _state.update { it.copy(minutesUntilDeparture = value) }
}
