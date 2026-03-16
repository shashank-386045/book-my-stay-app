import java.io.*;
import java.util.*;

class Reservation implements Serializable {
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

class Inventory implements Serializable {
    private Map<String, Integer> roomAvailability = new HashMap<>();

    public Inventory() {
        roomAvailability.put("Standard", 2);
        roomAvailability.put("Deluxe", 1);
        roomAvailability.put("Suite", 1);
    }

    public boolean allocateRoom(String roomType) {
        int available = roomAvailability.getOrDefault(roomType, 0);
        if (available > 0) {
            roomAvailability.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void restoreRoom(String roomType) {
        int available = roomAvailability.getOrDefault(roomType, 0);
        roomAvailability.put(roomType, available + 1);
    }

    public Map<String, Integer> getAvailability() {
        return roomAvailability;
    }
}

class BookingHistory implements Serializable {
    private List<Reservation> confirmedBookings = new ArrayList<>();

    public void addBooking(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    public List<Reservation> getAllBookings() {
        return confirmedBookings;
    }
}

class PersistenceService {
    public void saveState(BookingHistory history, Inventory inventory, String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(history);
            out.writeObject(inventory);
            System.out.println("System state saved.");
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    public Object[] loadState(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            BookingHistory history = (BookingHistory) in.readObject();
            Inventory inventory = (Inventory) in.readObject();
            System.out.println("System state restored.");
            return new Object[]{history, inventory};
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state, starting fresh.");
            return new Object[]{new BookingHistory(), new Inventory()};
        }
    }
}

public class bookmystayapp{
    public static void main(String[] args) {
        String filename = "system_state.dat";
        PersistenceService persistenceService = new PersistenceService();
        Object[] state = persistenceService.loadState(filename);
        BookingHistory history = (BookingHistory) state[0];
        Inventory inventory = (Inventory) state[1];

        Reservation r1 = new Reservation("RES501", "Alice", "Deluxe", 3000.0);
        if (inventory.allocateRoom(r1.getRoomType())) {
            history.addBooking(r1);
            System.out.println("Booking confirmed: " + r1);
        }

        Reservation r2 = new Reservation("RES502", "Bob", "Suite", 5000.0);
        if (inventory.allocateRoom(r2.getRoomType())) {
            history.addBooking(r2);
            System.out.println("Booking confirmed: " + r2);
        }

        System.out.println("=== Current Reservations ===");
        for (Reservation r : history.getAllBookings()) {
            System.out.println(r);
        }

        System.out.println("=== Current Inventory ===");
        System.out.println(inventory.getAvailability());

        persistenceService.saveState(history, inventory, filename);
    }
}