package main.java.game;

import javafx.scene.canvas.GraphicsContext;
import main.java.game.map.TileMap;
import main.java.game.inventory.Inventory;

/**
 * Représente le joueur dans le jeu
 * Gère les déplacements, les collisions et le rendu du personnage
 */
public class Player {
    private final PlayerSprite sprite;      // Sprite animé du joueur
    private TileMap tileMap;                // Carte du jeu pour les collisions
    private double speed = 100.0;           // Vitesse de déplacement en pixels/seconde
    private double dx = 0;                  // Direction horizontale (-1: gauche, 0: immobile, 1: droite)
    private double dy = 0;                  // Direction verticale (-1: haut, 0: immobile, 1: bas)
    private double x;                       // Position X du joueur
    private double y;                       // Position Y du joueur
    public final double width = 32 * 2;     // Largeur de la hitbox du joueur
    public final double height = 32 * 2;    // Hauteur de la hitbox du joueur
    private Stats stats;                    // Statistiques du joueur (vie, pièces, etc)
    private Inventory inventory;            // Inventaire du joueur
    
    /**
     * Crée un nouveau joueur au centre de l'écran
     */
    public Player() {
        sprite = new PlayerSprite();
        stats = new Stats();
        inventory = new Inventory(24);
        x = 400 - sprite.getFitWidth()/2;
        y = 300 - sprite.getFitHeight()/2;
    }
    
    // Méthodes de contrôle du mouvement
    public void moveUp() {
        dy = -1;
    }
    
    public void moveDown() {
        dy = 1;
    }
    
    public void moveLeft() {
        dx = -1;
    }
    
    public void moveRight() {
        dx = 1;
    }
    
    public void stopVertical() {
        dy = 0;
    }
    
    public void stopHorizontal() {
        dx = 0;
    }
    
    public boolean isMovingUp() {
        return dy < 0;
    }
    
    public boolean isMovingDown() {
        return dy > 0;
    }
    
    public boolean isMovingLeft() {
        return dx < 0;
    }
    
    public boolean isMovingRight() {
        return dx > 0;
    }
    
    /**
     * Met à jour la position du joueur en fonction du temps écoulé
     * Gère aussi la direction du sprite et les collisions
     */
    public void update(double deltaTime) {
        double moveX = dx;
        double moveY = dy;
        
        // Normalise la vitesse en diagonale
        if (dx != 0 && dy != 0) {
            double length = Math.sqrt(2);
            moveX /= length;
            moveY /= length;
        }
        
        double deltaX = moveX * speed * deltaTime;
        double deltaY = moveY * speed * deltaTime;
        
        if (deltaX != 0) {
            moveOnX(deltaX);
        }
        if (deltaY != 0) {
            moveOnY(deltaY);
        }
        
        // Met à jour la direction du sprite selon le mouvement
        if (dx > 0) sprite.setDirection(PlayerSprite.Direction.RIGHT);
        else if (dx < 0) sprite.setDirection(PlayerSprite.Direction.LEFT);
        else if (dy < 0) sprite.setDirection(PlayerSprite.Direction.UP);
        else if (dy > 0) sprite.setDirection(PlayerSprite.Direction.DOWN);
    }
    
    /**
     * Gère le déplacement horizontal avec collisions
     */
    private void moveOnX(double deltaX) {
        double newX = x + deltaX;
        
        boolean canMove = true;
        if (deltaX > 0) {
            canMove = !tileMap.hasCollisionAt(newX + width, y) &&
                     !tileMap.hasCollisionAt(newX + width, y + height - 1);
        } else {
            canMove = !tileMap.hasCollisionAt(newX, y) &&
                     !tileMap.hasCollisionAt(newX, y + height);
        }
        
        if (canMove) {
            x = newX;
        }
    }
    
    /**
     * Gère le déplacement vertical avec collisions
     */
    private void moveOnY(double deltaY) {
        double newY = y + deltaY;
        
        boolean canMove = true;
        if (deltaY > 0) {
            canMove = !tileMap.hasCollisionAt(x, newY + height) &&
                     !tileMap.hasCollisionAt(x + width - 1, newY + height);
        } else {
            canMove = !tileMap.hasCollisionAt(x, newY) &&
                     !tileMap.hasCollisionAt(x + width - 1, newY);
        }
        
        if (canMove) {
            y = newY;
        }
    }
    
    /**
     * Dessine le sprite du joueur à l'écran
     */
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        if (sprite.getImage() != null) {
            gc.drawImage(
                sprite.getImage(),
                sprite.getViewport().getMinX(),
                sprite.getViewport().getMinY(),
                sprite.getViewport().getWidth(),
                sprite.getViewport().getHeight(),
                x - cameraX,
                y - cameraY,
                sprite.getFitWidth() * sprite.getScaleX(),
                sprite.getFitHeight() * sprite.getScaleY()
            );
        }
    }
    
    // Getters et setters
    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
    }
    
    public void updateAnimation(long now) {
        sprite.updateAnimation(now);
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public double getSpeed() {
        return speed;
    }

    public Stats getStats() {
        return stats;
    }

    public Inventory getInventory() {
        return inventory;
    }
}