import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
interface CustomerOperations {
    void addCustomer(Customer c);
    void deleteCustomer(int id);
    List<Customer> searchCustomer(String keyword);
}
class InvalidCustomerException extends Exception {
    public InvalidCustomerException(String message) {
        super(message);
    }
}
abstract class AbstractCustomerManager {
    protected List<Customer> customerList;
    public AbstractCustomerManager() {
        customerList = new ArrayList<>();
    }
    public abstract void displayAll();
}
class Customer {
    private int id;
    private String name;
    private String phone;
    private String membership;
    public Customer(int id, String name, String phone, String membership) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.membership = membership;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getMembership() { return membership; }
}
public class InventoryCustomerGUI extends AbstractCustomerManager implements CustomerOperations {
    private JFrame frame = new JFrame("Customer Management");
    private JTable table =new JTable();
    private DefaultTableModel model;
    private JTextField searchField=new JTextField(70);
    private JTextField nameField =new JTextField(70);
    private JTextField idField =new JTextField(70);
    private JTextField phoneField=new JTextField(70);
    private JComboBox<String> membershipBox;
    private JButton addButton=new JButton("Add Customer");
    private JButton deleteButton =new JButton("Delete Customer");
    private JButton searchButton =new JButton("Search");
    private JButton resetButton= new JButton("Reset");
    private TableRowSorter<DefaultTableModel> sorter;
    public InventoryCustomerGUI() {
        customerList.add(new Customer(101, "Alice", "9876543210", "Premium"));
        customerList.add(new Customer(102, "Bob", "8695471230", "Regular"));
        customerList.add(new Customer(103, "Charlie", "9123456780", "VIP"));
        frame = new JFrame("Customer Details - Inventory Management");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchPanel.add(new JLabel("Search by Name or ID:"));
        frame.add(searchPanel, BorderLayout.NORTH);
        String[] columns = {"Customer ID", "Customer Name", "Phone Number", "Membership"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);
        displayAll();
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        idField = new JTextField();
        nameField = new JTextField();
        phoneField = new JTextField();
        membershipBox = new JComboBox<>(new String[]{"Regular", "Premium", "VIP"});
        inputPanel.add(new JLabel("Customer ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Membership:"));
        inputPanel.add(membershipBox);
        bottomPanel.add(inputPanel);
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Customer");
        deleteButton = new JButton("Delete Customer");
        JButton backButton = new JButton("Back to Home");
        backButton.addActionListener(e -> {
            frame.dispose();
            new InventoryFrontPage();
        });
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        bottomPanel.add(buttonPanel);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        addButton.addActionListener(e -> handleAdd());
        deleteButton.addActionListener(e -> handleDelete());
        searchButton.addActionListener(e -> searchCustomer(searchField.getText()));
        resetButton.addActionListener(e -> resetFilter());
        frame.setVisible(true);
    }
    public void displayAll() {
        model.setRowCount(0);
        for (Customer c : customerList) {
            model.addRow(new Object[]{c.getId(), c.getName(), c.getPhone(), c.getMembership()});
        }
    }
    @Override
    public void addCustomer(Customer c) {
        customerList.add(c);
        model.addRow(new Object[]{c.getId(), c.getName(), c.getPhone(), c.getMembership()});
    }
    public void deleteCustomer(int id) {
        customerList.removeIf(c -> c.getId() == id);
        displayAll();
    }
    private void handleAdd() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String membership = membershipBox.getSelectedItem().toString();
            if (name.isEmpty() || phone.isEmpty()) {
                throw new InvalidCustomerException("Name and Phone cannot be empty!");
            }
            if (!phone.matches("\\d{10}")) {
                throw new InvalidCustomerException("Invalid phone number format!");
            }
            for (Customer c : customerList) {
                if (c.getId() == id) throw new InvalidCustomerException("Duplicate ID found!");
            }
            addCustomer(new Customer(id, name, phone, membership));
            JOptionPane.showMessageDialog(frame, "Customer Added Successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Customer ID must be numeric!");
        } catch (InvalidCustomerException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage());
        }
    }
    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(frame, "Select a customer to delete!");
            return;
        }
        int modelRow = table.convertRowIndexToModel(row);
        int id = (int) model.getValueAt(modelRow, 0);
        deleteCustomer(id);
        JOptionPane.showMessageDialog(frame, "Customer Deleted Successfully!");
    }
    public List<Customer> searchCustomer(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            sorter.setRowFilter(null);
            return customerList;
        }
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
        return null;
    }
    private void resetFilter() {
        searchField.setText("");
        sorter.setRowFilter(null);
    }
    public static void main(String[] args) {
        new InventoryCustomerGUI();
    }
}
