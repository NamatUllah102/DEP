/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.usermanagementapp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Admin
 */
public class UserManagementApp extends JFrame {
    private JRadioButton createButton, readButton, updateButton, deleteButton;
    private JButton nextButton, closeButton;
    private ButtonGroup group;

    public UserManagementApp() {
        setTitle("User Management");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Which option do you want to select?");
        label.setBounds(10, 10, 200, 20);
        add(label, BorderLayout.NORTH);

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new GridLayout(4, 1));
        createButton = new JRadioButton("Create");
        readButton = new JRadioButton("Read");
        updateButton = new JRadioButton("Update");
        deleteButton = new JRadioButton("Delete");
        group = new ButtonGroup();
        group.add(createButton);
        group.add(readButton);
        group.add(updateButton);
        group.add(deleteButton);
        radioPanel.add(createButton);
        radioPanel.add(readButton);
        radioPanel.add(updateButton);
        radioPanel.add(deleteButton);
        add(radioPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        closeButton = new JButton("Close");
        nextButton = new JButton("Next");
        buttonPanel.add(closeButton);
        buttonPanel.add(nextButton);
        add(buttonPanel, BorderLayout.SOUTH);

        closeButton.addActionListener(e -> System.exit(0));
        nextButton.addActionListener(e -> showNextFrame());

        setVisible(true);
    }

