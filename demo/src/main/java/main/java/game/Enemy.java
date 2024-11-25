package main.java.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Représente un ennemi dans le jeu
 * Les ennemis peuvent infliger des dégâts au joueur et mourir en laissant tomber des pièces
 */
public class Enemy {
    private double x;
    private double y;
    private final double size = 30;  // Taille en pixels de l'ennemi
    private int health = 3;          // Points de vie de l'ennemi
    private boolean isDead = false;
    private long lastDamageTime = 0;
    private static final long DAMAGE_COOLDOWN = 2000;  // Délai entre chaque attaque (en ms)

    /**
     * Crée un nouvel ennemi à la position spécifiée
     */
    public Enemy(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Dessine l'ennemi à l'écran s'il est vivant
     */
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        if (!isDead) {
            gc.setFill(Color.RED);
            gc.fillRect(x - cameraX, y - cameraY, size, size);
        }
    }

    /**
     * Inflige des dégâts à l'ennemi et donne des pièces au joueur s'il meurt
     */
    public void takeDamage(Player player) {
        health--;
        if (health <= 0 && !isDead) {
            isDead = true;
            player.getStats().addCoins(10);
        }
    }

    /**
     * Vérifie si l'ennemi peut attaquer (cooldown écoulé)
     */
    public boolean canDamagePlayer() {
        return System.currentTimeMillis() - lastDamageTime >= DAMAGE_COOLDOWN;
    }

    /**
     * Inflige des dégâts au joueur si possible
     */
    public void dealDamage(Player player) {
        if (canDamagePlayer() && !isDead) {
            player.getStats().takeDamage(1);
            lastDamageTime = System.currentTimeMillis();
        }
    }

    /**
     * Vérifie si le joueur est à portée d'attaque (rayon de 50 pixels)
     */
    public boolean isNearPlayer(Player player) {
        double distance = Math.sqrt(
            Math.pow(player.getX() + player.width/2 - (x + size/2), 2) +
            Math.pow(player.getY() + player.height/2 - (y + size/2), 2)
        );
        return distance < 50;
    }

    public boolean isDead() {
        return isDead;
    }

    public void update(double deltaTime) {
    }
}