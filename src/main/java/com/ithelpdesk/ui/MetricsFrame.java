package com.ithelpdesk.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import org.jfree.chart.*;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;
import com.ithelpdesk.dao.DatabaseInterface;

public class MetricsFrame extends JPanel {
    private final MainFrame mainFrame;
    private JLabel averageResolutionTimeLabel;
    private JTable activeTicketsTable;
    private JButton refreshButton;
    private DefaultTableModel tableModel;
    private DatabaseInterface database;

    public MetricsFrame(MainFrame mainFrame, DatabaseInterface database) {
        this.mainFrame = mainFrame;
        this.database = database;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245));

        // Header and title
        add(createHeaderPanel(), BorderLayout.NORTH);

        // Metrics panel with charts
        add(createMetricsPanel(), BorderLayout.CENTER);

        // Button panel for refreshing metrics
        add(createButtonPanel(), BorderLayout.SOUTH);

        loadMetrics();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 242, 245));

        JLabel headerLabel = new JLabel("Metrics Dashboard");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(headerLabel);

        return panel;
    }

    private JPanel createMetricsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 242, 245));

        // Metrics for average resolution time
        averageResolutionTimeLabel = new JLabel("Average Resolution Time: -");
        averageResolutionTimeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // Create charts
        JFreeChart activeTicketsChart = createActiveTicketsChart();
        ChartPanel activeTicketsChartPanel = new ChartPanel(activeTicketsChart);
        activeTicketsChartPanel.setPreferredSize(new Dimension(500, 300));

        JFreeChart avgResolutionTimeSeverityChart = createAvgResolutionTimeBySeverityChart();
        ChartPanel avgResolutionTimeSeverityPanel = new ChartPanel(avgResolutionTimeSeverityChart);
        avgResolutionTimeSeverityPanel.setPreferredSize(new Dimension(500, 300));

        JFreeChart avgResolutionTimePriorityChart = createAvgResolutionTimeByPriorityChart();
        ChartPanel avgResolutionTimePriorityPanel = new ChartPanel(avgResolutionTimePriorityChart);
        avgResolutionTimePriorityPanel.setPreferredSize(new Dimension(500, 300));

        // Metrics table
        String[] columns = {"Metric", "Value"};
        tableModel = new DefaultTableModel(columns, 0);
        activeTicketsTable = new JTable(tableModel);

        // Add elements to the panel
        panel.add(averageResolutionTimeLabel, BorderLayout.NORTH);
        panel.add(activeTicketsChartPanel, BorderLayout.WEST);
        panel.add(avgResolutionTimeSeverityPanel, BorderLayout.CENTER);
        panel.add(avgResolutionTimePriorityPanel, BorderLayout.EAST);
        panel.add(new JScrollPane(activeTicketsTable), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(240, 242, 245));

        refreshButton = new JButton("Refresh Metrics");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.setBackground(new Color(52, 152, 219));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setOpaque(true);
        
        refreshButton.addActionListener(e -> loadMetrics());

        panel.add(refreshButton);
        return panel;
    }

    // Pie Chart for Active Tickets
    private JFreeChart createActiveTicketsChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        try {
            int openTickets = database.getTicketsByStatus("Open").size();
            int inProgressTickets = database.getTicketsByStatus("In Progress").size();
            int resolvedTickets = database.getTicketsByStatus("Resolved").size();
            int closedTickets = database.getTicketsByStatus("Closed").size();

            dataset.setValue("Open", openTickets);
            dataset.setValue("In Progress", inProgressTickets);
            dataset.setValue("Resolved", resolvedTickets);
            dataset.setValue("Closed", closedTickets);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ChartFactory.createPieChart(
                "Active Tickets by Status",
                dataset,
                true, // Include legend
                true, // Include tooltips
                false // No URLs
        );
    }

    // Bar Chart for Average Resolution Time by Severity
    private JFreeChart createAvgResolutionTimeBySeverityChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            dataset.addValue(database.getAverageResolutionTimeBySeverity("1"), "Resolution Time", "Severity 1");
            dataset.addValue(database.getAverageResolutionTimeBySeverity("2"), "Resolution Time", "Severity 2");
            dataset.addValue(database.getAverageResolutionTimeBySeverity("3"), "Resolution Time", "Severity 3");
            dataset.addValue(database.getAverageResolutionTimeBySeverity("4"), "Resolution Time", "Severity 4");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ChartFactory.createBarChart(
                "Average Resolution Time by Severity",
                "Severity Level",
                "Average Resolution Time (hours)",
                dataset,
                PlotOrientation.VERTICAL,
                true, // Include legend
                true, // Include tooltips
                false // No URLs
        );
    }

    // Bar Chart for Average Resolution Time by Priority
    private JFreeChart createAvgResolutionTimeByPriorityChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            dataset.addValue(database.getAverageResolutionTimeByPriority("Very High"), "Resolution Time", "Very High");
            dataset.addValue(database.getAverageResolutionTimeByPriority("High"), "Resolution Time", "High");
            dataset.addValue(database.getAverageResolutionTimeByPriority("Moderate"), "Resolution Time", "Moderate");
            dataset.addValue(database.getAverageResolutionTimeByPriority("Low"), "Resolution Time", "Low");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ChartFactory.createBarChart(
                "Average Resolution Time by Priority",
                "Priority Level",
                "Average Resolution Time (hours)",
                dataset,
                PlotOrientation.VERTICAL,
                true, // Include legend
                true, // Include tooltips
                false // No URLs
        );
    }

    // Load metrics data (both the charts and the table)
    private void loadMetrics() {
        // Update average resolution time
        averageResolutionTimeLabel.setText("Average Resolution Time: 24.5 hours");

        // Update metrics table with ticket data
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{"Total Tickets", "100"});
        tableModel.addRow(new Object[]{"Open Tickets", "25"});
        tableModel.addRow(new Object[]{"In Progress", "35"});
        tableModel.addRow(new Object[]{"Resolved", "40"});
        tableModel.addRow(new Object[]{"Resolution Rate", "85%"});
    }
}
