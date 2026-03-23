import java.util.HashMap;
import java.util.Map;

public class BookMyStayApp {

    // Inner class for Room Inventory (Version 3.1 logic)
    static class RoomInventory {

        private Map<String, Integer> inventory;

        // Constructor
        public RoomInventory() {
            inventory = new HashMap<>();
        }

        // Add room type
        public void addRoomType(String roomType, int count) {
            inventory.put(roomType, count);
        }

        // Get availability
        public int getAvailability(String roomType) {
            return inventory.getOrDefault(roomType, 0);
        }

        // Update availability
        public void updateAvailability(String roomType, int change) {
            int current = inventory.getOrDefault(roomType, 0);
            int updated = current + change;

            if (updated < 0) {
                System.out.println("❌ Not enough rooms available for: " + roomType);
            } else {
                inventory.put(roomType, updated);
                System.out.println("✅ Updated " + roomType + " to " + updated);
            }
        }

        // Display inventory
        public void displayInventory() {
            System.out.println("\n📊 Current Room Inventory:");
            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
    }

    // Main method
    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        // Initialize rooms
        inventory.addRoomType("Standard", 10);
        inventory.addRoomType("Deluxe", 5);
        inventory.addRoomType("Suite", 2);

        // Display initial
        inventory.displayInventory();

        // Check availability
        System.out.println("\n🔍 Availability:");
        System.out.println("Standard: " + inventory.getAvailability("Standard"));
        System.out.println("Suite: " + inventory.getAvailability("Suite"));

        // Booking
        System.out.println("\n🛎 Booking:");
        inventory.updateAvailability("Standard", -2);
        inventory.updateAvailability("Suite", -1);

        // Invalid booking
        inventory.updateAvailability("Suite", -5);

        // Cancellation
        System.out.println("\n↩️ Cancellation:");
        inventory.updateAvailability("Standard", +1);

        // Final state
        inventory.displayInventory();
    }
}