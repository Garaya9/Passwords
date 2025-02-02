package application;

/*
 * Class: CMSC204
 * Platform/compiler: Eclipse Java SE Development Kit (JDK) [21]
 * Student Name: G.Araya
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.text.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class PasswordMain extends BorderPane
{
    // Declare UI components (labels, text fields, buttons, etc.)
    private Label passwordLabel, passwordALabel, instruction1Label, instruction2Label, instruction3Label,
            instruction4Label, instruction5Label, instruction6Label;
    private TextField passwordText, passwordAText;
    private Button checkPwdButton, exitButton, checkPwdsInFileButton;
    DecimalFormat format = new DecimalFormat("#0.000");
    private Alert alert = new Alert(AlertType.INFORMATION); // Alert for showing messages to the user
    PasswordCheckerUtility pwdChecker; // Utility to check password validity

    public PasswordMain()
    {
        // Create VBox to hold instructions
        VBox subpanel = new VBox();
        instruction1Label = new Label("Use the following rules when creating your passwords:");
        instruction2Label = new Label("\t1.  Length must be greater than 6; a strong password will contain at least 10 characters");
        instruction3Label = new Label("\t2.  Must contain at least one upper case alpha character");
        instruction4Label = new Label("\t3.  Must contain at least one lower case alpha character");
        instruction5Label = new Label("\t4.  Must contain at least one numeric character");
        instruction6Label = new Label("\t5.  May not have more than 2 of the same character in sequence");

        // Add margins to instructions for spacing
        VBox.setMargin(instruction1Label, new Insets(2,10,2,10));
        VBox.setMargin(instruction2Label, new Insets(2,10,2,10));
        VBox.setMargin(instruction3Label, new Insets(2,10,2,10));
        VBox.setMargin(instruction4Label, new Insets(2,10,2,10));
        VBox.setMargin(instruction5Label, new Insets(2,10,2,10));
        VBox.setMargin(instruction6Label, new Insets(2,10,2,10));

        // Align and add instructions to the VBox
        subpanel.setAlignment(Pos.CENTER_LEFT);
        subpanel.getChildren().addAll(instruction1Label, instruction2Label, instruction3Label,
                instruction4Label, instruction5Label, instruction6Label);

        // Create first section for password input
        HBox subpanel1a = new HBox();
        passwordLabel = new Label ("Password");
        passwordText = new TextField(); // TextField for password input (used instead of PasswordField for easier debugging)
        HBox.setMargin(passwordLabel, new Insets(10,10,10,10));
        HBox.setMargin(passwordText, new Insets(10,10,10,10));
        subpanel1a.setAlignment(Pos.CENTER);
        subpanel1a.getChildren().addAll(passwordLabel, passwordText);

        // Create second section for password confirmation input
        HBox subpanel1b = new HBox();
        passwordALabel = new Label ("Re-type\nPassword");
        passwordAText = new TextField(); // TextField for re-typing password
        HBox.setMargin(passwordALabel, new Insets(10,10,10,10));
        HBox.setMargin(passwordAText, new Insets(10,10,10,10));
        subpanel1b.setAlignment(Pos.CENTER);
        subpanel1b.getChildren().addAll(passwordALabel, passwordAText);

        // Create main password entry panel with the two sections
        VBox subpanel1 = new VBox();
        VBox.setMargin(subpanel1a, new Insets(10,10,10,10));
        VBox.setMargin(subpanel1b, new Insets(10,10,10,10));
        subpanel1.setAlignment(Pos.CENTER);
        subpanel1.getChildren().addAll(subpanel1a, subpanel1b);

        // Create button for checking passwords from a file
        checkPwdsInFileButton = new Button("Check Passwords in _File");
        checkPwdsInFileButton.setMnemonicParsing(true); // Enable mnemonic (Alt + F)
        checkPwdsInFileButton.setTooltip(new Tooltip("Select to read passwords from a file"));
        // Action event for checking passwords in file
        checkPwdsInFileButton.setOnAction(
                event -> {
                    try {
                        readFile(); // Calls method to read passwords from the file
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        // Create button for checking password entered by the user
        checkPwdButton = new Button ("Check _Password");
        checkPwdButton.setMnemonicParsing(true); // Enable mnemonic (Alt + P)
        checkPwdButton.setTooltip(new Tooltip("Select to check a password."));
        // Action event for checking password
        checkPwdButton.setOnAction(
                event -> {
                    checkPassword(); // Calls method to validate password
                });

        // Create button for exiting the application
        exitButton = new Button("E_xit");
        exitButton.setMnemonicParsing(true);  // Enable mnemonic (Alt + X)
        exitButton.setTooltip(new Tooltip("Select to close the application"));
        // Action event for closing the application
        exitButton.setOnAction(
                event -> {
                    Platform.exit(); // Closes JavaFX application
                    System.exit(0); // Terminates the program
                }
        );

        // Create a button panel and add buttons to it
        HBox buttonPanel = new HBox();
        HBox.setMargin(checkPwdButton, new Insets(10,10,10,10));
        HBox.setMargin(checkPwdsInFileButton, new Insets(10,10,10,10));
        HBox.setMargin(exitButton, new Insets(10,10,10,10));
        buttonPanel.setAlignment(Pos.CENTER);
        buttonPanel.getChildren().addAll(checkPwdButton, checkPwdsInFileButton, exitButton);

        // Set the layout of the UI components
        setTop(subpanel);  // Set instruction panel at the top
        setCenter(subpanel1);  // Set password entry panel at the center
        setBottom(buttonPanel);  // Set button panel at the bottom
    }

    // Method to check if the password is valid and matches the retyped password
    public void checkPassword() {
        String passwordString = passwordText.getText();
        String passwordAString = passwordAText.getText();
        try {
            if (!PasswordCheckerUtility.comparePasswordsWithReturn(passwordString, passwordAString)) {
                throw new UnmatchedException(passwordAString); // If passwords don't match, throw exception
            }

            // Check if password is valid
            if (PasswordCheckerUtility.isValidPassword(passwordString)) {
                // Check for weak password if it's valid and has length between 6-9
                if (passwordString.length() >= 6 && passwordString.length() <= 9) {
                    alert.setContentText("Password is OK but weak (fewer than 10 characters).");
                    alert.showAndWait();
                } else {
                    alert.setContentText("Password is valid.");
                    alert.showAndWait();
                }
            } else {
                alert.setContentText("Password is Not valid.");
                alert.showAndWait();
            }
        } catch (UnmatchedException ex) {
            alert.setContentText(ex.getMessage()); // Show message if passwords do not match
            alert.showAndWait();
        } catch (Exception ex) {
            alert.setContentText(ex.getMessage()); // Show error message if an exception occurs
            alert.showAndWait();
        }
    }

    // Method to read passwords from a selected file and check their validity
    public void readFile() {
        FileChooser chooser = new FileChooser();
        Scanner input;
        File selectedFile = chooser.showOpenDialog(null); // Open file chooser for selecting a file
        String results = "", title = "";
        if (selectedFile != null) {
            ArrayList<String> passwords = new ArrayList<String>();
            try {
                input = new Scanner(selectedFile);
                // Read passwords from the file and add to the list
                while (input.hasNext()) {
                    passwords.add(input.next());
                }
                // Check for invalid passwords
                ArrayList<String> invalidPassword = PasswordCheckerUtility.getInvalidPasswords(passwords);
                if (invalidPassword.isEmpty()) {
                    results = "All Passwords are valid!";
                    title = "Passwords";
                } else {
                    results = "Invalid Passwords\n";
                    title = "Invalid Passwords";
                }
                // Display the invalid passwords (if any)
                for (String passwordString : invalidPassword)
                    results += passwordString + "\n";
                JOptionPane.showMessageDialog(null, results, title, JOptionPane.PLAIN_MESSAGE);
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, ex.toString(), "File Error", JOptionPane.PLAIN_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
