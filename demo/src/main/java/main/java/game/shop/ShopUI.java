package main.java.game.shop;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import main.java.game.Player;
import main.java.game.inventory.Item;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface utilisateur de la boutique du jeu
 * Permet au joueur d'acheter des objets et des points de vie
 */
public class ShopUI {
    @FXML private GridPane shopGrid;      // Grille contenant les articles
    @FXML private Label playerCoins;      // Affichage des pièces du joueur
    
    private Player player;
    private List<ShopItem> shopItems = new ArrayList<>();

    /**
     * Initialise la boutique avec des articles par défaut
     */
    public void initialize() {
        shopItems.add(new ShopItem(new Item("potion", "Potion de vie", null, "Restaure 2 points de vie", true, 5), 10));
        shopItems.add(new ShopItem(new Item("sword", "Épée en fer", null, "Une épée basique", false, 1), 30));
        shopItems.add(new ShopItem(3, 25)); // Pack de 3 points de vie
    }

    public void setPlayer(Player player) {
        this.player = player;
        updateUI();
    }

    /**
     * Met à jour l'affichage de la boutique :
     * - Actualise le nombre de pièces du joueur
     * - Rafraîchit la liste des articles disponibles
     */
    public void updateUI() {
        playerCoins.setText("Pièces: " + player.getStats().getCoins());
        shopGrid.getChildren().clear();

        int row = 0;
        for (ShopItem shopItem : shopItems) {
            VBox itemBox = createShopItemBox(shopItem);
            shopGrid.add(itemBox, 0, row++);
        }
    }

    /**
     * Crée une boîte d'affichage pour un article de la boutique
     * avec son nom, son prix et un bouton d'achat
     */
    private VBox createShopItemBox(ShopItem shopItem) {
        VBox itemBox = new VBox(5);
        itemBox.getStyleClass().add("shop-item");

        String itemName = shopItem.getType().equals("HEALTH") 
            ? "Pack de " + shopItem.getHealthAmount() + " vies" 
            : shopItem.getItem().getName();

        Label nameLabel = new Label(itemName);
        nameLabel.getStyleClass().add("shop-item-name");

        Label priceLabel = new Label(shopItem.getPrice() + " pièces");
        priceLabel.getStyleClass().addAll("shop-item-price", 
            player.getStats().getCoins() >= shopItem.getPrice() 
                ? "shop-item-price-affordable" 
                : "shop-item-price-expensive");

        Button buyButton = new Button("Acheter");
        buyButton.getStyleClass().add("shop-button");
        buyButton.setOnAction(e -> buyItem(shopItem));

        itemBox.getChildren().addAll(nameLabel, priceLabel, buyButton);
        return itemBox;
    }

    /**
     * Gère l'achat d'un article :
     * - Vérifie si le joueur a assez de pièces
     * - Déduit le prix et ajoute l'objet à l'inventaire ou soigne le joueur
     */
    private void buyItem(ShopItem shopItem) {
        if (player.getStats().getCoins() < shopItem.getPrice()) {
            System.out.println("Pas assez d'argent!");
            return;
        }

        player.getStats().spendCoins(shopItem.getPrice());

        if (shopItem.getType().equals("HEALTH")) {
            player.getStats().heal(shopItem.getHealthAmount());
        } else {
            player.getInventory().addItem(shopItem.getItem(), 1);
        }

        updateUI();
    }
}