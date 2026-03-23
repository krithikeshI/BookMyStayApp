import java.util.*;

// Booking Request Model
class BookingRequest {
    String customerName;
    String roomType;

    public BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void printInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// Room Allocation Service
public class BookMyStayApp {

    // Queue for FIFO booking requests
    private static Queue<BookingRequest> requestQueue = new LinkedList<>();

    // Set to ensure unique room IDs
    private static Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type -> allocated room IDs
    private static Map<String, Set<String>> roomAllocationMap = new HashMap<>();

    private static InventoryService inventoryService = new InventoryService();

    public static void main(String[] args) {

        // Sample booking requests
        requestQueue.add(new BookingRequest("Alice", "Single"));
        requestQueue.add(new BookingRequest("Bob", "Double"));
        requestQueue.add(new BookingRequest("Charlie", "Single"));
        requestQueue.add(new BookingRequest("David", "Suite"));
        requestQueue.add(new BookingRequest("Eve", "Single")); // may fail if no inventory

        processBookings();

        System.out.println("\nFinal Allocation:");
        System.out.println(roomAllocationMap);

        inventoryService.printInventory();
    }

    // Process bookings in FIFO order
    private static void processBookings() {
        while (!requestQueue.isEmpty()) {
            BookingRequest request = requestQueue.poll();

            System.out.println("\nProcessing booking for: " + request.customerName);

            if (!inventoryService.isAvailable(request.roomType)) {
                System.out.println("❌ No rooms available for type: " + request.roomType);
                continue;
            }

            // Atomic allocation
            String roomId = generateUniqueRoomId(request.roomType);

            // Record allocation
            allocatedRoomIds.add(roomId);

            roomAllocationMap
                    .computeIfAbsent(request.roomType, k -> new HashSet<>())
                    .add(roomId);

            // Update inventory immediately
            inventoryService.decrement(request.roomType);

            // Confirm booking
            System.out.println("✅ Booking Confirmed!");
            System.out.println("Customer: " + request.customerName);
            System.out.println("Room Type: " + request.roomType);
            System.out.println("Allocated Room ID: " + roomId);
        }
    }

    // Generate unique room ID
    private static String generateUniqueRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType.substring(0, 1).toUpperCase() + UUID.randomUUID().toString().substring(0, 5);
        } while (allocatedRoomIds.contains(roomId)); // ensure uniqueness

        return roomId;
    }
}