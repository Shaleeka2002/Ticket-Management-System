package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.core.TicketPool;
import org.example.threads.Customer;
import org.example.threads.Vendor;

import java.util.HashMap;
import java.util.Map;

public class GUI extends Application {

    private static TicketPool ticketPool; // Shared TicketPool instance
    private final Label statusLabel = new Label("Welcome to the Ticket System!");
    private final Label ticketsAvailableLabel = new Label("Tickets Available: 0");
    private final Label vendorCountLabel = new Label("Tickets Added by Vendors: 0");
    private final Label customerCountLabel = new Label("Tickets Bought by Customers: 0");

    private int totalTicketsAddedByVendors = 0;
    private int totalTicketsBoughtByCustomers = 0;
    private final Map<Integer, Integer> vendorTicketsMap = new HashMap<>();
    public GUI(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    @Override
    public void start(Stage primaryStage) {
        if (ticketPool == null) {
            throw new IllegalStateException("TicketPool is not set! Please initialize the TicketPool before launching the GUI.");
        }

        VBox root = new VBox(15);
        root.setPadding(new Insets(15));

        VBox ticketStatusBox = new VBox(10);
        ticketStatusBox.setPadding(new Insets(10));
        ticketStatusBox.getChildren().addAll(
                new Label("System Status:"),
                ticketsAvailableLabel,
                vendorCountLabel,
                customerCountLabel,
                statusLabel
        );

        VBox vendorBox = new VBox(10);
        vendorBox.setPadding(new Insets(10));
        vendorBox.setStyle("-fx-border-color: #6A5ACD; -fx-border-width: 2; -fx-border-radius: 5;");
        Label vendorLabel = new Label("Vendor Operations");

        HBox vendorAddBox = new HBox(10);
        TextField ticketsToAddField = new TextField();
        ticketsToAddField.setPromptText("Enter tickets to add");
        TextField vendorIdField = new TextField();
        vendorIdField.setPromptText("Enter Vendor ID");
        Button addButton = new Button("Add Tickets");
        vendorAddBox.getChildren().addAll(vendorIdField, ticketsToAddField, addButton);

        HBox vendorDeleteBox = new HBox(10);
        TextField ticketsToDeleteField = new TextField();
        ticketsToDeleteField.setPromptText("Enter tickets to delete");
        TextField deleteVendorIdField = new TextField();
        deleteVendorIdField.setPromptText("Enter Vendor ID");
        Button deleteButton = new Button("Delete Tickets");
        vendorDeleteBox.getChildren().addAll(deleteVendorIdField, ticketsToDeleteField, deleteButton);

        vendorBox.getChildren().addAll(vendorLabel, vendorAddBox, vendorDeleteBox);

        VBox customerBox = new VBox(10);
        customerBox.setPadding(new Insets(10));
        customerBox.setStyle("-fx-border-color: #32CD32; -fx-border-width: 2; -fx-border-radius: 5;");
        Label customerLabel = new Label("Customer Operations");

        HBox customerBuyBox = new HBox(10);
        TextField ticketsToBuyField = new TextField();
        ticketsToBuyField.setPromptText("Enter tickets to buy");
        TextField customerIdField = new TextField();
        customerIdField.setPromptText("Enter Customer ID");
        Button buyButton = new Button("Buy Tickets");
        customerBuyBox.getChildren().addAll(customerIdField, ticketsToBuyField, buyButton);

        customerBox.getChildren().addAll(customerLabel, customerBuyBox);

        addButton.setOnAction(e -> handleAddTickets(vendorIdField, ticketsToAddField));
        deleteButton.setOnAction(e -> handleDeleteTickets(deleteVendorIdField, ticketsToDeleteField));
        buyButton.setOnAction(e -> handleBuyTickets(customerIdField, ticketsToBuyField));

        root.getChildren().addAll(ticketStatusBox, vendorBox, customerBox);

        Scene scene = new Scene(root, 500, 600);
        primaryStage.setTitle("Ticketing System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleAddTickets(TextField vendorIdField, TextField ticketsToAddField) {
        try {
            int ticketsToAdd = Integer.parseInt(ticketsToAddField.getText());
            int vendorId = Integer.parseInt(vendorIdField.getText().trim());

            if (ticketsToAdd > 0) {
                Vendor vendor = new Vendor(vendorId, ticketPool, ticketsToAdd);
                new Thread(vendor).start();

                Platform.runLater(() -> {
                    totalTicketsAddedByVendors += ticketsToAdd;
                    vendorTicketsMap.put(vendorId, vendorTicketsMap.getOrDefault(vendorId, 0) + ticketsToAdd);
                    statusLabel.setText("Vendor " + vendorId + " added " + ticketsToAdd + " tickets.");
                    updateLabels();
                });
            } else {
                showAlert("Invalid Input", "Enter a valid ticket number.");
            }
        } catch (NumberFormatException ex) {
            showAlert("Invalid Input", "Please enter numeric values for ticket numbers and Vendor ID.");
        }
    }

    private void handleDeleteTickets(TextField vendorIdField, TextField ticketsToDeleteField) {
        try {
            int ticketsToDelete = Integer.parseInt(ticketsToDeleteField.getText());
            int vendorId = Integer.parseInt(vendorIdField.getText().trim());

            if (ticketsToDelete > 0 && vendorTicketsMap.getOrDefault(vendorId, 0) >= ticketsToDelete) {
                ticketPool.removeTickets(vendorId, ticketsToDelete);

                Platform.runLater(() -> {
                    vendorTicketsMap.put(vendorId, vendorTicketsMap.get(vendorId) - ticketsToDelete);
                    totalTicketsAddedByVendors -= ticketsToDelete;
                    statusLabel.setText("Vendor " + vendorId + " deleted " + ticketsToDelete + " tickets.");
                    updateLabels();
                });
            } else {
                showAlert("Invalid Input", "Vendor does not have enough tickets to delete.");
            }
        } catch (NumberFormatException ex) {
            showAlert("Invalid Input", "Please enter numeric values for ticket numbers and Vendor ID.");
        }
    }

    private void handleBuyTickets(TextField customerIdField, TextField ticketsToBuyField) {
        try {
            int ticketsToBuy = Integer.parseInt(ticketsToBuyField.getText());
            int customerId = Integer.parseInt(customerIdField.getText().trim());

            if (ticketsToBuy > 0) {
                Customer customer = new Customer(customerId, ticketPool);
                customer.setTicketsToBuy(ticketsToBuy);
                new Thread(customer).start();

                Platform.runLater(() -> {
                    totalTicketsBoughtByCustomers += ticketsToBuy;
                    statusLabel.setText("Customer " + customerId + " bought " + ticketsToBuy + " tickets.");
                    updateLabels();
                });
            } else {
                showAlert("Invalid Input", "Enter a valid ticket number.");
            }
        } catch (NumberFormatException ex) {
            showAlert("Invalid Input", "Please enter numeric values for ticket numbers and Customer ID.");
        }
    }

    private void updateLabels() {
        ticketsAvailableLabel.setText("Tickets Available: " + ticketPool.getAvailableTickets());
        vendorCountLabel.setText("Tickets Added by Vendors: " + totalTicketsAddedByVendors);
        customerCountLabel.setText("Tickets Bought by Customers: " + totalTicketsBoughtByCustomers);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void setTicketPool(TicketPool ticketPool) {
        GUI.ticketPool = ticketPool;
    }
}
