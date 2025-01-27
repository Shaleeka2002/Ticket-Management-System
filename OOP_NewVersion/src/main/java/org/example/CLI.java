package org.example;

import org.example.core.TicketPool;

import java.util.Scanner;

public class CLI {

    private static TicketPool ticketPool;

    public void initializeSystem() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Ticketing System Configuration!");

        // Get system configuration parameters from the user
        System.out.print("Enter total number of tickets: ");
        int totalTickets = scanner.nextInt();

        System.out.print("Enter ticket release rate: ");
        int ticketReleaseRate = scanner.nextInt();

        System.out.print("Enter customer retrieval rate: ");
        int customerRetrievalRate = scanner.nextInt();

        System.out.print("Enter maximum ticket capacity: ");
        int maxTicketCapacity = scanner.nextInt();

        // Initialize the TicketPool with the given configuration
        ticketPool = new TicketPool(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);

        System.out.println("Configuration Complete!");
        System.out.println("Total Tickets: " + totalTickets);
        System.out.println("Ticket Release Rate: " + ticketReleaseRate);
        System.out.println("Customer Retrieval Rate: " + customerRetrievalRate);
        System.out.println("Maximum Ticket Capacity: " + maxTicketCapacity);
    }

    public static TicketPool getTicketPool() {
        return ticketPool;
    }

    public void run() {
        if (ticketPool == null) {
            System.out.println("System not initialized. Please initialize the system first.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nTicketing System Menu:");
            System.out.println("1. Add tickets (Vendor)");
            System.out.println("2. Remove tickets (Vendor)");
            System.out.println("3. Buy tickets (Customer)");
            System.out.println("4. View system status");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    handleAddTickets(scanner);
                    break;
                case 2:
                    handleRemoveTickets(scanner);
                    break;
                case 3:
                    handleBuyTickets(scanner);
                    break;
                case 4:
                    viewSystemStatus();
                    break;
                case 5:
                    exit = true;
                    System.out.println("Exiting the CLI...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void handleAddTickets(Scanner scanner) {
        System.out.print("Enter Vendor ID: ");
        int vendorId = scanner.nextInt();

        System.out.print("Enter number of tickets to add: ");
        int ticketsToAdd = scanner.nextInt();

        if (ticketsToAdd > 0) {
            ticketPool.addTickets(vendorId, ticketsToAdd);
            System.out.println("Vendor " + vendorId + " added " + ticketsToAdd + " tickets.");
        } else {
            System.out.println("Invalid ticket count. Please try again.");
        }
    }

    private void handleRemoveTickets(Scanner scanner) {
        System.out.print("Enter Vendor ID: ");
        int vendorId = scanner.nextInt();

        System.out.print("Enter number of tickets to remove: ");
        int ticketsToRemove = scanner.nextInt();

        ticketPool.removeTickets(vendorId, ticketsToRemove);
    }

    private void handleBuyTickets(Scanner scanner) {
        System.out.print("Enter Customer ID: ");
        int customerId = scanner.nextInt();

        System.out.print("Enter number of tickets to buy: ");
        int ticketsToBuy = scanner.nextInt();

        ticketPool.buyTickets(customerId, ticketsToBuy);
    }

    private void viewSystemStatus() {
        System.out.println("\nSystem Status:");
        System.out.println("Tickets Available: " + ticketPool.getAvailableTickets());
        System.out.println("Tickets Released per Vendor: " + ticketPool.getVendorTicketCount());
        System.out.println("Tickets Bought by Customers: " + ticketPool.getCustomerTicketCount());
    }
}
