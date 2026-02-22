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
import tn.edu.esprit.entities.Enquete;
import tn.edu.esprit.services.ServiceEnquete;

import java.util.List;

public class MainApp extends Application {

    private Stage primaryStage;
    private boolean isAdmin = true; 

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("Khademni");
        showAccueil();
        primaryStage.show();
    }

    private void showAccueil() {

        BorderPane root = new BorderPane();

        // ===== HEADER =====
        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #1F1B1B;");

        Label logo = new Label("Khademni");
        logo.setTextFill(Color.WHITE);
        logo.setFont(Font.font("Verdana", 22));
        logo.setStyle("-fx-font-weight: bold;");

        TextField searchField = new TextField();
        searchField.setPromptText("Recherche...");
        searchField.setPrefWidth(200);

        Button btnSearch = new Button("🔍");
        btnSearch.setStyle("-fx-background-color: WHITE; -fx-text-fill: BLACK;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnLogin = new Button("Se connecter");
        Button btnSignup = new Button("S’inscrire");
        Button btnReclamation = new Button("Réclamation");
        Button btnEnquetes = new Button("Enquêtes");

        btnLogin.setStyle("-fx-background-color: WHITE;");
        btnSignup.setStyle("-fx-background-color: WHITE;");
        btnReclamation.setStyle("-fx-background-color: WHITE;");
        btnEnquetes.setStyle("-fx-background-color: WHITE;");

        // ===== ACTIONS =====
        btnLogin.setOnAction(e -> showMessage("Page Login"));
        btnSignup.setOnAction(e -> showMessage("Page Signup"));

        btnReclamation.setOnAction(e -> {
            try {
                new ReclamationForm().start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ✅ Bouton Enquêtes correctement branché
        btnEnquetes.setOnAction(e -> {
            try {
                new EnquetesGestion(primaryStage).start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ===== RECHERCHE =====
        btnSearch.setOnAction(e -> {
            String query = searchField.getText().trim().toLowerCase();
            VBox results = new VBox(10);
            results.setPadding(new Insets(20));

            if (query.isEmpty()) {
                results.getChildren().add(new Label("Veuillez entrer un mot clé."));
            } else {
                List<Enquete> enquetes = new ServiceEnquete().getEnquetesActives();
                boolean found = false;

                for (Enquete enq : enquetes) {
                    if (enq.getSujet().toLowerCase().contains(query)
                            || enq.getDescription().toLowerCase().contains(query)) {

                        results.getChildren().add(
                                new Label("Enquête : " + enq.getSujet())
                        );
                        found = true;
                    }
                }

                if (!found) {
                    results.getChildren().add(
                            new Label("Aucun résultat trouvé.")
                    );
                }
            }

            root.setCenter(results);
        });

        topBar.getChildren().addAll(
                logo,
                searchField,
                btnSearch,
                spacer,
                btnLogin,
                btnSignup,
                btnReclamation,
                btnEnquetes
        );

        // ===== CENTRE =====
        VBox center = new VBox(20);
        center.setAlignment(Pos.CENTER);

        Label welcome = new Label(isAdmin ? "Bienvenue Admin" : "Bienvenue Utilisateur");
        welcome.setFont(Font.font("Verdana", 30));

        center.getChildren().add(welcome);

        root.setTop(topBar);
        root.setCenter(center);
        root.setStyle("-fx-background-color: #F2F0EF;");

        Scene scene = new Scene(root, 900, 500);
        primaryStage.setScene(scene);
    }

    private void showMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}