    private void showNextFrame() {
        if (createButton.isSelected()) {
            new CreateFrame();
        } else if (readButton.isSelected()) {
            new ReadFrame();
        } else if (updateButton.isSelected()) {
            new UpdateFrame();
        } else if (deleteButton.isSelected()) {
            new DeleteFrame();
        } else {
            JOptionPane.showMessageDialog(this, "Please select an option.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UserManagementApp::new);
    }

    // Create Frame
    class CreateFrame extends JFrame {
        private JTextField usernameField, fatherNameField, dobField, passwordField;
        private JButton prevButton, confirmButton;

        public CreateFrame() {
            setTitle("Create User");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new GridLayout(6, 2));

            add(new JLabel("Username:"));
            usernameField = new JTextField();
            add(usernameField);

            add(new JLabel("Father Name:"));
            fatherNameField = new JTextField();
            add(fatherNameField);

            add(new JLabel("Date of Birth:"));
            dobField = new JTextField();
            add(dobField);

            add(new JLabel("Password:"));
            passwordField = new JTextField();
            add(passwordField);

            prevButton = new JButton("Prev");
            confirmButton = new JButton("Confirm");

            add(prevButton);
            add(confirmButton);

            prevButton.addActionListener(e -> {
                dispose();
                new UserManagementApp();
            });

            confirmButton.addActionListener(e -> {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_data.txt", true))) {
                    writer.write(usernameField.getText() + "," + fatherNameField.getText() + "," + dobField.getText() + "," + passwordField.getText());
                    writer.newLine();
                    writer.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                dispose();
                new UserManagementApp();
            });

            setVisible(true);
        }
    }

    // Read Frame
    class ReadFrame extends JFrame {
        private JTextField passwordField;
        private JButton prevButton, readButton;

        public ReadFrame() {
            setTitle("Read User");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new GridLayout(3, 2));

            add(new JLabel("Password:"));
            passwordField = new JTextField();
            add(passwordField);

            prevButton = new JButton("Prev");
            readButton = new JButton("Read");

            add(prevButton);
            add(readButton);

            prevButton.addActionListener(e -> {
                dispose();
                new UserManagementApp();
            });

            readButton.addActionListener(e -> {
                String inputPassword = passwordField.getText();
                try (BufferedReader reader = new BufferedReader(new FileReader("user_data.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts[3].equals(inputPassword)) {
                            JOptionPane.showMessageDialog(this, "User Info:\nUsername: " + parts[0] + "\nFather Name: " + parts[1] + "\nDate of Birth: " + parts[2]);
                            return;
                        }
                    }
                    JOptionPane.showMessageDialog(this, "Password does not match.");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            setVisible(true);
        }
    }

    // Update Frame
    class UpdateFrame extends JFrame {
        private JRadioButton updateUsernameButton, updateDobButton, updatePasswordButton;
        private JButton prevButton, nextButton;
        private ButtonGroup updateGroup;

        public UpdateFrame() {
            setTitle("Update User");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new GridLayout(5, 1));

            updateUsernameButton = new JRadioButton("Update Username");
            updateDobButton = new JRadioButton("Update Date of Birth");
            updatePasswordButton = new JRadioButton("Update Password");
            updateGroup = new ButtonGroup();
            updateGroup.add(updateUsernameButton);
            updateGroup.add(updateDobButton);
            updateGroup.add(updatePasswordButton);

            add(updateUsernameButton);
            add(updateDobButton);
            add(updatePasswordButton);

            prevButton = new JButton("Prev");
            nextButton = new JButton("Next");

            add(prevButton);
            add(nextButton);

            prevButton.addActionListener(e -> {
                dispose();
                new UserManagementApp();
            });

            nextButton.addActionListener(e -> {
                String selectedOption = getSelectedOption();
                if (selectedOption != null) {
                    new UpdateDetailsFrame(selectedOption);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Please select an update option.");
                }
            });

            setVisible(true);
        }

        private String getSelectedOption() {
            if (updateUsernameButton.isSelected()) return "username";
            if (updateDobButton.isSelected()) return "dob";
            if (updatePasswordButton.isSelected()) return "password";
            return null;
        }
    }

    // Update Details Frame
    class UpdateDetailsFrame extends JFrame {
        private JTextField passwordField, newValueField;
        private JButton prevButton, confirmButton;
        private String updateType;

        public UpdateDetailsFrame(String updateType) {
            this.updateType = updateType;
            setTitle("Update " + updateType);
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new GridLayout(3, 2));

            add(new JLabel("Password:"));
            passwordField = new JTextField();
            add(passwordField);

            add(new JLabel("New " + updateType + ":"));
            newValueField = new JTextField();
            add(newValueField);

            prevButton = new JButton("Prev");
            confirmButton = new JButton("Confirm");

            add(prevButton);
            add(confirmButton);

            prevButton.addActionListener(e -> {
                dispose();
                new UpdateFrame();
            });

            confirmButton.addActionListener(e -> {
                String inputPassword = passwordField.getText();
                String newValue = newValueField.getText();
                List<String> updatedLines = new ArrayList<>();
                boolean passwordMatched = false;

                try (BufferedReader reader = new BufferedReader(new FileReader("user_data.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts[3].equals(inputPassword)) {
                            passwordMatched = true;
                            if (updateType.equals("username")) {
                                updatedLines.add(newValue + "," + parts[1] + "," + parts[2] + "," + parts[3]);
                            } else if (updateType.equals("dob")) {
                                updatedLines.add(parts[0] + "," + parts[1] + "," + newValue + "," + parts[3]);
                            } else if (updateType.equals("password")) {
                                updatedLines.add(parts[0] + "," + parts[1] + "," + parts[2] + "," + newValue);
                            }
                        } else {
                            updatedLines.add(line);
                        }
                    }

                    if (passwordMatched) {
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_data.txt"))) {
                            for (String updatedLine : updatedLines) {
                                writer.write(updatedLine);
                                writer.newLine();
                            }
                        }
                        JOptionPane.showMessageDialog(this, "User information updated.");
                        dispose();
                        new UserManagementApp();
                    } else {
                        JOptionPane.showMessageDialog(this, "Password does not match.");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            setVisible(true);
        }
    }

    // Delete Frame
    class DeleteFrame extends JFrame {
        private JTextField passwordField;
        private JButton prevButton, confirmButton;

        public DeleteFrame() {
            setTitle("Delete User");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new GridLayout(3, 2));

            add(new JLabel("Password:"));
            passwordField = new JTextField();
            add(passwordField);

            prevButton = new JButton("Prev");
            confirmButton = new JButton("Confirm");

            add(prevButton);
            add(confirmButton);

            prevButton.addActionListener(e -> {
                dispose();
                new UserManagementApp();
            });

            confirmButton.addActionListener(e -> {
                String inputPassword = passwordField.getText();
                List<String> remainingLines = new ArrayList<>();
                boolean passwordMatched = false;

                try (BufferedReader reader = new BufferedReader(new FileReader("user_data.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts[3].equals(inputPassword)) {
                            passwordMatched = true;
                        } else {
                            remainingLines.add(line);
                        }
                    }

                    if (passwordMatched) {
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_data.txt"))) {
                            for (String remainingLine : remainingLines) {
                                writer.write(remainingLine);
                                writer.newLine();
                            }
                        }
                        JOptionPane.showMessageDialog(this, "User information deleted.");
                        dispose();
                        new UserManagementApp();
                    } else {
                        JOptionPane.showMessageDialog(this, "Password does not match.");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            setVisible(true);
        }
    }
}
