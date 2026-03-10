public class bookmystayapp {

    public static void main(String[] args) {

        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        int singleAvailability = 5;
        int doubleAvailability = 3;
        int suiteAvailability = 2;

        System.out.println("Hotel Booking System v2.1");
        System.out.println();

        singleRoom.displayRoomDetails();
        System.out.println("Available: " + singleAvailability);
        System.out.println();

        doubleRoom.displayRoomDetails();
        System.out.println("Available: " + doubleAvailability);
        System.out.println();

        suiteRoom.displayRoomDetails();
        System.out.println("Available: " + suiteAvailability);
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