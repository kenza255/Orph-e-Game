package main.java.game.quest;

import main.java.game.inventory.Item;

/**
 * Représente la récompense obtenue à la fin d'une quête
 * Une récompense peut contenir des pièces, un objet et de l'expérience
 */
public class QuestReward {
    private int coins;      // Nombre de pièces gagnées
    private Item item;      // Objet obtenu en récompense (peut être null)
    private int experience; // Points d'expérience gagnés

    /**
     * Crée une nouvelle récompense avec les gains spécifiés
     */
    public QuestReward(int coins, Item item, int experience) {
        this.coins = coins;
        this.item = item;
        this.experience = experience;
    }

    public int getCoins() { return coins; }
    public Item getItem() { return item; }
    public int getExperience() { return experience; }
}