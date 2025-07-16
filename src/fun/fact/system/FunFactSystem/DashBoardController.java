/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fun.fact.system.FunFactSystem;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Fabiha
 */
public class DashBoardController implements Initializable {

    @FXML
    private TextField title;
    @FXML
    private TextArea Content;
    @FXML
    private Button add;
    @FXML
    private Hyperlink Logobut;
    @FXML
    private Hyperlink YourFunFact;
    @FXML
    private Hyperlink PublicFunFact;

    private Session session;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        session = new Session();
        String email = session.getEmail();

        // Add button action to save fun fact
        add.setOnAction(event -> saveFunFact());

        // YourFunFact hyperlink action to open new window
        YourFunFact.setOnAction(event -> openYourFunFactsWindow());

        // Logobut hyperlink action to open login window
        Logobut.setOnAction(event -> openLoginWindow());
        PublicFunFact.setOnAction(event -> openPublicFunFactWindow());
        
    }

    private void saveFunFact() {
        String email = session.getEmail();
        String factTitle = title.getText();
        String factContent = Content.getText();

        // Validate inputs
        if (email == null || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Session email is missing!");
            return;
        }
        if (factTitle.isEmpty() || factContent.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Title or Content cannot be empty!");
            return;
        }

        String sql = "INSERT INTO fact (Email, Title, Description) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, factTitle);
            pstmt.setString(3, factContent);
            pstmt.executeUpdate();
            showAlert(Alert.AlertType.ERROR, "Success", "Fun fact saved successfully!");
            title.clear();
            Content.clear();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save fun fact: " + e.getMessage());
        }
    }

    private void openYourFunFactsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MyAllFact.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("My Fun Facts");
            stage.show();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error opening Your Fun Facts window: " + e.getMessage());
        }
    }
    
    
    private void openPublicFunFactWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PublicFunFact.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Public Fun Fact");
            stage.show();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error opening Public Fun Fact window: " + e.getMessage());
        }
    }

    private void openLoginWindow() {
        try {
            // Load SignUp.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            
            // Get the current stage and set the new scene
            Stage stage = (Stage) Logobut.getScene().getWindow();
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

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/fun_fact_system?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found: " + e.getMessage());
        }
        return DriverManager.getConnection(url, user, password);
    }

    private void showAlert(AlertType ERROR, String title, String message) {
        Alert alert = new Alert(title.equals("Error") ? AlertType.ERROR : AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}