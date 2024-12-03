package com.ithelpdesk.ui;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class ArticleDetailsFrame extends JPanel {
   private final MainFrame mainFrame;
   private JLabel titleLabel;
   private JTextArea contentArea;
   private JLabel createdByLabel;
   private JLabel createdAtLabel;
   private JLabel lastUpdatedLabel;
   private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");

   public ArticleDetailsFrame(MainFrame mainFrame) {
       this.mainFrame = mainFrame;
       initComponents();
   }

   private void initComponents() {
       setLayout(new BorderLayout(10, 10));
       setBackground(new Color(240, 242, 245));

       add(createHeaderPanel(), BorderLayout.NORTH);
       add(createDetailsPanel(), BorderLayout.CENTER);
   }

   private JPanel createHeaderPanel() {
       JPanel panel = new JPanel(new BorderLayout());
       panel.setBackground(new Color(240, 242, 245));

       JLabel headerLabel = new JLabel("Article Details");
       headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
       headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

       panel.add(headerLabel, BorderLayout.NORTH);
       return panel;
   }

   private JPanel createDetailsPanel() {
	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.setBackground(new Color(240, 242, 245));

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.anchor = GridBagConstraints.WEST;
	    gbc.insets = new Insets(5, 5, 5, 5);

	    // Updated fields to match database schema
	    titleLabel = new JLabel();
	    contentArea = new JTextArea(10, 40);
	    contentArea.setEditable(false);
	    contentArea.setLineWrap(true);
	    createdByLabel = new JLabel();
	    createdAtLabel = new JLabel();
	    lastUpdatedLabel = new JLabel();

	    addDetailField(panel, "Title:", titleLabel, gbc);
	    addDetailField(panel, "Content:", new JScrollPane(contentArea), gbc);
	    addDetailField(panel, "Created By:", createdByLabel, gbc);
	    addDetailField(panel, "Created At:", createdAtLabel, gbc);
	    addDetailField(panel, "Last Updated:", lastUpdatedLabel, gbc);

	    return panel;
	}

   private JLabel createStyledLabel(String text) {
       JLabel label = new JLabel(text);
       label.setFont(new Font("Arial", Font.PLAIN, 14));
       return label;
   }

   private JTextArea createStyledTextArea() {
       JTextArea textArea = new JTextArea(10, 40);
       textArea.setLineWrap(true);
       textArea.setWrapStyleWord(true);
       textArea.setEditable(false);
       textArea.setFont(new Font("Arial", Font.PLAIN, 14));
       textArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
       return textArea;
   }

   private void addDetailField(JPanel panel, String labelText, Component component, GridBagConstraints gbc) {
       JLabel label = new JLabel(labelText);
       label.setFont(new Font("Arial", Font.BOLD, 14));
       
       gbc.gridx = 0;
       gbc.gridy++;
       panel.add(label, gbc);
       
       gbc.gridx = 1;
       gbc.fill = (component instanceof JScrollPane) ? GridBagConstraints.BOTH : GridBagConstraints.HORIZONTAL;
       panel.add(component, gbc);
   }

   public void setArticleDetails(String title, String content, String createdBy, String createdAt, String lastUpdated) {
       titleLabel.setText(title);
       contentArea.setText(content);
       createdByLabel.setText(createdBy);
       createdAtLabel.setText(createdAt);
       lastUpdatedLabel.setText(lastUpdated);
   }
}
