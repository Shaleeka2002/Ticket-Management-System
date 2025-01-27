package org.example.core;

import java.util.HashMap;
import java.util.Map;

public class TicketPool {
    private int availableTickets;
    private final int ticketReleaseRate;
    private final int customerRetrievalRate;
    private final int maxTicketCapacity;

    // Map to track vendor tickets
    private final Map<Integer, Integer> vendorTicketsMap;

    public TicketPool(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.availableTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
        this.vendorTicketsMap = new HashMap<>();
    }

    // Synchronized to ensure thread safety
    public synchronized void addTickets(int vendorId, int ticketsToAdd) {
        if (ticketsToAdd <= 0) {
            System.out.println("Error: Tickets to add must be greater than 0.");
            return;
        }

        int newAvailableTickets = availableTickets + ticketsToAdd;
        if (newAvailableTickets <= maxTicketCapacity) {
            availableTickets = newAvailableTickets;
            vendorTicketsMap.put(vendorId, vendorTicketsMap.getOrDefault(vendorId, 0) + ticketsToAdd);
        } else {
            availableTickets = maxTicketCapacity;  // Prevent exceeding max capacity
            System.out.println("Warning: Max ticket capacity reached, no more tickets can be added.");
        }
    }

    // Synchronized to ensure thread safety
    public synchronized void removeTickets(int vendorId, int ticketsToDelete) {
        if (ticketsToDelete <= 0) {
            System.out.println("Error: Tickets to delete must be greater than 0.");
            return;
        }

        int currentVendorTickets = vendorTicketsMap.getOrDefault(vendorId, 0);
        if (currentVendorTickets >= ticketsToDelete) {
            vendorTicketsMap.put(vendorId, currentVendorTickets - ticketsToDelete);
            availableTickets -= ticketsToDelete;
            if (availableTickets < 0) {
                availableTickets = 0;  // Ensure available tickets don't go negative
            }
            System.out.println("Vendor " + vendorId + " removed " + ticketsToDelete + " tickets.");
        } else {
            System.out.println("Error: Vendor " + vendorId + " does not have enough tickets to delete.");
        }
    }

    // Synchronized to ensure thread safety
    public synchronized void buyTickets(int customerId, int ticketsToBuy) {
        if (ticketsToBuy <= 0) {
            System.out.println("Error: Tickets to buy must be greater than 0.");
            return;
        }

        if (availableTickets >= ticketsToBuy) {
            availableTickets -= ticketsToBuy;
            System.out.println("Customer " + customerId + " successfully bought " + ticketsToBuy + " tickets.");
        } else {
            System.out.println("Error: Not enough tickets available for Customer " + customerId + " to buy.");
        }
    }

    // Method to get the current available tickets
    public synchronized int getAvailableTickets() {
        return availableTickets;
    }

    // Getter for ticket release rate (if needed in other parts of the system)
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    // Getter for customer retrieval rate (if needed in other parts of the system)
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    // Getter for max ticket capacity (if needed in other parts of the system)
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    // Getter for vendor tickets map (useful for debugging and tracking vendor actions)
    public Map<Integer, Integer> getVendorTicketsMap() {
        return vendorTicketsMap;
    }

    // Method to get the total tickets added by vendors
    public String getVendorTicketCount() {
        int totalVendorTickets = 0;
        for (int tickets : vendorTicketsMap.values()) {
            totalVendorTickets += tickets;
        }
        return String.valueOf(totalVendorTickets);
    }

    // Method to get the total tickets bought by customers
    public String getCustomerTicketCount() {
        return String.valueOf(getAvailableTickets()); // Available tickets represent the tickets that have been bought
    }
}
