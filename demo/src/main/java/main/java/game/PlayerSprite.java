package main.java.game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import java.net.URL;

/**
 * Gère le sprite animé du joueur et ses animations de déplacement
 * Utilise une spritesheet avec différentes animations selon la direction
 */
public class PlayerSprite extends ImageView {
    private static final int SPRITE_WIDTH = 32;    // Largeur d'une frame dans la spritesheet
    private static final int SPRITE_HEIGHT = 32;   // Hauteur d'une frame dans la spritesheet
    private static final int SCALE = 2;            // Facteur d'agrandissement du sprite
    
    private int currentFrame = 0;                  // Frame actuelle de l'animation
    private int frameCount = 6;                    // Nombre total de frames par animation
    private long lastFrameTime = 0;                // Timestamp de la dernière frame
    private static final long FRAME_DURATION = 200_000_000;  // Durée d'affichage d'une frame en nanosecondes
    
    /**
     * Directions possibles du joueur avec leur ligne correspondante dans la spritesheet
     */
    public enum Direction {
        UP(2),
        RIGHT(1),
        DOWN(0),
        LEFT(1);   
        
        private final int row;
        Direction(int row) { this.row = row; }
        public int getRow() { return row; }
    }
    
    private Direction currentDirection = Direction.DOWN;
    
    /**
     * Initialise le sprite en chargeant la spritesheet et configure l'affichage
     */
    public PlayerSprite() {
        try {
            URL imageUrl = getClass().getResource("/sprites/player/Player.png");
            
            if (imageUrl != null) {
                Image spritesheet = new Image(imageUrl.toExternalForm());
                setImage(spritesheet);
                
                setFitWidth(SPRITE_WIDTH);
                setFitHeight(SPRITE_HEIGHT);
                
                setScaleX(SCALE);
                setScaleY(SCALE);
                
                setSmooth(false);
                setPreserveRatio(true);
                
                updateViewport();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Change la direction du sprite et inverse l'image si nécessaire (pour la gauche)
     */
    public void setDirection(Direction direction) {
        if (currentDirection != direction) {
            currentDirection = direction;
            currentFrame = 0;
            
            if (direction == Direction.LEFT) {
                setScaleX(-1 * SCALE); 
            } else {
                setScaleX(SCALE); 
            }
            
            updateViewport();
        }
    }
    
    /**
     * Met à jour l'animation en passant à la frame suivante si assez de temps s'est écoulé
     */
    public void updateAnimation(long now) {
        if (now - lastFrameTime >= FRAME_DURATION) {
            currentFrame = (currentFrame + 1) % frameCount;
            updateViewport();
            lastFrameTime = now;
        }
    }
    
    /**
     * Met à jour la portion visible de la spritesheet selon la frame et direction actuelles
     */
    private void updateViewport() {
        if (getImage() != null) {
            Rectangle2D viewport = new Rectangle2D(
                currentFrame * SPRITE_WIDTH,
                currentDirection.getRow() * SPRITE_HEIGHT,
                SPRITE_WIDTH,
                SPRITE_HEIGHT
            );
            setViewport(viewport);
        }
    }
}