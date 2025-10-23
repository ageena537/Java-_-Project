import javax.swing.*;
import java.awt.*;

// Model class
class Product {
    private String productId, productName, category;
    private double price;
    private int stock;
    public Product(String productId, String productName, String category, double price, int stock) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public String toString() {
        return productId + "\t" + productName + "\t" + category + "\t" + price + "\t" + stock;
    }
}
// Custom Exception
class ProductException extends Exception {
    public ProductException(String message) { super(message); }
}
// Controller class
class ProductManager {
    private Product[] products = new Product[20];
    private int count = 0;
    public void addProduct(Product p) throws ProductException {
        for (int i = 0; i < count; i++) {
            if (products[i].getProductId().equals(p.getProductId()))
                throw new ProductException("Product ID must be unique!");
        }
        if (count >= products.length)
            throw new ProductException("Product limit reached!");
        products[count++] = p;
    }
    public Product[] getAllProducts() {
        Product[] list = new Product[count];
        for (int i = 0; i < count; i++) list[i] = products[i];
        return list;
    }
    public Product searchProduct(String id) throws ProductException {
        for (int i = 0; i < count; i++) {
            if (products[i].getProductId().equals(id))
                return products[i];
        }
        throw new ProductException("Product not found!");
    }
    public void deleteProduct(String id) throws ProductException {
        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (products[i].getProductId().equals(id)) {
                for (int j = i; j < count - 1; j++) products[j] = products[j + 1];
                products[--count] = null;
                found = true;
                break;
            }
        }
        if (!found) throw new ProductException("Product not found!");
    }
}
// View class with GUI
public class ProductApp extends JFrame {
    private JTextField txtId = new JTextField(5);
    private JTextField txtName = new JTextField(10);
    private JComboBox<String> cmbCategory = new JComboBox<>(new String[]{
            "Electronics", "Cosmetics", "Groceries", "Cloths"
    });
    private JTextField txtPrice = new JTextField(6);
    private JTextField txtStock = new JTextField(4);
    private JTextArea output = new JTextArea(10, 40);
    private ProductManager manager = new ProductManager();
    public ProductApp() {
        setTitle("Simple Product Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Product ID:")); inputPanel.add(txtId);
        inputPanel.add(new JLabel("Product Name:")); inputPanel.add(txtName);
        inputPanel.add(new JLabel("Category:")); inputPanel.add(cmbCategory);
        inputPanel.add(new JLabel("Price:")); inputPanel.add(txtPrice);
        inputPanel.add(new JLabel("Stock:")); inputPanel.add(txtStock);
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add");
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
        // ADD BUTTON
        addBtn.addActionListener(e -> {
            try {
                String id = txtId.getText().trim();
                String name = txtName.getText().trim();
                String category = (String) cmbCategory.getSelectedItem();
                String priceStr = txtPrice.getText().trim();
                String stockStr = txtStock.getText().trim();
                if (id.isEmpty() || name.isEmpty() || category.isEmpty() ||
                        priceStr.isEmpty() || stockStr.isEmpty())
                    throw new ProductException("Please fill all fields!");
                if (!name.matches("^[a-zA-Z ]+$"))
                    throw new ProductException("Product Name can contain only letters and spaces!");
                if (!id.matches("\\d+") || Integer.parseInt(id) <= 0)
                    throw new ProductException("Product ID must be a positive integer!");
                double price = Double.parseDouble(priceStr);
                if (price <= 0)
                    throw new ProductException("Price must be greater than zero!");
                int stock = Integer.parseInt(stockStr);
                if (stock < 0)
                    throw new ProductException("Stock cannot be negative!");
                Product p = new Product(id, name, category, price, stock);
                manager.addProduct(p);
                JOptionPane.showMessageDialog(this, "Product added successfully!");
                output.setText("Added Product:\n" + p.toString());
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Price and Stock must be valid numbers", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (ProductException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        // SHOW ALL BUTTON
        showBtn.addActionListener(e -> {
            output.setText("All Products:\n");
            for (Product p : manager.getAllProducts())
                output.append(p.toString() + "\n");
        });
        // SEARCH BUTTON
        searchBtn.addActionListener(e -> {
            try {
                String searchId = JOptionPane.showInputDialog("Enter Product ID to search:");
                if (searchId == null) return;
                Product p = manager.searchProduct(searchId.trim());
                output.setText("Product found:\n" + p.toString());
            } catch (ProductException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Not Found", JOptionPane.ERROR_MESSAGE);
            }
        });
        // DELETE BUTTON
        deleteBtn.addActionListener(e -> {
            try {
                String delId = JOptionPane.showInputDialog("Enter Product ID to delete:");
                if (delId == null) return;
                manager.deleteProduct(delId.trim());
                JOptionPane.showMessageDialog(this, "Product deleted!");
                output.setText("");
            } catch (ProductException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Not Found", JOptionPane.ERROR_MESSAGE);
            }
        });
        setSize(550, 450);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String[] args) {
        new ProductApp();
    }
}