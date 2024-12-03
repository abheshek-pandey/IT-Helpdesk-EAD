package com.ithelpdesk.controller;

import com.ithelpdesk.service.TicketService;
import javax.swing.*;

public class DashboardController {

    private TicketService ticketService;

    public DashboardController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Constructor for initializing UI components and services
    public void initialize() {
        // Example of initializing the UI elements (e.g., table, buttons, etc.)
        JFrame frame = new JFrame("Dashboard");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
