import java.util.*;

// Booking Request Class
class BookingRequest {
    String guestName;
    String roomType;
    int count;

    public BookingRequest(String guestName, String roomType, int count) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.count = count;
    }
}

// Thread-Safe Hotel Inventory
class HotelInventory {
    private Map<String, Integer> rooms = new HashMap<>();

    public HotelInventory() {
        rooms.put("Single", 5);
        rooms.put("Double", 3);
        rooms.put("Suite", 2);
    }

    // Synchronized method (Critical Section)
    public synchronized boolean allocateRoom(String roomType, int count, String guestName) {
        if (!rooms.containsKey(roomType)) {
            System.out.println(guestName + " → Invalid Room Type");
            return false;
        }

        int available = rooms.get(roomType);

        if (available >= count) {
            System.out.println(guestName + " is booking " + count + " " + roomType + " room(s)");

            // Simulate delay (to expose race condition if not synchronized)
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}

            rooms.put(roomType, available - count);

            System.out.println("Booking SUCCESS for " + guestName);
            return true;
        } else {
            System.out.println("Booking FAILED for " + guestName + " → Not enough rooms");
            return false;
        }
    }

    public void display() {
        System.out.println("\nFinal Inventory:");
        for (Map.Entry<String, Integer> e : rooms.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

// Shared Booking Queue
class BookingQueue {
    private Queue<BookingRequest> queue = new LinkedList<>();

    // Add request (Producer)
    public synchronized void addRequest(BookingRequest request) {
        queue.add(request);
        notify(); // notify waiting threads
    }

    // Get request (Consumer)
    public synchronized BookingRequest getRequest() {
        while (queue.isEmpty()) {
            try {
                wait(); // wait until request arrives
            } catch (InterruptedException e) {}
        }
        return queue.poll();
    }
}

// Booking Processor Thread
class BookingProcessor extends Thread {
    private BookingQueue queue;
    private HotelInventory inventory;

    public BookingProcessor(BookingQueue queue, HotelInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {
        while (true) {
            BookingRequest request = queue.getRequest();

            // Critical Section handled inside inventory
            inventory.allocateRoom(
                    request.roomType,
                    request.count,
                    request.guestName
            );
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) throws InterruptedException {

        HotelInventory inventory = new HotelInventory();
        BookingQueue queue = new BookingQueue();

        // Create Multiple Processor Threads
        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);
        BookingProcessor t3 = new BookingProcessor(queue, inventory);

        t1.start();
        t2.start();
        t3.start();

        // Simulate Multiple Guests (Concurrent Requests)
        queue.addRequest(new BookingRequest("Guest1", "Single", 2));
        queue.addRequest(new BookingRequest("Guest2", "Single", 3));
        queue.addRequest(new BookingRequest("Guest3", "Single", 2)); // should fail
        queue.addRequest(new BookingRequest("Guest4", "Double", 2));
        queue.addRequest(new BookingRequest("Guest5", "Suite", 1));
        queue.addRequest(new BookingRequest("Guest6", "Suite", 2)); // may fail

        // Allow threads to process
        Thread.sleep(2000);

        inventory.display();

        System.exit(0);
    }
}