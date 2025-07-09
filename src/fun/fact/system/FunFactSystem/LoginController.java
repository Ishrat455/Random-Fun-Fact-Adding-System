/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fun.fact.system.FunFactSystem;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Hyperlink;

/**
 * FXML Controller class
 *
 * @author Fabiha
 */
public class LoginController implements Initializable {

    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginB;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/fun_fact_system";
    private static final String DB_USER = "root"; 
    private static final String DB_PASS = "";     
    @FXML
    private Hyperlink Signup;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loginB.setOnAction(this::handleLogin);
        Signup.setOnAction(this::handleSignupLink);
    }    
    
    private void handleLogin(ActionEvent event) {
        String userEmail = login.getText().trim();
        String userPass = password.getText();
        
        // Validation
        if (userEmail.isEmpty() || userPass.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill in all fields");
            return;
        }
        
        // Database authentication
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT password FROM users WHERE email = ?")) {
            
            stmt.setString(1, userEmail);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (userPass.equals(storedPassword)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Login successful!");
                    navigateToDashboard();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Error", "Incorrect password");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Error", "Email not found");
            }
            
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", 
                "Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleSignupLink(ActionEvent event) {
        try {
            // Load SignUp.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
            Parent root = loader.load();
            
            // Get the current stage and set the new scene
            Stage stage = (Stage) Signup.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Sign Up");
            stage.show();
            
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                "Failed to load signup page: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void navigateToDashboard() {
        try {
            // Load DashBoard.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DashBoard.fxml"));
            Parent root = loader.load();
            
            // Get the current stage and set the new scene
            Stage stage = (Stage) loginB.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Dashboard");
            stage.show();
            
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                "Failed to load dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}