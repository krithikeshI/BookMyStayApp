import java.util.*;

// Custom Exception Class
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Hotel Inventory Class
class HotelInventory {
    private Map<String, Integer> rooms;

    public HotelInventory() {
        rooms = new HashMap<>();

        // Initial Room Availability
        rooms.put("Single", 5);
        rooms.put("Double", 3);
        rooms.put("Suite", 2);
    }

    // Validate Room Type
    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!rooms.containsKey(roomType)) {
            throw new InvalidBookingException("Error: Invalid room type -> " + roomType);
        }
    }

    // Validate Booking Count
    public void validateBookingCount(int count) throws InvalidBookingException {
        if (count <= 0) {
            throw new InvalidBookingException("Error: Booking count must be greater than 0");
        }
    }

    // Validate Availability
    public void validateAvailability(String roomType, int count) throws InvalidBookingException {
        int available = rooms.get(roomType);

        if (available < count) {
            throw new InvalidBookingException(
                    "Error: Only " + available + " " + roomType + " room(s) available"
            );
        }
    }

    // Book Room
    public void bookRoom(String roomType, int count) {
        int available = rooms.get(roomType);
        rooms.put(roomType, available - count);
    }

    // Display Inventory
    public void displayInventory() {
        System.out.println("\n--- Current Room Availability ---");
        for (Map.Entry<String, Integer> entry : rooms.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Booking Service Class
class BookingService {
    private HotelInventory inventory;

    public BookingService(HotelInventory inventory) {
        this.inventory = inventory;
    }

    // Process Booking with Validation
    public void processBooking(String roomType, int count) {
        try {
            // Fail-Fast Validations
            inventory.validateRoomType(roomType);
            inventory.validateBookingCount(count);
            inventory.validateAvailability(roomType, count);

            // Booking
            inventory.bookRoom(roomType, count);
            System.out.println("Booking Successful: " + count + " " + roomType + " room(s)");

        } catch (InvalidBookingException e) {
            // Graceful Error Handling
            System.out.println("Booking Failed -> " + e.getMessage());
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        HotelInventory inventory = new HotelInventory();
        BookingService service = new BookingService(inventory);

        System.out.println("=== Welcome to Book My Stay App ===");

        while (true) {
            System.out.println("\nEnter Room Type (Single/Double/Suite) or 'exit' to quit:");
            String roomType = sc.next();

            if (roomType.equalsIgnoreCase("exit")) {
                break;
            }

            System.out.println("Enter number of rooms:");
            int count;

            try {
                count = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a valid number");
                sc.next(); // clear invalid input
                continue;
            }

            // Process Booking
            service.processBooking(roomType, count);

            // Show Inventory After Each Attempt
            inventory.displayInventory();
        }

        System.out.println("\nThank you for using Book My Stay App!");
        sc.close();
    }
}