import java.util.*;

// Custom Exception
class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
}

// Booking Class
class Booking {
    String bookingId;
    String roomType;
    int count;
    boolean isCancelled;

    public Booking(String bookingId, String roomType, int count) {
        this.bookingId = bookingId;
        this.roomType = roomType;
        this.count = count;
        this.isCancelled = false;
    }
}

// Hotel Inventory
class HotelInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public HotelInventory() {
        rooms.put("Single", 5);
        rooms.put("Double", 3);
        rooms.put("Suite", 2);
    }

    public void validateRoomType(String roomType) throws BookingException {
        if (!rooms.containsKey(roomType)) {
            throw new BookingException("Invalid room type");
        }
    }

    public void validateAvailability(String roomType, int count) throws BookingException {
        if (count <= 0) {
            throw new BookingException("Invalid room count");
        }

        int available = rooms.get(roomType);
        if (available < count) {
            throw new BookingException("Insufficient rooms available");
        }
    }

    public void allocate(String roomType, int count) {
        rooms.put(roomType, rooms.get(roomType) - count);
    }

    public void release(String roomType, int count) {
        rooms.put(roomType, rooms.get(roomType) + count);
    }

    public void display() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> e : rooms.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

// Booking Service
class BookingService {
    private HotelInventory inventory;
    private Map<String, Booking> bookings = new HashMap<>();
    private Stack<String> rollbackStack = new Stack<>();
    private int idCounter = 1;

    public BookingService(HotelInventory inventory) {
        this.inventory = inventory;
    }

    // Create Booking
    public String createBooking(String roomType, int count) {
        try {
            inventory.validateRoomType(roomType);
            inventory.validateAvailability(roomType, count);

            String bookingId = "B" + idCounter++;
            Booking booking = new Booking(bookingId, roomType, count);

            inventory.allocate(roomType, count);
            bookings.put(bookingId, booking);

            // Push to stack (simulate allocated room IDs)
            for (int i = 0; i < count; i++) {
                rollbackStack.push(bookingId);
            }

            System.out.println("Booking Successful! ID: " + bookingId);
            return bookingId;

        } catch (BookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
            return null;
        }
    }

    // Cancel Booking
    public void cancelBooking(String bookingId) {
        try {
            if (!bookings.containsKey(bookingId)) {
                throw new BookingException("Booking does not exist");
            }

            Booking booking = bookings.get(bookingId);

            if (booking.isCancelled) {
                throw new BookingException("Booking already cancelled");
            }

            // Rollback using stack (LIFO)
            int released = 0;
            Stack<String> tempStack = new Stack<>();

            while (!rollbackStack.isEmpty() && released < booking.count) {
                String id = rollbackStack.pop();

                if (id.equals(bookingId)) {
                    released++;
                } else {
                    tempStack.push(id);
                }
            }

            // Restore remaining stack
            while (!tempStack.isEmpty()) {
                rollbackStack.push(tempStack.pop());
            }

            // Restore inventory
            inventory.release(booking.roomType, booking.count);

            // Mark cancelled
            booking.isCancelled = true;

            System.out.println("Cancellation Successful for Booking ID: " + bookingId);

        } catch (BookingException e) {
            System.out.println("Cancellation Failed: " + e.getMessage());
        }
    }

    public void showBookings() {
        System.out.println("\nBooking History:");
        for (Booking b : bookings.values()) {
            System.out.println(
                    b.bookingId + " | " + b.roomType + " | " + b.count +
                            " | " + (b.isCancelled ? "Cancelled" : "Active")
            );
        }
    }
}

// Main Class
public class BookMyStayApp{
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        HotelInventory inventory = new HotelInventory();
        BookingService service = new BookingService(inventory);

        while (true) {
            System.out.println("\n1. Book Room");
            System.out.println("2. Cancel Booking");
            System.out.println("3. View Bookings");
            System.out.println("4. View Inventory");
            System.out.println("5. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter Room Type (Single/Double/Suite): ");
                    String type = sc.next();

                    System.out.print("Enter count: ");
                    int count = sc.nextInt();

                    service.createBooking(type, count);
                    break;

                case 2:
                    System.out.print("Enter Booking ID: ");
                    String id = sc.next();

                    service.cancelBooking(id);
                    break;

                case 3:
                    service.showBookings();
                    break;

                case 4:
                    inventory.display();
                    break;

                case 5:
                    System.out.println("Thank you!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}