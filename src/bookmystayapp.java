import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

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

    public void validateAndBook(String roomType) throws InvalidBookingException {
        if (!roomAvailability.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
        int available = roomAvailability.get(roomType);
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }
        roomAvailability.put(roomType, available - 1);
    }

    public Map<String, Integer> getAvailability() {
        return roomAvailability;
    }
}

public class bookmystayapp {
    public static void main(String[] args) {
        Inventory inventory = new Inventory();
        List<Reservation> confirmedReservations = new ArrayList<>();

        try {
            inventory.validateAndBook("Deluxe");
            Reservation r1 = new Reservation("RES201", "Alice", "Deluxe", 3000.0);
            confirmedReservations.add(r1);
            System.out.println("Booking confirmed: " + r1);
        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }

        try {
            inventory.validateAndBook("Penthouse");
            Reservation r2 = new Reservation("RES202", "Bob", "Penthouse", 8000.0);
            confirmedReservations.add(r2);
            System.out.println("Booking confirmed: " + r2);
        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }

        try {
            inventory.validateAndBook("Deluxe");
            Reservation r3 = new Reservation("RES203", "Charlie", "Deluxe", 3000.0);
            confirmedReservations.add(r3);
            System.out.println("Booking confirmed: " + r3);
        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }

        System.out.println("=== Final Confirmed Reservations ===");
        for (Reservation r : confirmedReservations) {
            System.out.println(r);
        }

        System.out.println("=== Remaining Inventory ===");
        System.out.println(inventory.getAvailability());
    }
}