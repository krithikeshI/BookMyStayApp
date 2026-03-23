import java.util.*;

// Add-On Service class
class Service {
    private String serviceName;
    private double price;

    public Service(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return serviceName + " (₹" + price + ")";
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<Service>> reservationServicesMap;

    public AddOnServiceManager() {
        reservationServicesMap = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, Service service) {
        reservationServicesMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    // Get services for a reservation
    public List<Service> getServices(String reservationId) {
        return reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total additional cost
    public double calculateTotalServiceCost(String reservationId) {
        List<Service> services = getServices(reservationId);
        double total = 0;

        for (Service service : services) {
            total += service.getPrice();
        }

        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        List<Service> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Add-On Services for Reservation ID: " + reservationId);
        for (Service s : services) {
            System.out.println("- " + s);
        }

        System.out.println("Total Add-On Cost: ₹" + calculateTotalServiceCost(reservationId));
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AddOnServiceManager manager = new AddOnServiceManager();

        // Sample Reservation ID
        System.out.print("Enter Reservation ID: ");
        String reservationId = scanner.nextLine();

        int choice;

        do {
            System.out.println("\n===== Add-On Service Menu =====");
            System.out.println("1. Add Breakfast (₹500)");
            System.out.println("2. Add Airport Pickup (₹1000)");
            System.out.println("3. Add Extra Bed (₹700)");
            System.out.println("4. View Services");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");

            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    manager.addService(reservationId, new Service("Breakfast", 500));
                    System.out.println("Breakfast added.");
                    break;

                case 2:
                    manager.addService(reservationId, new Service("Airport Pickup", 1000));
                    System.out.println("Airport Pickup added.");
                    break;

                case 3:
                    manager.addService(reservationId, new Service("Extra Bed", 700));
                    System.out.println("Extra Bed added.");
                    break;

                case 4:
                    manager.displayServices(reservationId);
                    break;

                case 5:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 5);

        scanner.close();
    }
}