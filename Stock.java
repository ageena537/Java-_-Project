import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
class Stock {
    String name;
    String category;
    int quantity;
    Stock(String name, String category, int quantity) {
        this.name = name;
        this.category = category;
        this.quantity = quantity;
    }
    String getName() { return name; }
    String getCategory() { return category; }
    int getQuantity() { return quantity; }
}
class StockMonitoringView extends JFrame {
    JTable t1;
    DefaultTableModel t2;
    JTextField nameField, quantityField;
    JComboBox<String> categoryBox;
    List<Stock> products;
    StockMonitoringView() {
        products = new ArrayList<>();
        setTitle("Stock Monitoring System");
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        // Create a panel to hold the title and input fields vertically
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Stock Monitoring", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        northPanel.add(title);
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        p.setBackground(Color.WHITE);
        nameField = new JTextField(12);
        quantityField = new JTextField(5);
        String[] categories = {"Electronics", "Groceries", "Clothes", "Stationery", "Others"};
        categoryBox = new JComboBox<>(categories);
        JButton addButton = new JButton("Add Product");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JButton backButton = new JButton("Back to Home");
        backButton.addActionListener(e -> {
            dispose();
            new InventoryFrontPage();
        });
        p.add(new JLabel("Product Name:"));
        p.add(nameField);
        p.add(new JLabel("Category:"));
        p.add(categoryBox);
        p.add(new JLabel("Quantity:"));
        p.add(quantityField);
        p.add(addButton);
        p.add(backButton);
        northPanel.add(p);
        add(northPanel, BorderLayout.NORTH);
        t2 = new DefaultTableModel(new Object[]{"Product Name", "Category", "Quantity", "Status"}, 0);
        t1 = new JTable(t2);
        t1.setRowHeight(25);
        t1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(t1);
        add(scrollPane, BorderLayout.CENTER);
        addButton.addActionListener(e -> addProduct());
        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }
    void addProduct() {
        String name = nameField.getText();
        String category = (String) categoryBox.getSelectedItem();
        String qtyText = quantityField.getText();
        if (name.isEmpty() || qtyText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(qtyText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Stock p = new Stock(name, category, quantity);
        products.add(p);
        String status = quantity < 10 ? "Low Stock" : "OK";
        t2.addRow(new Object[]{p.getName(), p.getCategory(), p.getQuantity(), status});
        nameField.setText("");
        quantityField.setText("");
        categoryBox.setSelectedIndex(0);
    }
    public static void main(String[] args) {
        new StockMonitoringView();
    }
}
