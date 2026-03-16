import java.util.*;


class AddOnService {
    private String name;
    private double cost;

    public AddOnService(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return name + " (₹" + cost + ")";
    }
}

class AddOnServiceManager {
    private Map<String, List<AddOnService>> reservationServices = new HashMap<>();

    public void addServiceToReservation(String reservationId, AddOnService service) {
        reservationServices
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    public List<AddOnService> getServicesForReservation(String reservationId) {
        return reservationServices.getOrDefault(reservationId, Collections.emptyList());
    }


    public double calculateAdditionalCost(String reservationId) {
        return getServicesForReservation(reservationId)
                .stream()
                .mapToDouble(AddOnService::getCost)
                .sum();
    }
}

public class bookmystayapp {
    public static void main(String[] args) {
        AddOnServiceManager manager = new AddOnServiceManager();

        String reservation1 = "RES123";
        String reservation2 = "RES456";

        AddOnService breakfast = new AddOnService("Breakfast", 500.0);
        AddOnService spa = new AddOnService("Spa Access", 1500.0);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 800.0);

        manager.addServiceToReservation(reservation1, breakfast);
        manager.addServiceToReservation(reservation1, spa);

        manager.addServiceToReservation(reservation2, airportPickup);


        System.out.println("Reservation " + reservation1 + " selected services: "
                + manager.getServicesForReservation(reservation1));
        System.out.println("Additional cost: ₹" + manager.calculateAdditionalCost(reservation1));

        System.out.println("Reservation " + reservation2 + " selected services: "
                + manager.getServicesForReservation(reservation2));
        System.out.println("Additional cost: ₹" + manager.calculateAdditionalCost(reservation2));
    }
}