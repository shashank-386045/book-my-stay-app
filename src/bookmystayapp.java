import java.util.HashMap;

public class bookmystayapp{

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 3);
        inventory.addRoomType("Suite Room", 0);

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        Room[] rooms = { single, doubleRoom, suite };

        RoomSearchService searchService = new RoomSearchService();

        System.out.println("Hotel Booking System v4.0");
        System.out.println("Available Rooms:");
        System.out.println();

        searchService.searchAvailableRooms(inventory, rooms);
    }
}

abstract class Room {

    String roomType;
    int beds;
    double size;
    double price;

    Room(String roomType, int beds, double size, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq ft");
        System.out.println("Price: $" + price);
    }
}

class SingleRoom extends Room {

    SingleRoom() {
        super("Single Room", 1, 200, 80);
    }
}

class DoubleRoom extends Room {

    DoubleRoom() {
        super("Double Room", 2, 350, 150);
    }
}

class SuiteRoom extends Room {

    SuiteRoom() {
        super("Suite Room", 3, 600, 300);
    }
}

class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

class RoomSearchService {

    public void searchAvailableRooms(RoomInventory inventory, Room[] rooms) {

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.roomType);

            if (available > 0) {
                room.displayRoomDetails();
                System.out.println("Available: " + available);
                System.out.println();
            }
        }
    }
}