package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import tn.edu.esprit.entities.Reclamation;
import tn.edu.esprit.services.ServiceReclamation;

import java.time.LocalDateTime;

public class ReclamationForm {

    private ServiceReclamation service = new ServiceReclamation(); // Service pour la DB

    public void start(Stage stage) {

        BorderPane root = new BorderPane();

        // ===== HEADER =====
        HBox header = new HBox(10);
        header.setPadding(new Insets(10));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #014B4E;");

        Label logo = new Label("Khademni");
        logo.setTextFill(Color.WHITE);
        logo.setFont(Font.font(18));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnRetour = new Button("← Retour");
        btnRetour.setStyle("-fx-background-color: WHITE; -fx-text-fill: BLACK; -fx-font-weight: bold;");
        btnRetour.setOnAction(e -> {
            try {
                new MainApp().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        header.getChildren().addAll(logo, spacer, btnRetour);

        // ===== FORMULAIRE =====
        VBox formContent = new VBox(15);
        formContent.setAlignment(Pos.TOP_LEFT);

        // Sujet
        Label lblSujet = new Label("Sujet :");
        lblSujet.setFont(Font.font("Verdana", 14));
        TextField sujet = new TextField();
        sujet.setPrefWidth(400);
        sujet.setMaxWidth(400);

        // Description
        Label lblDescription = new Label("Description :");
        lblDescription.setFont(Font.font("Verdana", 14));
        TextArea description = new TextArea();
        description.setPrefRowCount(5);
        description.setPrefWidth(400);
        description.setMaxWidth(400);

        Button envoyer = new Button("Envoyer");
        envoyer.setStyle(
                "-fx-background-color: #014B4E; " +
                "-fx-text-fill: WHITE; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 14px; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px;"
        );

        envoyer.setOnAction(e -> {
            String texteSujet = sujet.getText().trim();
            String texteDesc = description.getText().trim();

            // Validation
            if (texteSujet.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Le sujet est obligatoire !");
                alert.showAndWait();
                return;
            }

            if (texteDesc.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("La description est obligatoire !");
                alert.showAndWait();
                return;
            }

            // Création de l'objet Reclamation
            Reclamation rec = new Reclamation();
            rec.setSujet(texteSujet);
            rec.setDescription(texteDesc);
            rec.setDateCreation(LocalDateTime.now());

            // === Enregistrement dans la BD ===
            service.ajouter(rec);

            // Confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Réclamation envoyée avec succès ✅");
            alert.showAndWait();

            // Nettoyer les champs
            sujet.clear();
            description.clear();
        });

        formContent.getChildren().addAll(
                lblSujet, sujet,
                lblDescription, description,
                envoyer
        );

        // Centrer le formulaire
        StackPane centerPane = new StackPane(formContent);
        centerPane.setPadding(new Insets(20));

        root.setTop(header);
        root.setCenter(centerPane);
        root.setStyle("-fx-background-color: #F2F0EF;");

        Scene scene = new Scene(root, 900, 500);
        stage.setScene(scene);
        stage.setTitle("Réclamation");
        stage.show();
    }
}