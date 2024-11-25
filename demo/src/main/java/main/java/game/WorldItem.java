package main.java.game;

import javafx.scene.canvas.GraphicsContext;
import main.java.game.inventory.Item;

/**
 * Représente un objet ramassable dans le monde du jeu
 */
public class WorldItem {
    private double x;                       // Position X de l'objet
    private double y;                       // Position Y de l'objet
    private final Item item;                // L'objet à ramasser
    private final double size = 20;         // Taille d'affichage de l'objet
    private boolean isNearPlayer = false;   // Si le joueur est assez proche pour ramasser

    /**
     * Crée un nouvel objet ramassable aux coordonnées spécifiées
     */
    public WorldItem(double x, double y, Item item) {
        this.x = x;
        this.y = y;
        this.item = item;
    }

    /**
     * Dessine l'objet et affiche un message si le joueur est proche
     */
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        gc.setFill(javafx.scene.paint.Color.BLACK);
        gc.fillRect(x - cameraX, y - cameraY, size, size);
        
        if (isNearPlayer) {
            gc.setFill(javafx.scene.paint.Color.WHITE);
            gc.fillText("Appuyez sur A pour ramasser", 
                       x - cameraX, 
                       y - cameraY - 10);
        }
    }

    /**
     * Vérifie si le joueur est assez proche pour ramasser l'objet (distance < 50 pixels)
     */
    public boolean isNearPlayer(Player player) {
        double distance = Math.sqrt(
            Math.pow(player.getX() + player.width/2 - (x + size/2), 2) +
            Math.pow(player.getY() + player.height/2 - (y + size/2), 2)
        );
        isNearPlayer = distance < 50;
        return isNearPlayer;
    }

    public Item getItem() {
        return item;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
}