package main.java.game.quest.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import main.java.game.Player;
import main.java.game.quest.Quest;
import main.java.game.quest.QuestChoice;
import main.java.game.quest.QuestReward;
import java.util.List;

/**
 * Interface utilisateur pour afficher et gérer les quêtes
 * Permet d'afficher le titre, la description, les choix possibles et les récompenses d'une quête
 */
public class QuestUI {
    @FXML private VBox root;
    @FXML private Label questTitle;
    @FXML private Label questDescription;
    @FXML private VBox choicesContainer;
    @FXML private Label rewardLabel;
    
    private Quest currentQuest;
    private Player player;

    public VBox getRoot() {
        return root;
    }

    public void setQuest(Quest quest) {
        this.currentQuest = quest;
        updateUI();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Met à jour l'interface avec les informations de la quête courante
     * Affiche le titre, la description et crée des boutons pour chaque choix possible
     */
    public void updateUI() {
        if (currentQuest == null) return;

        questTitle.setText(currentQuest.getTitle());
        questDescription.setText(currentQuest.getCurrentState().getDescription());
        
        choicesContainer.getChildren().clear();
        List<QuestChoice> choices = currentQuest.getCurrentChoices();
        
        for (int i = 0; i < choices.size(); i++) {
            QuestChoice choice = choices.get(i);
            Button choiceButton = new Button(choice.getText());
            final int choiceIndex = i;
            
            choiceButton.setOnAction(e -> {
                currentQuest.makeChoice(choiceIndex);
                if (currentQuest.isCompleted()) {
                    handleQuestCompletion();
                } else {
                    updateUI();
                }
            });
            
            choiceButton.getStyleClass().add("quest-choice-button");
            choicesContainer.getChildren().add(choiceButton);
        }
    }

    /**
     * Gère la fin d'une quête en attribuant les récompenses au joueur
     * Affiche les récompenses obtenues (pièces, expérience, objets)
     */
    private void handleQuestCompletion() {
        QuestReward reward = currentQuest.getReward();
        if (reward != null) {
            if (reward.getCoins() > 0) {
                player.getStats().addCoins(reward.getCoins());
            }
            if (reward.getItem() != null) {
                player.getInventory().addItem(reward.getItem(), 1);
            }
            
            rewardLabel.setText(String.format(
                "Récompenses obtenues : %d pièces, %d expérience%s",
                reward.getCoins(),
                reward.getExperience(),
                reward.getItem() != null ? ", " + reward.getItem().getName() : ""
            ));
        }
        
        questDescription.setText("Quête terminée !");
        choicesContainer.getChildren().clear();
    }
}