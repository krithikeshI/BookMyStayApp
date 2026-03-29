import java.io.*;
import java.util.*;

// Booking Class (Serializable)
class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    String bookingId;
    String roomType;
    int count;

    public Booking(String bookingId, String roomType, int count) {
        this.bookingId = bookingId;
        this.roomType = roomType;
        this.count = count;
    }
}

// Wrapper class to persist entire system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    Map<String, Booking> bookings;

    public SystemState(Map<String, Integer> inventory, Map<String, Booking> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

// Hotel Inventory
class HotelInventory {
    private Map<String, Integer> rooms;

    public HotelInventory() {
        rooms = new HashMap<>();
        rooms.put("Single", 5);
        rooms.put("Double", 3);
        rooms.put("Suite", 2);
    }

    public Map<String, Integer> getRooms() {
        return rooms;
    }

    public void setRooms(Map<String, Integer> rooms) {
        this.rooms = rooms;
    }

    public boolean allocate(String type, int count) {
        if (!rooms.containsKey(type)) return false;

        int available = rooms.get(type);
        if (available >= count) {
            rooms.put(type, available - count);
            return true;
        }
        return false;
    }

    public void display() {
        System.out.println("\nInventory:");
        for (Map.Entry<String, Integer> e : rooms.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

// Booking Service
class BookingService {
    private HotelInventory inventory;
    private Map<String, Booking> bookings;
    private int counter;

    public BookingService(HotelInventory inventory) {
        this.inventory = inventory;
        this.bookings = new HashMap<>();
        this.counter = 1;
    }

    public void setBookings(Map<String, Booking> bookings) {
        this.bookings = bookings;
        this.counter = bookings.size() + 1;
    }

    public Map<String, Booking> getBookings() {
        return bookings;
    }

    public String createBooking(String type, int count) {
        if (inventory.allocate(type, count)) {
            String id = "B" + counter++;
            Booking b = new Booking(id, type, count);
            bookings.put(id, b);
            System.out.println("Booking Success → ID: " + id);
            return id;
        } else {
            System.out.println("Booking Failed");
            return null;
        }
    }

    public void showBookings() {
        System.out.println("\nBookings:");
        for (Booking b : bookings.values()) {
            System.out.println(b.bookingId + " | " + b.roomType + " | " + b.count);
        }
    }
}

// Persistence Service
class PersistenceService {
    private static final String FILE_NAME = "system_state.dat";

    // Save state
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    // Load state
    public static SystemState load() {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) ois.readObject();
            System.out.println("System state loaded successfully.");
            return state;

        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Corrupted data. Starting fresh.");
        }
        return null;
    }
}

// Main Class
public class BookMyStayApp{
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        HotelInventory inventory = new HotelInventory();
        BookingService service = new BookingService(inventory);

        // Load persisted data
        SystemState loadedState = PersistenceService.load();
        if (loadedState != null) {
            inventory.setRooms(loadedState.inventory);
            service.setBookings(loadedState.bookings);
        }

        while (true) {
            System.out.println("\n1. Book Room");
            System.out.println("2. View Bookings");
            System.out.println("3. View Inventory");
            System.out.println("4. Exit (Save Data)");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter Room Type (Single/Double/Suite): ");
                    String type = sc.next();

                    System.out.print("Enter Count: ");
                    int count = sc.nextInt();

                    service.createBooking(type, count);
                    break;

                case 2:
                    service.showBookings();
                    break;

                case 3:
                    inventory.display();
                    break;

                case 4:
                    // Save before exit
                    SystemState state = new SystemState(
                            inventory.getRooms(),
                            service.getBookings()
                    );

                    PersistenceService.save(state);

                    System.out.println("Exiting system...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}