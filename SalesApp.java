import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class Sale {
    private String saleId, productId, customerName, date, paymentMode;
    private int quantity;
    private double price, discount;
    public Sale(String saleId, String productId, String customerName, String date, int quantity, double price, double discount, String paymentMode) {
        this.saleId = saleId;
        this.productId = productId;
        this.customerName = customerName;
        this.date = date;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.paymentMode = paymentMode;
    }
    public String getSaleId() { return saleId; }
    public String toString() {
        double total = quantity * price * (1 - discount / 100.0);
        return saleId + "\t" + productId + "\t" + customerName + "\t" + date + "\t" +
                quantity + "\t" + price + "\t" + discount + "\t" + paymentMode + "\t" + String.format("%.2f", total);
    }
}
class SaleException extends Exception {
    public SaleException(String message) { super(message); }
}
class SaleManager {
    private Sale[] sales = new Sale[30];
    private int count = 0;
    public void addSale(Sale s) throws SaleException {
        for (int i = 0; i < count; i++)
            if (sales[i].getSaleId().equals(s.getSaleId()))
                throw new SaleException("Sale ID must be unique!");
        if (count >= sales.length)
            throw new SaleException("Sales limit reached!");
        sales[count++] = s;
    }
    public Sale[] getAllSales() {
        Sale[] arr = new Sale[count];
        for (int i = 0; i < count; i++) arr[i] = sales[i];
        return arr;
    }
    public Sale searchSale(String id) throws SaleException {
        for (int i = 0; i < count; i++)
            if (sales[i].getSaleId().equals(id))
                return sales[i];
        throw new SaleException("Sale ID not found!");
    }
    public void deleteSale(String id) throws SaleException {
        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (sales[i].getSaleId().equals(id)) {
                for (int j = i; j < count - 1; j++)
                    sales[j] = sales[j + 1];
                sales[--count] = null;
                found = true;
                break;
            }
        }
        if (!found)
            throw new SaleException("Sale ID not found!");
    }
}
public class SalesApp extends JFrame {
    private JTextField txtSaleId = new JTextField(6), txtProductId = new JTextField(6),
            txtCustomer = new JTextField(10), txtDate = new JTextField(8),
            txtQuantity = new JTextField(4), txtPrice = new JTextField(8), txtDiscount = new JTextField(5);
    private JComboBox<String> cmbPaymentMode = new JComboBox<>(new String[]{"Online", "Offline"});
    private JTextArea output = new JTextArea(10, 70);
    private SaleManager manager = new SaleManager();
    public SalesApp() {
        setTitle("Sales Management - Student Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(8, 2));
        inputPanel.add(new JLabel("Sale ID:")); inputPanel.add(txtSaleId);
        inputPanel.add(new JLabel("Product ID:")); inputPanel.add(txtProductId);
        inputPanel.add(new JLabel("Customer Name:")); inputPanel.add(txtCustomer);
        inputPanel.add(new JLabel("Date (yyyy-mm-dd):")); inputPanel.add(txtDate);
        inputPanel.add(new JLabel("Quantity:")); inputPanel.add(txtQuantity);
        inputPanel.add(new JLabel("Price:")); inputPanel.add(txtPrice);
        inputPanel.add(new JLabel("Discount (%):")); inputPanel.add(txtDiscount);
        inputPanel.add(new JLabel("Payment Mode:")); inputPanel.add(cmbPaymentMode);
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add Sale");
        JButton showBtn = new JButton("Show All");
        JButton searchBtn = new JButton("Search");
        JButton deleteBtn = new JButton("Delete");
        JButton backButton = new JButton("Back to Home");
        backButton.addActionListener(e -> {
            dispose();
            new InventoryFrontPage();
        });
        buttonPanel.add(addBtn);
        buttonPanel.add(showBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(backButton);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(output), BorderLayout.SOUTH);
        output.setEditable(false);
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String saleId = txtSaleId.getText().trim();
                    String productId = txtProductId.getText().trim();
                    String customer = txtCustomer.getText().trim();
                    String date = txtDate.getText().trim();
                    String qStr = txtQuantity.getText().trim();
                    String pStr = txtPrice.getText().trim();
                    String dStr = txtDiscount.getText().trim();
                    String payment = (String) cmbPaymentMode.getSelectedItem();
                    if (saleId.isEmpty() || productId.isEmpty() || customer.isEmpty() ||
                            date.isEmpty() || qStr.isEmpty() || pStr.isEmpty() || dStr.isEmpty() || payment.isEmpty())
                        throw new SaleException("Please fill all fields!");
                    if (!saleId.matches("\\d+"))
                        throw new SaleException("Sale ID must be digits only!");
                    if (!customer.matches("^[a-zA-Z ]+$"))
                        throw new SaleException("Customer Name must be letters and spaces only!");
                    if (!date.matches("\\d{4}-\\d{2}-\\d{2}"))
                        throw new SaleException("Date must be yyyy-mm-dd (e.g. 2025-10-23)");
                    String[] parts = date.split("-");
                    int m = Integer.parseInt(parts[1]), d = Integer.parseInt(parts[2]);
                    if (m < 1 || m > 12 || d < 1 || d > 31)
                        throw new SaleException("Month 1-12, day 1-31");
                    int quantity = Integer.parseInt(qStr);
                    double price = Double.parseDouble(pStr);
                    double discount = Double.parseDouble(dStr);
                    if (quantity <= 0 || price <= 0)
                        throw new SaleException("Quantity/Price must be positive!");
                    if (discount < 0 || discount > 100)
                        throw new SaleException("Discount must be between 0 and 100!");
                    Sale s = new Sale(saleId, productId, customer, date, quantity, price, discount, payment);
                    manager.addSale(s);
                    JOptionPane.showMessageDialog(SalesApp.this, "Sale added!");
                    output.setText("Added:\n" + s.toString());
                    clearFields();
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(SalesApp.this, "Quantity, Price, Discount must be valid numbers", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (SaleException ex) {
                    JOptionPane.showMessageDialog(SalesApp.this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        showBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                output.setText("SaleID\tProdID\tCustomer\tDate\tQty\tPrice\tDiscount\tPayMode\tTotal\n");
                for (Sale s : manager.getAllSales())
                    output.append(s.toString() + "\n");
            }
        });
        searchBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String searchId = JOptionPane.showInputDialog(SalesApp.this, "Enter Sale ID to search:");
                    if (searchId == null) return;
                    Sale s = manager.searchSale(searchId.trim());
                    output.setText("Sale found:\n" + s.toString());
                } catch (SaleException ex) {
                    JOptionPane.showMessageDialog(SalesApp.this, ex.getMessage(), "Not Found", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        deleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String delId = JOptionPane.showInputDialog(SalesApp.this, "Enter Sale ID to delete:");
                    if (delId == null) return;
                    manager.deleteSale(delId.trim());
                    JOptionPane.showMessageDialog(SalesApp.this, "Sale deleted!");
                    output.setText("");
                } catch (SaleException ex) {
                    JOptionPane.showMessageDialog(SalesApp.this, ex.getMessage(), "Not Found", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        setSize(950, 470);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void clearFields() {
        txtSaleId.setText("");
        txtProductId.setText("");
        txtCustomer.setText("");
        txtDate.setText("");
        txtQuantity.setText("");
        txtPrice.setText("");
        txtDiscount.setText("");
        cmbPaymentMode.setSelectedIndex(0);
    }
    public static void main(String[] args) {
        new SalesApp();
    }
}
