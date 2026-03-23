import java.util.*;

// Reservation Class
class Reservation {
    private int bookingId;
    private String customerName;
    private String roomType;
    private int nights;

    public Reservation(int bookingId, String customerName, String roomType, int nights) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "Booking ID: " + bookingId +
                ", Customer: " + customerName +
                ", Room: " + roomType +
                ", Nights: " + nights;
    }
}

// Booking History Class
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    // Store confirmed booking
    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    // Retrieve all bookings
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// Booking Report Service
class BookingReportService {

    // Generate summary report
    public void generateReport(List<Reservation> reservations) {
        System.out.println("\n--- Booking Report ---");

        if (reservations.isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }

        int totalBookings = reservations.size();
        int totalNights = 0;

        for (Reservation r : reservations) {
            totalNights += r.getNights();
        }

        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Nights Booked: " + totalNights);

        System.out.println("\nDetailed Bookings:");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        BookingHistory bookingHistory = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulating confirmed bookings
        Reservation r1 = new Reservation(101, "Alice", "Deluxe", 2);
        Reservation r2 = new Reservation(102, "Bob", "Suite", 3);
        Reservation r3 = new Reservation(103, "Charlie", "Standard", 1);

        // Add to booking history
        bookingHistory.addReservation(r1);
        bookingHistory.addReservation(r2);
        bookingHistory.addReservation(r3);

        // Admin retrieves booking history
        List<Reservation> allBookings = bookingHistory.getAllReservations();

        // Generate report
        reportService.generateReport(allBookings);
    }
}