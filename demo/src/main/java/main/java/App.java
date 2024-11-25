package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.io.File;
import java.net.URISyntaxException;

/**
 * Point d'entrée principal de l'application JavaFX
 * Gère le chargement et l'affichage de la fenêtre principale
 */
public class App extends Application {
    private Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Charge l'interface du menu principal depuis le fichier FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main-menu.fxml"));
            Parent root = fxmlLoader.load();
            
            // Crée la scène principale de 800x600 pixels
            scene = new Scene(root, 800, 600);
            
            // Récupère les ressources (images et styles)
            URL imageUrl = getClass().getResource("/sprites/gui/menu/background.png");
            URL cssUrl = getClass().getResource("/styles/main-menu.css");
            
            // Affiche des informations de debug sur les ressources chargées
            System.out.println("=== Debug Resources ===");
            System.out.println("Image URL: " + imageUrl);
            System.out.println("CSS URL: " + cssUrl);
            
            if (imageUrl != null) {
                File file = new File(imageUrl.toURI());
                System.out.println("Image exists: " + file.exists());
                System.out.println("Image size: " + file.length() + " bytes");
            }
            
            // Applique le style CSS à la scène
            scene.getStylesheets().add(getClass().getResource("/styles/main-menu.css").toExternalForm());
            
            // Configure et affiche la fenêtre principale
            stage.setTitle("Orphée");
            stage.setScene(scene);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();
            
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Lance l'application JavaFX
     */
    public static void main(String[] args) {
        launch(args);
    }
}
