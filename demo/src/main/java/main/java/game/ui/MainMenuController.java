package main.java.game.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import java.io.File;
import java.io.IOException;

/**
 * Contrôleur du menu principal du jeu
 * Gère les actions des boutons (nouvelle partie, charger, paramètres, etc.)
 */
public class MainMenuController {

    /**
     * Démarre une nouvelle partie
     */
    @FXML
    private void newGame(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game-scene.fxml"));
            Parent gameRoot = loader.load();
            Scene gameScene = new Scene(gameRoot);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            stage.setScene(gameScene);
            stage.show();
            
            gameRoot.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Charge une partie sauvegardée si elle existe
     */
    @FXML
    private void loadGame(ActionEvent event) {
        File saveFile = new File("save/game.sav");
        if (saveFile.exists()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game-scene.fxml"));
                Parent gameRoot = loader.load();
                
                GameController gameController = loader.getController();
                try {
                    gameController.loadSaveGame();
                } catch (IOException e) {
                    showLoadGameErrorAlert();
                    return;
                }
                
                Scene gameScene = new Scene(gameRoot, 1024, 768);
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.setScene(gameScene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                showLoadGameErrorAlert();
            }
        } else {
            showNoSaveGameAlert();
        }
    }

    @FXML
    private void openSettings() {
        try {
            System.out.println("Paramètres");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showCredits() {
        try {
            System.out.println("Crédits");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exitGame() {
        Platform.exit();
    }

    /**
     * Affiche une alerte si aucune sauvegarde n'est trouvée
     */
    private void showNoSaveGameAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Aucune sauvegarde");
        alert.setHeaderText(null);
        alert.setContentText("Aucune sauvegarde n'a été trouvée.");
        alert.showAndWait();
    }

    /**
     * Affiche une alerte en cas d'erreur lors du chargement
     */
    private void showLoadGameErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de chargement");
        alert.setHeaderText(null);
        alert.setContentText("Une erreur est survenue lors du chargement de la sauvegarde.");
        alert.showAndWait();
    }

    /**
     * Initialise l'interface du menu principal
     * Charge les ressources graphiques nécessaires
     */
    @FXML
    private void initialize() {
        try {
            String spritesheetPath = "/sprites/gui/menu/buttons/SpriteSheet.png";
            var inputStream = getClass().getResourceAsStream(spritesheetPath);
            if (inputStream == null) {
                System.err.println("Impossible de charger le spritesheet: " + spritesheetPath);
                return;
            }
            Image spritesheet = new Image(inputStream);
            
            WritableImage buttonImage = new WritableImage(spritesheet.getPixelReader(), 
                0, 0,
                200, 40
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
