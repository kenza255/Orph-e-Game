package main.java.game;

import main.java.game.map.TileMap;
import main.java.game.Player;

/**
 * Gère la caméra qui suit le joueur dans le jeu
 * Permet de définir quelle portion de la carte est visible à l'écran
 */
public class Camera {
    // Position de la caméra dans le monde
    private double x;
    private double y;
    
    // Dimensions de la zone visible à l'écran
    private double viewportWidth;
    private double viewportHeight;
    
    // Dimensions totales de la carte de jeu
    private double mapWidth;
    private double mapHeight;
    
    /**
     * Crée une nouvelle caméra avec une zone de vue donnée
     */
    public Camera(double viewportWidth, double viewportHeight) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
        this.x = 0;
        this.y = 0;
    }
    
    /**
     * Définit les limites de la carte pour empêcher la caméra de sortir
     */
    public void setBounds(double width, double height) {
        this.mapWidth = width;
        this.mapHeight = height;
    }
    
    /**
     * Initialise la caméra avec la carte et centre sur le joueur
     */
    public void initializeCameraBounds(TileMap map, Player player) {
        setBounds(map.getTotalWidth(), map.getTotalHeight());
        updatePosition(player.getX(), player.getY());
    }
    
    /**
     * Met à jour la position de la caméra en restant dans les limites de la carte
     */
    public void updatePosition(double targetX, double targetY) {
        this.x = Math.max(0, Math.min(targetX - viewportWidth / 2, mapWidth - viewportWidth));
        this.y = Math.max(0, Math.min(targetY - viewportHeight / 2, mapHeight - viewportHeight));
    }
    
    /**
     * Centre la caméra sur un point spécifique
     */
    public void centerOn(double x, double y) {
        double targetX = x - viewportWidth / 2;
        double targetY = y - viewportHeight / 2;
        
        this.x = Math.max(0, Math.min(targetX, mapWidth - viewportWidth));
        this.y = Math.max(0, Math.min(targetY, mapHeight - viewportHeight));
    }
    
    // Getters pour accéder aux propriétés de la caméra
    public double getX() { return x; }
    public double getY() { return y; }
    public double getViewportWidth() { return viewportWidth; }
    public double getViewportHeight() { return viewportHeight; }
}