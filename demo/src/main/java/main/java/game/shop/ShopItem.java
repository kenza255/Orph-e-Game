package main.java.game.shop;

import main.java.game.inventory.Item;

/**
 * Représente un article dans la boutique du jeu
 * Peut être soit un objet (Item) soit des points de vie à acheter
 */
public class ShopItem {
    private Item item;          // L'objet à vendre (null si c'est de la vie)
    private int price;         // Prix en pièces
    private String type;       // Type d'article : "ITEM" ou "HEALTH"
    private int healthAmount;  // Quantité de points de vie (si type="HEALTH")

    /**
     * Crée un article de type objet
     */
    public ShopItem(Item item, int price) {
        this.item = item;
        this.price = price;
        this.type = "ITEM";
    }

    /**
     * Crée un article de type points de vie
     */
    public ShopItem(int healthAmount, int price) {
        this.healthAmount = healthAmount;
        this.price = price;
        this.type = "HEALTH";
    }

    public Item getItem() { return item; }
    public int getPrice() { return price; }
    public String getType() { return type; }
    public int getHealthAmount() { return healthAmount; }
}