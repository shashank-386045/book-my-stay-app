import java.util.*;

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private double cost;

    public Reservation(String reservationId, String guestName, String roomType, double cost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.cost = cost;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "ReservationID: " + reservationId + ", Guest: " + guestName + ", Room: " + roomType + ", Cost: ₹" + cost;
    }
}

class BookingHistory {
    private List<Reservation> confirmedBookings = new ArrayList<>();

    public void addBooking(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    public List<Reservation> getAllBookings() {
        return confirmedBookings;
    }
}

class BookingReportService {
    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    public void generateReport() {
        List<Reservation> bookings = history.getAllBookings();
        System.out.println("=== Booking Report ===");
        for (Reservation r : bookings) {
            System.out.println(r);
        }
        double totalRevenue = bookings.stream().mapToDouble(Reservation::getCost).sum();
        System.out.println("Total Bookings: " + bookings.size());
        System.out.println("Total Revenue: ₹" + totalRevenue);
    }
}

public class bookmystayapp{
    public static void main(String[] args) {
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService(history);

        Reservation r1 = new Reservation("RES101", "Alice", "Deluxe", 3000.0);
        Reservation r2 = new Reservation("RES102", "Bob", "Suite", 5000.0);
        Reservation r3 = new Reservation("RES103", "Charlie", "Standard", 2000.0);

        history.addBooking(r1);
        history.addBooking(r2);
        history.addBooking(r3);

        reportService.generateReport();
    }
}