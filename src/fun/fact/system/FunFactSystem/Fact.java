/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fun.fact.system.FunFactSystem;

import javafx.beans.property.*;

/**
 * FXML Controller class
 *
 * @author Fabiha
 */
public class Fact {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty email;
    private final SimpleStringProperty title;
    private final SimpleStringProperty description;

    public Fact(int id, String email, String title, String description) {
        this.id = new SimpleIntegerProperty(id);
        this.email = new SimpleStringProperty(email);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
    }

    public int getId() { return id.get(); }
    public String getEmail() { return email.get(); }
    public String getTitle() { return title.get(); }
    public String getDescription() { return description.get(); }
}