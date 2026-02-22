package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import tn.edu.esprit.entities.AttributEnquete;
import tn.edu.esprit.entities.Enquete;
import tn.edu.esprit.entities.ReponseEnquete;
import tn.edu.esprit.services.ServiceEnquete;
import tn.edu.esprit.services.ServiceReponseEnquete;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReponseEnqueteForm {

    private final ServiceEnquete serviceEnquete = new ServiceEnquete();
    private final ServiceReponseEnquete serviceReponse = new ServiceReponseEnquete();
    private final Stage primaryStage;

    public ReponseEnqueteForm(Stage stage) {
        this.primaryStage = stage;
    }

    public void start() {

        BorderPane root = new BorderPane();

        // ===== HEADER =====
        HBox header = new HBox(10);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #014B4E;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label logo = new Label("Répondre aux enquêtes");
        logo.setTextFill(Color.WHITE);
        logo.setFont(Font.font("Verdana", 20));
        logo.setStyle("-fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnRetour = new Button("← Retour");
        btnRetour.setOnAction(e -> new EnquetesGestion(primaryStage).start());

        header.getChildren().addAll(logo, spacer, btnRetour);
        root.setTop(header);

        VBox centerBox = new VBox(15);
        centerBox.setPadding(new Insets(20));
        centerBox.setAlignment(Pos.TOP_CENTER);

        List<Enquete> enquetes = serviceEnquete.getEnquetesActives();

        if (enquetes.isEmpty()) {
            centerBox.getChildren().add(new Label("Aucune enquête active."));
        } else {
            centerBox.getChildren().add(new Label("Choisissez une enquête :"));

            VBox listBox = new VBox(10);

            for (Enquete e : enquetes) {
                Button btn = new Button(e.getSujet());
                btn.setPrefWidth(400);
                btn.setOnAction(ev -> showQuestionsForm(e));
                listBox.getChildren().add(btn);
            }

            centerBox.getChildren().add(listBox);
        }

        root.setCenter(centerBox);

        Scene scene = new Scene(root, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Répondre à une enquête");
        primaryStage.show();
    }

    private void showQuestionsForm(Enquete enquete) {

        BorderPane root = new BorderPane();

        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(20));

        Map<Integer, ChoiceBox<Integer>> notesControls = new HashMap<>();

        for (AttributEnquete attr : enquete.getAttributs()) {

            HBox row = new HBox(10);

            Label label = new Label(attr.getNom());
            label.setPrefWidth(300);

            ChoiceBox<Integer> choice = new ChoiceBox<>();
            choice.getItems().addAll(1, 2, 3, 4, 5);
            choice.setValue(3);

            notesControls.put(attr.getIdAttributEnquete(), choice);

            row.getChildren().addAll(label, choice);
            formBox.getChildren().add(row);
        }

        Button btnSubmit = new Button("Envoyer");
        btnSubmit.setOnAction(ev -> {

            ReponseEnquete r = new ReponseEnquete(enquete);

            for (var entry : notesControls.entrySet()) {
                r.ajouterNote(entry.getKey(), entry.getValue().getValue());
            }

            serviceReponse.ajouter(r);

            showAlert("Réponse enregistrée !");
            start();
        });

        Button btnRetour = new Button("← Retour");
        btnRetour.setOnAction(e -> start());

        formBox.getChildren().addAll(btnSubmit, btnRetour);
        root.setCenter(formBox);

        Scene scene = new Scene(root, 700, 500);
        primaryStage.setScene(scene);
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}