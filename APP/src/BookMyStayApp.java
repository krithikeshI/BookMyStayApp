// Version 2.1 - Refactored Implementation

// Abstract class representing a generic Room
abstract class Room {
    private String roomType;
    private int numberOfBeds;
    private double size;
    private double price;

    // Constructor
    public Room(String roomType, int numberOfBeds, double size, double price) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.size = size;
        this.price = price;
    }

    // Getters (Encapsulation)
    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    // Abstract method (forces subclasses to define their behavior)
    public abstract void displayRoomDetails();
}

// Single Room Class
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 200.0, 1500.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Size: " + getSize() + " sq.ft");
        System.out.println("Price: ₹" + getPrice());
    }
}

// Double Room Class
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 350.0, 2500.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Size: " + getSize() + " sq.ft");
        System.out.println("Price: ₹" + getPrice());
    }
}

// Suite Room Class
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 600.0, 5000.0);
    }

    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + getNumberOfBeds());
        System.out.println("Size: " + getSize() + " sq.ft");
        System.out.println("Price: ₹" + getPrice());
    }
}

// Main Application Class
public class BookMyStayApp {

    public static void main(String[] args) {

        // Creating Room Objects (Polymorphism)
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Static Availability Variables
        int singleRoomAvailability = 10;
        int doubleRoomAvailability = 5;
        int suiteRoomAvailability = 2;

        // Display Room Details and Availability
        System.out.println("===== HOTEL ROOM DETAILS =====\n");

        singleRoom.displayRoomDetails();
        System.out.println("Available: " + singleRoomAvailability);
        System.out.println("-----------------------------");

        doubleRoom.displayRoomDetails();
        System.out.println("Available: " + doubleRoomAvailability);
        System.out.println("-----------------------------");

        suiteRoom.displayRoomDetails();
        System.out.println("Available: " + suiteRoomAvailability);
        System.out.println("-----------------------------");

        System.out.println("\n===== END OF LIST =====");
    }
}
