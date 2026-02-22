package view;

import javafx.application.Application;
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
import tn.edu.esprit.services.ServiceEnquete;

import java.util.ArrayList;
import java.util.List;

public class EnqueteForm extends Application {

    private Stage primaryStage;
    private ServiceEnquete service = new ServiceEnquete();

    // Constructeur pour passer le Stage depuis MainApp
    public EnqueteForm(Stage stage) {
        this.primaryStage = stage;
    }

    // Constructeur par défaut (nécessaire pour lancer seul)
    public EnqueteForm() {}

    @Override
    public void start(Stage stage) {
        if (primaryStage == null) primaryStage = stage;

        BorderPane root = new BorderPane();

        // ================= HEADER =================
        HBox header = new HBox(10);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #1F1B1B;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label logo = new Label("Khademni");
        logo.setTextFill(Color.WHITE);
        logo.setFont(Font.font("Verdana", 20));
        logo.setStyle("-fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnRetour = new Button("← Retour");
        btnRetour.setStyle("-fx-background-color: WHITE; -fx-text-fill: BLACK; -fx-font-weight: bold;");
        btnRetour.setOnAction(e -> {
            try {
                new MainApp().start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        header.getChildren().addAll(logo, spacer, btnRetour);

        // ================= FORMULAIRE =================
        VBox formBox = new VBox(12);
        formBox.setPadding(new Insets(30));
        formBox.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Ajouter une enquête");
        title.setFont(Font.font("Verdana", 18));
        title.setTextFill(Color.BLACK);

        // Sujet
        Label lblSujet = new Label("Sujet :");
        lblSujet.setTextFill(Color.BLACK);
        TextField tfSujet = new TextField();
        tfSujet.setPrefWidth(350);

        // Description
        Label lblDesc = new Label("Description :");
        lblDesc.setTextFill(Color.BLACK);
        TextArea taDesc = new TextArea();
        taDesc.setPrefRowCount(3);
        taDesc.setPrefWidth(350);

        // Anonyme
        CheckBox cbAnonyme = new CheckBox("Anonyme");
        cbAnonyme.setSelected(true);
        cbAnonyme.setTextFill(Color.BLACK);

        // ================= QUESTIONS =================
        Label lblQuestions = new Label("Questions :");
        lblQuestions.setTextFill(Color.BLACK);

        VBox questionsBox = new VBox(15);
        questionsBox.setPadding(new Insets(15));
        questionsBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-border-radius: 5;");

        Button btnAjouterQuestion = new Button("Ajouter une question");
        btnAjouterQuestion.setStyle(
                "-fx-background-color: WHITE; -fx-text-fill: BLACK; -fx-font-weight: bold;"
        );

        btnAjouterQuestion.setOnAction(ev -> {
            int questionNumber = questionsBox.getChildren().size() + 1;

            HBox questionRow = new HBox(10);
            questionRow.setAlignment(Pos.CENTER_LEFT);

            Label lblQ = new Label("Question " + questionNumber + " :");
            lblQ.setPrefWidth(100);
            TextField tfQuestion = new TextField();
            tfQuestion.setPrefWidth(300);
            tfQuestion.setPromptText("Entrez la question...");

            // Optionnel : bouton supprimer question
            /*
            Button btnSupprimer = new Button("❌");
            btnSupprimer.setStyle("-fx-background-color: transparent; -fx-text-fill: red;");
            btnSupprimer.setOnAction(ev2 -> questionsBox.getChildren().remove(questionRow));
            questionRow.getChildren().addAll(lblQ, tfQuestion, btnSupprimer);
            */
            questionRow.getChildren().addAll(lblQ, tfQuestion);
            questionsBox.getChildren().add(questionRow);
        });

        // ================= BOUTON AJOUTER =================
        
        Button btnAjouter = new Button("Ajouter l'enquête");
        btnAjouter.setPrefWidth(180);
        btnAjouter.setStyle(
                "-fx-background-color: WHITE; " +
                        "-fx-text-fill: BLACK; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 14px; " +
                        "-fx-border-color: BLACK; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-background-radius: 5px;"
        );

        btnAjouter.setOnAction(e -> {
            try {
                String sujet = tfSujet.getText().trim();
                String desc = taDesc.getText().trim();
                boolean anonyme = cbAnonyme.isSelected();

                // Validation
                if (sujet.isEmpty()) {
                    showAlert("Le sujet est obligatoire !");
                    return;
                }

                // Récupérer toutes les questions
                List<AttributEnquete> attributs = new ArrayList<>();
                for (var node : questionsBox.getChildren()) {
                    // Chaque node est un HBox contenant le TextField
                    if (node instanceof HBox hbox) {
                        for (var child : hbox.getChildren()) {
                            if (child instanceof TextField tfChild) {
                                String question = tfChild.getText().trim();
                                if (!question.isEmpty()) {
                                    AttributEnquete attr = new AttributEnquete();
                                    attr.setNom(question);
                                    attributs.add(attr);
                                }
                            }
                        }
                    }
                }

                if (attributs.isEmpty()) {
                    showAlert("Veuillez ajouter au moins une question !");
                    return;
                }

                // Créer l'enquête et ajouter les questions
                Enquete enquete = new Enquete(sujet, desc, anonyme);
                enquete.setAttributs(attributs);

                // Ajouter dans la base via Service
                service.ajouter(enquete);

                showAlert("Enquête ajoutée avec succès !");

                // Réinitialiser le formulaire
                tfSujet.clear();
                taDesc.clear();
                cbAnonyme.setSelected(true);
                questionsBox.getChildren().clear();

            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Erreur : " + ex.getMessage());
            }
        });

        formBox.getChildren().addAll(
                title,
                lblSujet, tfSujet,
                lblDesc, taDesc,
                cbAnonyme,
                lblQuestions, questionsBox, btnAjouterQuestion,
                btnAjouter
        );

        // ================= ROOT =================
        root.setTop(header);
        root.setCenter(formBox);
        root.setStyle("-fx-background-color: #F2F0EF;");

        Scene scene = new Scene(root, 900, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ajouter Enquête");
        primaryStage.show();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}