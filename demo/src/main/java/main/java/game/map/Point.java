package main.java.game.map;

import java.util.Objects;

/**
 * Représente un point dans l'espace 2D avec des coordonnées x et y
 * Utilisé pour stocker des positions sur la carte du jeu
 */
public class Point {
    private final int x;    // Coordonnée horizontale
    private final int y;    // Coordonnée verticale
    
    /**
     * Crée un nouveau point avec les coordonnées spécifiées
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Vérifie si deux points ont les mêmes coordonnées
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}