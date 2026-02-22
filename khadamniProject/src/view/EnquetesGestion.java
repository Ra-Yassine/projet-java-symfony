package view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import tn.edu.esprit.entities.Enquete;
import tn.edu.esprit.entities.StatutEnquete;
import tn.edu.esprit.services.ServiceEnquete;

public class EnquetesGestion {

    private final Stage stage;
    private final ServiceEnquete service = new ServiceEnquete();

    private TableView<Enquete> table;

    public EnquetesGestion(Stage stage) {
        this.stage = stage;
    }

    public void start() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        Label title = new Label("Gestion des Enquêtes");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // ================= TABLE =================
        table = new TableView<>();

        TableColumn<Enquete, String> colSujet = new TableColumn<>("Sujet");
        colSujet.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getSujet()));

        TableColumn<Enquete, String> colStatut = new TableColumn<>("Statut");
        colStatut.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getStatut().name()));

        table.getColumns().addAll(colSujet, colStatut);
        table.setPrefHeight(300);

        refreshTable();

        // ================= BUTTONS =================
        Button btnAjouter = new Button("Ajouter");
        Button btnModifier = new Button("Modifier");
        Button btnSupprimer = new Button("Supprimer");
        Button btnRepondre = new Button("Répondre");
        Button btnRetour = new Button("⬅ Retour");

        HBox buttons = new HBox(10, btnAjouter, btnModifier, btnSupprimer, btnRepondre, btnRetour);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10));

        // ================= ACTIONS =================
        btnAjouter.setOnAction(e -> {
            try {
                new EnqueteForm(stage).start(stage); // Ouvre le formulaire d'ajout
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnModifier.setOnAction(e -> modifierEnquete());
        btnSupprimer.setOnAction(e -> supprimerEnquete());
        btnRepondre.setOnAction(e ->
                new ReponseEnqueteForm(stage).start());

        btnRetour.setOnAction(e -> {
            try {
                new MainApp().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox center = new VBox(15, table, buttons);
        center.setAlignment(Pos.TOP_CENTER);

        root.setTop(title);
        root.setCenter(center);

        Scene scene = new Scene(root, 800, 500);
        stage.setScene(scene);
        stage.show();
    }

    // ================= LOGIQUE =================
    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(service.getAll()));
    }

    private void modifierEnquete() {
        Enquete selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Sélectionnez une enquête");
            return;
        }

        try {
            // On ouvre le formulaire EnqueteForm pour modification
            // Pour la modification, tu peux adapter EnqueteForm pour charger les données de l'enquête sélectionnée
            new EnqueteForm(stage).start(stage); 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void supprimerEnquete() {
        Enquete selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Sélectionnez une enquête");
            return;
        }

        service.supprimer(selected.getIdEnquete());
        refreshTable();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}