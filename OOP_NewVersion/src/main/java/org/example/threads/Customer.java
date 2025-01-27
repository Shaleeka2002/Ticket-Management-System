package org.example.threads;

import org.example.core.TicketPool;

public class Customer implements Runnable {
    private final int customerId;
    private final TicketPool ticketPool;
    private int ticketsToBuy;

    private static final int MAX_RETRIES = 3; // Maximum retries if not enough tickets are available

    public Customer(int customerId, TicketPool ticketPool) {
        this.customerId = customerId;
        this.ticketPool = ticketPool;
    }

    public void setTicketsToBuy(int ticketsToBuy) {
        this.ticketsToBuy = ticketsToBuy;
    }

    @Override
    public void run() {
        int attempt = 0;
        boolean purchaseSuccessful = false;

        while (attempt < MAX_RETRIES && !purchaseSuccessful) {
            synchronized (ticketPool) {
                if (ticketPool.getAvailableTickets() >= ticketsToBuy) {
                    ticketPool.buyTickets(customerId, ticketsToBuy); // Use the buyTickets method
                    System.out.println("Customer " + customerId + " bought " + ticketsToBuy + " tickets.");
                    purchaseSuccessful = true;
                } else {
                    System.out.println("Customer " + customerId + " attempted to buy " + ticketsToBuy + " tickets, but not enough tickets are available.");
                    attempt++;
                    if (attempt < MAX_RETRIES) {
                        System.out.println("Retrying... (Attempt " + attempt + ")");
                    }
                }
            }

            if (!purchaseSuccessful) {
                try {
                    // Simulate delay before retrying
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Customer " + customerId + " was interrupted during ticket purchase.");
                    return;  // Exiting the thread gracefully upon interruption
                }
            }
        }

        if (!purchaseSuccessful) {
            System.out.println("Customer " + customerId + " failed to buy tickets after " + MAX_RETRIES + " attempts.");
        }
    }
}
