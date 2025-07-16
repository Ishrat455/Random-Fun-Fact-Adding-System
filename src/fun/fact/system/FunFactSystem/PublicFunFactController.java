/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fun.fact.system.FunFactSystem;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class PublicFunFactController implements Initializable {

    @FXML
    private VBox cardContainer;

    private final String DB_URL = "jdbc:mysql://localhost:3306/fun_fact_system";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadFunFacts();
    }

    private void loadFunFacts() {
        String query = "SELECT Title, Description FROM fact";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String title = rs.getString("Title");
                String description = rs.getString("Description");

                TitledPane factCard = new TitledPane();
                factCard.setText(title);

                Label descriptionLabel = new Label(description);
                descriptionLabel.setWrapText(true);
                descriptionLabel.setStyle("-fx-padding: 10; -fx-font-size: 14;");

                factCard.setContent(descriptionLabel);
                factCard.setExpanded(false); // collapsed by default

                factCard.setStyle("-fx-background-color: #f9f9f9; "
                        + "-fx-border-color: #ccc; -fx-border-radius: 8px; -fx-padding: 5 10 5 10;");

                cardContainer.getChildren().add(factCard);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
