package main.java.game.ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.java.game.Player;
import main.java.game.Stats;

/**
 * Classe qui gère le rendu de l'interface utilisateur (UI)
 * Affiche les points de vie et les pièces du joueur
 */
public class UIRenderer {
    /**
     * Dessine l'interface utilisateur avec la vie et les pièces du joueur
     * @param gc Contexte graphique pour dessiner
     * @param player Joueur dont on affiche les statistiques
     */
    public static void renderUI(GraphicsContext gc, Player player) {
        Stats stats = player.getStats();
        // Affiche les coeurs de vie (rouges si pleins, gris si vides)
        for (int i = 0; i < stats.getMaxHealth(); i++) {
            if (i < stats.getHealth()) {
                renderHeart(gc, 10 + i * 30, 10, true);
            } else {
                renderHeart(gc, 10 + i * 30, 10, false);
            }
        }

        // Affiche le nombre de pièces
        renderCoin(gc, 10, 50);
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 16));
        gc.fillText(String.valueOf(stats.getCoins()), 35, 65);
    }

    private static void renderHeart(GraphicsContext gc, double x, double y, boolean filled) {
        gc.setFill(filled ? Color.RED : Color.GRAY);
        gc.fillOval(x, y, 20, 20);
    }

    private static void renderCoin(GraphicsContext gc, double x, double y) {
        gc.setFill(Color.YELLOW);
        gc.fillOval(x, y, 20, 20);
    }
}