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

class Inventory {
    private Map<String, Integer> roomAvailability = new HashMap<>();

    public Inventory() {
        roomAvailability.put("Standard", 2);
        roomAvailability.put("Deluxe", 1);
        roomAvailability.put("Suite", 1);
    }

    public void bookRoom(String roomType) {
        int available = roomAvailability.getOrDefault(roomType, 0);
        if (available <= 0) throw new IllegalStateException("No rooms available for type: " + roomType);
        roomAvailability.put(roomType, available - 1);
    }

    public void restoreRoom(String roomType) {
        int available = roomAvailability.getOrDefault(roomType, 0);
        roomAvailability.put(roomType, available + 1);
    }

    public Map<String, Integer> getAvailability() {
        return roomAvailability;
    }
}

class BookingHistory {
    private List<Reservation> confirmedBookings = new ArrayList<>();

    public void addBooking(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    public void removeBooking(String reservationId) {
        confirmedBookings.removeIf(r -> r.getReservationId().equals(reservationId));
    }

    public List<Reservation> getAllBookings() {
        return confirmedBookings;
    }
}

class CancellationService {
    private BookingHistory history;
    private Inventory inventory;
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(BookingHistory history, Inventory inventory) {
        this.history = history;
        this.inventory = inventory;
    }

    public void cancelReservation(String reservationId) {
        Optional<Reservation> reservationOpt = history.getAllBookings().stream()
                .filter(r -> r.getReservationId().equals(reservationId))
                .findFirst();
        if (!reservationOpt.isPresent()) {
            System.out.println("Cancellation failed: Reservation not found.");
            return;
        }
        Reservation reservation = reservationOpt.get();
        rollbackStack.push(reservation.getRoomType());
        inventory.restoreRoom(reservation.getRoomType());
        history.removeBooking(reservationId);
        System.out.println("Cancellation successful for " + reservationId);
    }

    public Stack<String> getRollbackStack() {
        return rollbackStack;
    }
}

public class bookmystayapp {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancellationService = new CancellationService(history, inventory);

        Reservation r1 = new Reservation("RES301", "Alice", "Deluxe", 3000.0);
        Reservation r2 = new Reservation("RES302", "Bob", "Suite", 5000.0);

        inventory.bookRoom(r1.getRoomType());
        history.addBooking(r1);

        inventory.bookRoom(r2.getRoomType());
        history.addBooking(r2);

        System.out.println("=== Confirmed Reservations ===");
        for (Reservation r : history.getAllBookings()) {
            System.out.println(r);
        }

        cancellationService.cancelReservation("RES301");

        System.out.println("=== Reservations After Cancellation ===");
        for (Reservation r : history.getAllBookings()) {
            System.out.println(r);
        }

        System.out.println("=== Remaining Inventory ===");
        System.out.println(inventory.getAvailability());

        System.out.println("=== Rollback Stack ===");
        System.out.println(cancellationService.getRollbackStack());
    }
}