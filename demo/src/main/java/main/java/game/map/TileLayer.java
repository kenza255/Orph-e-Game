package main.java.game.map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.java.game.Camera;

/**
 * Représente une couche de tuiles dans la carte du jeu
 * Chaque couche peut être une couche normale (décor) ou une couche de collision
 */
public class TileLayer {
    private String name;                // Nom de la couche
    private int[][] tileData;           // Données des tuiles (tableau 2D d'identifiants)
    private boolean isCollisionLayer;    // Indique si c'est une couche de collision

    /**
     * Crée une nouvelle couche de tuiles
     * Si le nom contient "collision", la couche sera traitée comme une couche de collision
     */
    public TileLayer(String name, int[][] tileData, int offsetX, int offsetY) {
        this.name = name;
        this.tileData = tileData;
        this.isCollisionLayer = name.toLowerCase().contains("collision");
    }

    /**
     * Dessine la couche de tuiles à l'écran en tenant compte de la caméra
     * Utilise un tileset (image contenant toutes les tuiles) comme source
     */
    public void render(GraphicsContext gc, Image tileset, int startX, int startY, 
                      int endX, int endY, int tileWidth, int tileHeight, 
                      double scale, Camera camera) {
        
        int tilesPerRow = (int)(tileset.getWidth() / tileWidth);
        
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                if (y < 0 || y >= tileData.length || x < 0 || x >= tileData[0].length) continue;
                
                int tileId = tileData[y][x];
                if (tileId > 0) {
                    tileId--;
                    int sourceX = (tileId % tilesPerRow) * tileWidth;
                    int sourceY = (tileId / tilesPerRow) * tileHeight;
                    
                    double destX = (x * tileWidth * scale) - camera.getX();
                    double destY = (y * tileHeight * scale) - camera.getY();
                    
                    gc.drawImage(tileset,
                        sourceX, sourceY, tileWidth, tileHeight,
                        destX, destY, tileWidth * scale, tileHeight * scale);
                }
            }
        }
    }

    /**
     * Indique si cette couche est une couche de collision
     */
    public boolean isCollision() {
        return isCollisionLayer;
    }

    /**
     * Vérifie s'il y a une collision à la position donnée
     * Retourne true si hors limites ou si une tuile est présente
     */
    public boolean hasCollisionAt(int x, int y) {
        if (x < 0 || y < 0 || y >= tileData.length || x >= tileData[0].length) {
            return true;
        }
        return tileData[y][x] > 0;
    }
}