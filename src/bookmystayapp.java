import java.util.*;
import java.util.concurrent.*;

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
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

    @Override
    public String toString() {
        return "ReservationID: " + reservationId + ", Guest: " + guestName + ", Room: " + roomType;
    }
}

class Inventory {
    private Map<String, Integer> roomAvailability = new HashMap<>();

    public Inventory() {
        roomAvailability.put("Standard", 2);
        roomAvailability.put("Deluxe", 2);
        roomAvailability.put("Suite", 1);
    }

    public synchronized boolean allocateRoom(String roomType) {
        int available = roomAvailability.getOrDefault(roomType, 0);
        if (available > 0) {
            roomAvailability.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public Map<String, Integer> getAvailability() {
        return roomAvailability;
    }
}

class ConcurrentBookingProcessor implements Runnable {
    private BlockingQueue<Reservation> bookingQueue;
    private Inventory inventory;
    private List<Reservation> confirmedReservations;

    public ConcurrentBookingProcessor(BlockingQueue<Reservation> bookingQueue, Inventory inventory, List<Reservation> confirmedReservations) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.confirmedReservations = confirmedReservations;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Reservation reservation = bookingQueue.poll(1, TimeUnit.SECONDS);
                if (reservation == null) break;
                synchronized (inventory) {
                    if (inventory.allocateRoom(reservation.getRoomType())) {
                        synchronized (confirmedReservations) {
                            confirmedReservations.add(reservation);
                            System.out.println("Booking confirmed: " + reservation);
                        }
                    } else {
                        System.out.println("Booking failed (no availability): " + reservation);
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class bookmystayapp{
    public static void main(String[] args) throws InterruptedException {
        Inventory inventory = new Inventory();
        BlockingQueue<Reservation> bookingQueue = new LinkedBlockingQueue<>();
        List<Reservation> confirmedReservations = Collections.synchronizedList(new ArrayList<>());

        bookingQueue.add(new Reservation("RES401", "Alice", "Deluxe"));
        bookingQueue.add(new Reservation("RES402", "Bob", "Suite"));
        bookingQueue.add(new Reservation("RES403", "Charlie", "Deluxe"));
        bookingQueue.add(new Reservation("RES404", "David", "Standard"));
        bookingQueue.add(new Reservation("RES405", "Eve", "Standard"));
        bookingQueue.add(new Reservation("RES406", "Frank", "Suite"));

        Thread t1 = new Thread(new ConcurrentBookingProcessor(bookingQueue, inventory, confirmedReservations));
        Thread t2 = new Thread(new ConcurrentBookingProcessor(bookingQueue, inventory, confirmedReservations));

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("=== Final Confirmed Reservations ===");
        for (Reservation r : confirmedReservations) {
            System.out.println(r);
        }

        System.out.println("=== Remaining Inventory ===");
        System.out.println(inventory.getAvailability());
    }
}