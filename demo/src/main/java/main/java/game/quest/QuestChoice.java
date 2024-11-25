package main.java.game.quest;

/**
 * Représente un choix possible dans une quête
 * Chaque choix peut mener à un nouvel état de la quête et/ou donner une récompense
 */
public class QuestChoice {
    private String text;          // Texte affiché pour le choix
    private QuestState nextState; // État suivant de la quête si ce choix est fait
    private QuestReward reward;   // Récompense éventuelle liée au choix

    /**
     * Crée un choix simple menant à un nouvel état
     */
    public QuestChoice(String text, QuestState nextState) {
        this.text = text;
        this.nextState = nextState;
    }

    /**
     * Crée un choix avec une récompense associée
     */
    public QuestChoice(String text, QuestState nextState, QuestReward reward) {
        this(text, nextState);
        this.reward = reward;
    }

    public String getText() { return text; }
    public QuestState getNextState() { return nextState; }
    public QuestReward getReward() { return reward; }
}