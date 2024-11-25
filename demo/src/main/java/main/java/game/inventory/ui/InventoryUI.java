package main.java.game.inventory.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import main.java.game.inventory.Inventory;
import main.java.game.inventory.InventorySlot;
import main.java.game.Player;
import main.java.game.inventory.Item;

/**
 * Gère l'interface utilisateur de l'inventaire du joueur
 */
public class InventoryUI {
    @FXML
    private GridPane inventoryGrid;
    
    private Inventory inventory;
    private StackPane[] slotPanes; // Tableau des emplacements visuels de l'inventaire
    private Player player;

    /**
     * Initialise la grille d'inventaire avec 24 emplacements (6x4)
     */
    public void initialize() {
        slotPanes = new StackPane[24];
        
        for (int i = 0; i < 24; i++) {
            StackPane slotPane = createSlotPane(i);
            slotPanes[i] = slotPane;
            inventoryGrid.add(slotPane, i % 6, i / 6);
        }
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        updateUI();
    }
    
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Crée un emplacement visuel d'inventaire avec gestion des clics
     */
    private StackPane createSlotPane(int index) {
        StackPane slotPane = new StackPane();
        slotPane.getStyleClass().add("inventory-slot");
        
        slotPane.setOnMouseClicked(e -> handleSlotClick(index));
        
        return slotPane;
    }

    /**
     * Gère l'utilisation des objets lors d'un clic sur un emplacement
     * Pour l'instant, seules les potions de vie sont utilisables
     */
    private void handleSlotClick(int index) {
        if (player == null) return;
        
        InventorySlot slot = inventory.getSlot(index);
        if (!slot.isEmpty()) {
            Item item = slot.getItem();
            
            if (item.getName().equals("Potion de vie") && item.isConsumable()) {
                player.getStats().heal(2);
                inventory.removeItem(item, 1);
                updateUI();
                System.out.println("Potion utilisée ! Vie actuelle : " + player.getStats().getHealth());
            }
        }
    }

    /**
     * Met à jour l'affichage de tous les emplacements de l'inventaire
     * Affiche le nom de l'objet et sa quantité pour chaque emplacement non vide
     */
    public void updateUI() {
        for (int i = 0; i < inventory.getSize(); i++) {
            InventorySlot slot = inventory.getSlot(i);
            StackPane slotPane = slotPanes[i];
            
            slotPane.getChildren().clear();
            
            if (!slot.isEmpty()) {
                Label itemLabel = new Label(slot.getItem().getName());
                itemLabel.setStyle("-fx-text-fill: white;");
                
                Label quantityLabel = new Label(String.valueOf(slot.getQuantity()));
                quantityLabel.setStyle("-fx-text-fill: white;");
                
                slotPane.getChildren().addAll(itemLabel, quantityLabel);
            }
        }
    }

    public Inventory getInventory() {
        return inventory;
    }
}