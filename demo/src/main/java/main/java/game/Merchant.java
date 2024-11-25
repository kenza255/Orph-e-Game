package main.java.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;

/**
 * Représente un marchand dans le jeu avec qui le joueur peut interagir
 * Le marchand s'affiche comme un carré bleu et affiche un message quand le joueur est proche
 */
public class Merchant {
    private double x;              // Position X du marchand
    private double y;              // Position Y du marchand
    private final double size = 40;  // Taille en pixels du marchand
    private boolean isNearPlayer = false;  // Indique si un joueur est à proximité

    /**
     * Crée un nouveau marchand à la position spécifiée
     */
    public Merchant(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Dessine le marchand et affiche un message si le joueur est proche
     */
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        // Dessine le marchand en bleu
        gc.setFill(Color.BLUE);
        gc.fillRect(x - cameraX, y - cameraY, size, size);

        // Affiche le message d'interaction si le joueur est proche
        if (isNearPlayer) {
            gc.setFill(Color.rgb(0, 0, 0, 0.7));
            gc.fillRect(x - cameraX - 100, y - cameraY - 30, 200, 20);
            
            gc.setFill(Color.WHITE);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(Font.font("Arial", 14));
            
            gc.fillText("Appuyez sur T pour commercer", 
                       x - cameraX + size/2, 
                       y - cameraY - 15);
        }
    }

    /**
     * Vérifie si le joueur est à portée d'interaction (rayon de 60 pixels)
     */
    public boolean isNearPlayer(Player player) {
        double distance = Math.sqrt(
            Math.pow(player.getX() + player.width/2 - (x + size/2), 2) +
            Math.pow(player.getY() + player.height/2 - (y + size/2), 2)
        );
        isNearPlayer = distance < 60;
        return isNearPlayer;
    }
}