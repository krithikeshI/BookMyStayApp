import java.util.LinkedList;
import java.util.Queue;

// Reservation class representing a booking request
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "Reservation [Guest=" + guestName + ", RoomType=" + roomType + "]";
    }
}

// Booking Request Queue Manager
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request (enqueue)
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added: " + reservation);
    }

    // View next request (without removing)
    public Reservation peekNextRequest() {
        return queue.peek();
    }

    // Process request (dequeue)
    public Reservation processRequest() {
        return queue.poll();
    }

    // Display all requests
    public void displayQueue() {
        if (queue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }

        System.out.println("\nCurrent Booking Request Queue:");
        for (Reservation r : queue) {
            System.out.println(r);
        }
    }
}

// Main class
public class BookMyStayApp {
    public static void main(String[] args) {

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Simulating incoming booking requests
        bookingQueue.addRequest(new Reservation("Alice", "Deluxe"));
        bookingQueue.addRequest(new Reservation("Bob", "Standard"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite"));

        // Display queue (FIFO order)
        bookingQueue.displayQueue();

        // Peek next request
        System.out.println("\nNext request to process: " + bookingQueue.peekNextRequest());

        // Process requests (FIFO)
        System.out.println("\nProcessing request: " + bookingQueue.processRequest());
        System.out.println("Processing request: " + bookingQueue.processRequest());

        // Display remaining queue
        bookingQueue.displayQueue();
    }
}