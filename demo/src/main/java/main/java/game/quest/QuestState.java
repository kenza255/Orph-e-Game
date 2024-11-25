package main.java.game.quest;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un état (une étape) d'une quête
 * Chaque état contient une description de la situation
 * et une liste de choix possibles pour le joueur
 */
public class QuestState {
    private String id;                  // Identifiant unique de l'état
    private String description;         // Description de la situation actuelle
    private List<QuestChoice> choices;  // Liste des choix possibles

    /**
     * Crée un nouvel état de quête avec un id et une description
     */
    public QuestState(String id, String description) {
        this.id = id;
        this.description = description;
        this.choices = new ArrayList<>();
    }

    /**
     * Ajoute un nouveau choix possible pour cet état
     */
    public void addChoice(QuestChoice choice) {
        choices.add(choice);
    }

    public String getId() { return id; }
    public String getDescription() { return description; }
    public List<QuestChoice> getChoices() { return choices; }
}