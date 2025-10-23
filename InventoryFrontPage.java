import javax.swing.*;
import java.awt.*;

public class InventoryFrontPage extends JFrame {
    public InventoryFrontPage() {
        setTitle("Inventory Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set layout
        setLayout(new BorderLayout());

        // Heading label
        JLabel heading = new JLabel("INVENTORY MANAGEMENT SYSTEM", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 28));
        add(heading, BorderLayout.NORTH);

        // Panel to hold buttons
        JPanel panel = new JPanel(new GridLayout(2, 2, 50, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 110, 50, 110));

        // Create buttons
        JButton btnProduct = new JButton("Product Management");
        JButton btnStocks = new JButton("Stocks");
        JButton btnCustomer = new JButton("Customer Manager");
        JButton btnSales = new JButton("Sales");

        btnCustomer.addActionListener(e -> {
            InventoryCustomerGUI.main(null);
        });
        btnProduct.addActionListener(e -> {
            ProductApp.main(null);
        });
        btnStocks.addActionListener(e -> {
            StockMonitoringView.main(null);
        });
        btnSales.addActionListener(e -> {
            SalesApp.main(null);
        });


        // Add buttons to panel
        panel.add(btnProduct);
        panel.add(btnStocks);
        panel.add(btnCustomer);
        panel.add(btnSales);

        // Add panel to center
        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void openNewPage(String pageName) {
        JFrame page = new JFrame(pageName);
        page.setSize(400, 300);
        page.setLocationRelativeTo(null);
        JLabel label = new JLabel(pageName, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        page.add(label);
        page.setVisible(true);
    }

    public static void main(String[] args) {
        new InventoryFrontPage();
    }
}