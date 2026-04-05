package android.natanel.flightliveupdate.model

enum class SeatType { WINDOW, MIDDLE, AISLE }

enum class FlightStatus { ON_TIME, DELAYED, CANCELLED }

data class FlightFormState(
    val flightNumber: String = "LY315",
    val origin: String = "Tel Aviv",
    val originCode: String = "TLV",
    val destination: String = "New York",
    val destinationCode: String = "JFK",
    val departureDate: String = "",
    val returnDate: String = "",
    val departureTime: String = "14:30",
    val gate: String = "B12",
    val seat: String = "12A",
    val seatType: SeatType = SeatType.WINDOW,
    val status: FlightStatus = FlightStatus.ON_TIME,
    val minutesUntilDeparture: String = "45"
)
