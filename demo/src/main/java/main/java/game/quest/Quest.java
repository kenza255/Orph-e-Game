package main.java.game.quest;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une quête dans le jeu avec différents états et choix possibles
 * Une quête est composée d'états successifs, chaque état proposant des choix au joueur
 */
public class Quest {
    private String id;                  // Identifiant unique de la quête
    private String title;               // Titre affiché de la quête
    private String description;         // Description générale de la quête
    private QuestState currentState;    // État actuel de la quête
    private List<QuestChoice> currentChoices;  // Choix disponibles dans l'état actuel
    private boolean isCompleted;        // Indique si la quête est terminée
    private QuestReward reward;         // Récompense finale de la quête

    /**
     * Crée une nouvelle quête avec ses informations de base
     */
    public Quest(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.currentChoices = new ArrayList<>();
        this.isCompleted = false;
    }

    /**
     * Change l'état actuel de la quête et met à jour les choix disponibles
     */
    public void setState(QuestState state) {
        this.currentState = state;
        this.currentChoices = state.getChoices();
    }

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }

    /**
     * Effectue un choix qui peut soit mener à un nouvel état,
     * soit terminer la quête avec une récompense
     */
    public void makeChoice(int choiceIndex) {
        if (choiceIndex >= 0 && choiceIndex < currentChoices.size()) {
            QuestChoice choice = currentChoices.get(choiceIndex);
            QuestState nextState = choice.getNextState();
            if (nextState != null) {
                setState(nextState);
            } else {
                isCompleted = true;
                if (choice.getReward() != null) {
                    this.reward = choice.getReward();
                }
            }
        }
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public QuestState getCurrentState() { return currentState; }
    public List<QuestChoice> getCurrentChoices() { return currentChoices; }
    public boolean isCompleted() { return isCompleted; }
    public QuestReward getReward() { return reward; }
}