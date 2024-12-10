import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class HouseRentingSystemGUI {
    private static ArrayList<House> houses = new ArrayList<>();
    private static ArrayList<User> users = new ArrayList<>();

    public static void main(String[] args) {
        try {
            houses = FileHandler.loadHouses();
            users = UserHandler.loadUsers();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error loading data.");
        }

        showLoginMenu();
    }


    private static void addHouse() {
        JTextField houseIdField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField rentField = new JTextField();
        JTextField mobileField = new JTextField();
        JTextField specField = new JTextField();
        JTextField reviewField = new JTextField();

        Object[] message = {
                "House ID:", houseIdField,
                "Address:", addressField,
                "Rent:", rentField,
                "Mobile Number:", mobileField,
                "Specifications:", specField,
                "Review:", reviewField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add House", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String houseId = houseIdField.getText();
            String address = addressField.getText();
            double rent;
            String mobile = mobileField.getText();
            String specifications = specField.getText();
            String review = reviewField.getText();

            try {
                rent = Double.parseDouble(rentField.getText());
                if (rent <= 0) throw new NumberFormatException();


                if (mobile.isEmpty() || mobile.length() < 10 || mobile.length() > 13 || !mobile.matches("\\d+")) {
                    throw new IllegalArgumentException("Invalid mobile number! Please enter a valid 10-13 digit number.");
                }

                // Add house to list and save to file
                House newHouse = new House(houseId, address, rent, true, mobile, specifications, review);
                houses.add(newHouse);
                FileHandler.saveHouses(houses);

                JOptionPane.showMessageDialog(null, "House added successfully!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Rent must be a positive number.");
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving house data.");
            }
        }
    }



    private static void viewAllHouses() {
        StringBuilder allHouses = new StringBuilder();
        for (House house : houses) {
            allHouses.append(house).append("\n\n");
        }
        JOptionPane.showMessageDialog(null, allHouses.length() > 0 ? allHouses.toString() : "No houses available.");
    }



    private static void removeHouse() {
        String houseId = JOptionPane.showInputDialog("Enter House ID to remove:");
        boolean removed = houses.removeIf(house -> house.getHouseId().equals(houseId));
        if (removed) {
            try {
                FileHandler.saveHouses(houses);
                JOptionPane.showMessageDialog(null, "House removed successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving changes.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "House not found.");
        }
    }


    private static void searchHouses() {
        JTextField minRentField = new JTextField();
        JTextField maxRentField = new JTextField();
        JTextField locationField = new JTextField();

        Object[] message = {
                "Minimum Rent:", minRentField,
                "Maximum Rent:", maxRentField,
                "Location:", locationField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Search Houses", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                double minRent = Double.parseDouble(minRentField.getText());
                double maxRent = Double.parseDouble(maxRentField.getText());
                String location = locationField.getText().toLowerCase();

                StringBuilder results = new StringBuilder();
                for (House house : houses) {
                    if (house.isAvailable() && house.getRent() >= minRent && house.getRent() <= maxRent
                            && house.getAddress().toLowerCase().contains(location)) {
                        results.append(house).append("\n\n");
                    }
                }
                JOptionPane.showMessageDialog(null, results.length() > 0 ? results.toString() : "No houses found.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid rent range.");
            }
        }
    }



    private static void bookHouse() {
        String houseId = JOptionPane.showInputDialog("Enter House ID to book:");
        for (House house : houses) {
            if (house.getHouseId().equals(houseId) && house.isAvailable()) {
                house.setAvailable(false);
                try {
                    FileHandler.saveHouses(houses);
                    JOptionPane.showMessageDialog(null, "House booked successfully!\nDetails:\n" + house);
                    return;
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error saving booking.");
                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(null, "House not available or not found.");
    }



    // Login Menu
    private static void showLoginMenu() {
        JFrame loginFrame = new JFrame("House Renting System - Login");
        loginFrame.setSize(400, 300);
        loginFrame.setLayout(new GridLayout(3, 1));

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginFrame.add(new JLabel("Welcome! Please Login or Register.", JLabel.CENTER));
        loginFrame.add(loginButton);
        loginFrame.add(registerButton);

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> register());

        loginFrame.setVisible(true);
    }

    // Register a new user
    private static void register() {
        JTextField userIdField = new JTextField();
        JTextField nameField = new JTextField();
        String[] roles = {"Tenant", "Landlord"};
        JComboBox<String> roleBox = new JComboBox<>(roles);

        Object[] message = {
                "User ID:", userIdField,
                "Name:", nameField,
                "Role:", roleBox
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Register", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String userId = userIdField.getText();
            String name = nameField.getText();
            String role = (String) roleBox.getSelectedItem();

            try {
                if (UserHandler.findUserById(userId, users) != null) {
                    throw new IllegalArgumentException("User ID already exists!");
                }

                if (userId.isEmpty() || name.isEmpty()) {
                    throw new IllegalArgumentException("Fields cannot be empty!");
                }

                User newUser = new User(userId, name, role);
                users.add(newUser);
                UserHandler.saveUsers(users);

                JOptionPane.showMessageDialog(null, "Registration successful!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving user data.");
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    // Login an existing user
    private static void login() {
        String userId = JOptionPane.showInputDialog("Enter User ID:");
        User user = UserHandler.findUserById(userId, users);

        if (user != null) {
            JOptionPane.showMessageDialog(null, "Welcome, " + user.getName() + "!");
            if (user.getRole().equals("Landlord")) {
                showLandlordMenu();
            } else {
                showTenantMenu();
            }
        } else {
            JOptionPane.showMessageDialog(null, "User not found. Please register.");
        }
    }

    // Landlord Menu
    private static void showLandlordMenu() {
        JFrame landlordFrame = new JFrame("Landlord Menu");
        landlordFrame.setSize(400, 300);
        landlordFrame.setLayout(new GridLayout(3, 1));

        JButton addHouseButton = new JButton("Add House");
        JButton viewHousesButton = new JButton("View All Houses");
        JButton removeHouseButton = new JButton("Remove House");

        landlordFrame.add(addHouseButton);
        landlordFrame.add(viewHousesButton);
        landlordFrame.add(removeHouseButton);

        addHouseButton.addActionListener(e -> addHouse());
        viewHousesButton.addActionListener(e -> viewAllHouses());
        removeHouseButton.addActionListener(e -> removeHouse());

        landlordFrame.setVisible(true);
    }

    // Tenant Menu
    private static void showTenantMenu() {
        JFrame tenantFrame = new JFrame("Tenant Menu");
        tenantFrame.setSize(400, 300);
        tenantFrame.setLayout(new GridLayout(2, 1));

        JButton searchHousesButton = new JButton("Search Houses");
        JButton bookHouseButton = new JButton("Book House");

        tenantFrame.add(searchHousesButton);
        tenantFrame.add(bookHouseButton);

        searchHousesButton.addActionListener(e -> searchHouses());
        bookHouseButton.addActionListener(e -> bookHouse());

        tenantFrame.setVisible(true);
    }

    // Remaining methods (addHouse, viewAllHouses, removeHouse, searchHouses, bookHouse)
    // are implemented the same as in the previous version.
}
