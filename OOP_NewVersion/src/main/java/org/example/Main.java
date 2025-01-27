package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.core.TicketPool;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize CLI system and TicketPool
        CLI cli = new CLI();
        cli.initializeSystem(); // Initializes TicketPool

        // Get the initialized TicketPool from CLI
        TicketPool ticketPool = CLI.getTicketPool();

        // Create and show the GUI
        GUI gui = new GUI(ticketPool);
        gui.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
