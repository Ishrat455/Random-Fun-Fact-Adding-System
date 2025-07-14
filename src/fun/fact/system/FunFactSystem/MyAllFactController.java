/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fun.fact.system.FunFactSystem;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Fabiha
 */
public class MyAllFactController implements Initializable {

    @FXML private TableView<Fact> factTable;
    @FXML private TableColumn<Fact, Integer> idColumn;
    @FXML private TableColumn<Fact, String> titleColumn;
    @FXML private TableColumn<Fact, String> descColumn;
    @FXML private TableColumn<Fact, Void> actionColumn;

    private ObservableList<Fact> factList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Session session = new Session();
        String email = session.getEmail();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        loadFacts(email);
        addActionButtons();
    }

    private void loadFacts(String email) {
        factList.clear(); // Clear old data
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fun_fact_system", "root", "");
             PreparedStatement ps = con.prepareStatement("SELECT * FROM fact WHERE Email = ?")) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                factList.add(new Fact(
                    rs.getInt("id"),
                    rs.getString("Email"),
                    rs.getString("Title"),
                    rs.getString("Description")
                ));
            }

            factTable.setItems(factList);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void addActionButtons() {
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button delBtn = new Button("Delete");
            private final HBox pane = new HBox(5, editBtn, delBtn);

            {
                editBtn.setOnAction(e -> {
                    Fact fact = getTableView().getItems().get(getIndex());
                    editFact(fact);
                });

                delBtn.setOnAction(e -> {
                    Fact fact = getTableView().getItems().get(getIndex());
                    deleteFact(fact);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void deleteFact(Fact fact) {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fun_fact_system", "root", "");
             PreparedStatement ps = con.prepareStatement("DELETE FROM fact WHERE id = ?")) {

            ps.setInt(1, fact.getId());
            ps.executeUpdate();

            factList.remove(fact);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void editFact(Fact fact) {
        Dialog<Fact> dialog = new Dialog<>();
        dialog.setTitle("Edit Fact");
        dialog.setHeaderText("Update the title and description");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        TextField titleField = new TextField(fact.getTitle());
        TextArea descField = new TextArea(fact.getDescription());
        descField.setWrapText(true);
        descField.setPrefRowCount(4);

        VBox content = new VBox(10, new Label("Title:"), titleField, new Label("Description:"), descField);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                updateFactInDatabase(fact.getId(), titleField.getText(), descField.getText());

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Fact updated successfully!");
                successAlert.showAndWait();

                loadFacts(new Session().getEmail()); // reload updated list
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void updateFactInDatabase(int id, String newTitle, String newDesc) {
        String sql = "UPDATE fact SET Title = ?, Description = ? WHERE id = ?";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fun_fact_system", "root", "");
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newTitle);
            ps.setString(2, newDesc);
            ps.setInt(3, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
