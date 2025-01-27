package org.example.threads;

import org.example.core.TicketPool;

public class Vendor implements Runnable {

    private final int vendorId;
    private final TicketPool ticketPool;
    private final int ticketsToAdd;

    private static final int MAX_RETRIES = 3; // Maximum retry attempts if adding tickets fails
    private int totalTicketsAdded = 0;

    public Vendor(int vendorId, TicketPool ticketPool, int ticketsToAdd) {
        this.vendorId = vendorId;
        this.ticketPool = ticketPool;
        this.ticketsToAdd = ticketsToAdd;
    }

    @Override
    public void run() {
        int attempt = 0;
        boolean ticketsAdded = false;

        while (attempt < MAX_RETRIES && !ticketsAdded) {
            synchronized (ticketPool) { // Synchronize to ensure thread safety
                try {
                    // Simulate some processing delay (e.g., ticket addition time)
                    Thread.sleep(1000);

                    // Try adding tickets to the pool
                    ticketPool.addTickets(vendorId, ticketsToAdd);
                    ticketsAdded = true; // If successful, exit the loop
                    System.out.println("Vendor " + vendorId + " successfully added " + ticketsToAdd + " tickets.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Vendor " + vendorId + " was interrupted.");
                    break; // Stop if the thread was interrupted
                } catch (Exception e) {
                    attempt++;
                    System.out.println("Vendor " + vendorId + " failed to add tickets (Attempt " + attempt + " of " + MAX_RETRIES + "). Retrying...");
                    if (attempt == MAX_RETRIES) {
                        System.out.println("Vendor " + vendorId + " failed to add tickets after " + MAX_RETRIES + " attempts.");
                    }
                }
            }
            if (!ticketsAdded) {
                try {
                    Thread.sleep(500); // Retry delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Vendor " + vendorId + " was interrupted during retry delay.");
                    return;  // Exit if interrupted during sleep
                }
            }
        }
    }

    public int getTotalTicketsAdded() {
        return totalTicketsAdded;
    }
}
