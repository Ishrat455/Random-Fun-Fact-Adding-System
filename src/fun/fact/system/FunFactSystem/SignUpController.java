/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package fun.fact.system.FunFactSystem;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

/**
 *
 * @author Fabiha
 */
public class SignUpController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private TextField username;
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private TextField cpassword;
    @FXML
    private Hyperlink login;
    @FXML
    private Button signupB;
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/fun_fact_system";
    private static final String DB_USER = "root"; 
    private static final String DB_PASS = "";    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        signupB.setOnAction(this::handleSignUp);
        login.setOnAction(this::handleLoginLink);
    }    
    
    @FXML
    private void handleSignUp(ActionEvent event) {
        String userName = username.getText().trim();
        String userEmail = email.getText().trim();
        String userPass = password.getText();
        String confirmPass = cpassword.getText();
        
        // Validation
        if (userName.isEmpty() || userEmail.isEmpty() || userPass.isEmpty() || confirmPass.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill in all fields");
            return;
        }
        
        if (!userPass.equals(confirmPass)) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Passwords do not match");
            return;
        }
        
        if (!userEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Invalid email format");
            return;
        }
        
        // Database insertion
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO users (username, email, password) VALUES (?, ?, ?)")) {
            
            stmt.setString(1, userName);
            stmt.setString(2, userEmail);
            stmt.setString(3, userPass);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Signup successful!");
                clearFields();
                navigateToLogin();
            }
            
        } catch (SQLException e) {
            if (e.getSQLState().equals("23000")) { // Duplicate entry error
                showAlert(Alert.AlertType.ERROR, "Database Error", 
                    "Username or email already exists");
            } else {
                showAlert(Alert.AlertType.ERROR, "Database Error", 
                    "Error occurred: " + e.getMessage());
            }
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleLoginLink(ActionEvent event) {
        navigateToLogin();
    }
    
    private void navigateToLogin() {
        try {
            // Load Login.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            
            // Get the current stage and set the new scene
            Stage stage = (Stage) signupB.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
            
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                "Failed to load login page: " + e.getMessage());
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
    
    private void clearFields() {
        username.setText("");
        email.setText("");
        password.setText("");
        cpassword.setText("");
    }
}