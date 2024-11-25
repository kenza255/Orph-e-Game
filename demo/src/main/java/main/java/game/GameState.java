package main.java.game;

import java.io.Serializable;

/**
 * Classe qui représente l'état du jeu à un instant donné
 * Permet de sauvegarder et charger une partie
 */
public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Statistiques du joueur
    private int playerLevel;      // Niveau actuel du joueur
    private int playerHealth;     // Points de vie du joueur
    private String currentLocation; // Zone actuelle du joueur
    private double playerX;       // Position X du joueur sur la carte
    private double playerY;       // Position Y du joueur sur la carte
    
    /**
     * Crée un nouvel état de jeu avec les valeurs par défaut
     * Le joueur commence au niveau 1 avec 100 PV à la position de départ
     */
    public GameState() {
        playerLevel = 1;
        playerHealth = 100;
        currentLocation = "start";
        playerX = 400;
        playerY = 300;
    }
    
    // Getters et setters pour accéder aux propriétés
    public int getPlayerLevel() { return playerLevel; }
    public void setPlayerLevel(int level) { this.playerLevel = level; }
    
    public int getPlayerHealth() { return playerHealth; }
    public void setPlayerHealth(int health) { this.playerHealth = health; }
    
    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String location) { this.currentLocation = location; }
    
    public double getPlayerX() { return playerX; }
    public void setPlayerX(double x) { this.playerX = x; }
    
    public double getPlayerY() { return playerY; }
    public void setPlayerY(double y) { this.playerY = y; }
}
