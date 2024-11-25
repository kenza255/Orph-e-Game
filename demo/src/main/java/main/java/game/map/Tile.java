package main.java.game.map;

import javafx.scene.image.Image;

/**
 * Représente une tuile (case) sur la carte du jeu
 * Chaque tuile a une image, un identifiant unique et peut bloquer ou non le passage
 */
public class Tile {
    private int id;             // Identifiant unique de la tuile
    private Image image;        // Image affichée pour cette tuile
    private boolean collision;  // True si la tuile bloque le passage

    /**
     * Crée une nouvelle tuile avec l'identifiant spécifié
     * Par défaut, la tuile ne bloque pas le passage
     */
    public Tile(int id) {
        this.id = id;
        this.collision = false;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    /**
     * Indique si cette tuile bloque le passage
     */
    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }
}