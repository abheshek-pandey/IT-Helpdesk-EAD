package com.ithelpdesk.controller;

import com.ithelpdesk.service.MetricsService;
import javax.swing.*;

public class MetricsController {

    private MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    // Constructor for initializing UI components and services
    public void initialize() {
        // Example of initializing the metrics display (e.g., labels, charts, etc.)
        JFrame frame = new JFrame("Metrics");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}