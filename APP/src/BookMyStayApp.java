import java.util.*;

// Room domain model
class Room {
    private String type;
    private double price;
    private List<String> amenities;

    public Room(String type, double price, List<String> amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }
}

// Inventory (State Holder)
class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    // Read-only access
    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public Set<String> getAllRoomTypes() {
        return availability.keySet();
    }
}

// Search Service (Read-only logic)
class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomDetails;

    public SearchService(Inventory inventory, Map<String, Room> roomDetails) {
        this.inventory = inventory;
        this.roomDetails = roomDetails;
    }

    public void searchAvailableRooms() {
        System.out.println("\nAvailable Rooms:\n");

        for (String type : inventory.getAllRoomTypes()) {

            int availableCount = inventory.getAvailability(type);

            // Validation: filter unavailable rooms
            if (availableCount > 0) {
                Room room = roomDetails.get(type);

                // Defensive check
                if (room != null) {
                    System.out.println("Room Type: " + room.getType());
                    System.out.println("Price: ₹" + room.getPrice());
                    System.out.println("Amenities: " + room.getAmenities());
                    System.out.println("Available: " + availableCount);
                    System.out.println("---------------------------");
                }
            }
        }
    }
}

// Main class
public class BookMyStayApp {
    public static void main(String[] args) {

        // Create Inventory
        Inventory inventory = new Inventory();
        inventory.addRoom("Single", 3);
        inventory.addRoom("Double", 0);
        inventory.addRoom("Suite", 2);

        // Create Room Details (Domain Model)
        Map<String, Room> roomDetails = new HashMap<>();

        roomDetails.put("Single",
                new Room("Single", 2000, Arrays.asList("WiFi", "TV")));

        roomDetails.put("Double",
                new Room("Double", 3500, Arrays.asList("WiFi", "TV", "AC")));

        roomDetails.put("Suite",
                new Room("Suite", 6000, Arrays.asList("WiFi", "TV", "AC", "Mini Bar")));

        // Search Service
        SearchService searchService = new SearchService(inventory, roomDetails);

        // Guest initiates search
        searchService.searchAvailableRooms();
    }
}