package main.java.game.ui;

import javafx.geometry.Rectangle2D;

/**
 * Définit les coordonnées des sprites de l'interface utilisateur dans la spritesheet
 * Ces rectangles sont utilisés pour découper et afficher les éléments graphiques
 */
public class GuiSprites {
    // Arrière-plan du menu principal (320x180 pixels)
    public static final Rectangle2D MENU_BACKGROUND = new Rectangle2D(0, 0, 320, 180);
    
    // État normal d'un bouton (200x40 pixels)
    public static final Rectangle2D BUTTON_NORMAL = new Rectangle2D(0, 180, 200, 40);
    
    // État survolé d'un bouton (200x40 pixels)
    public static final Rectangle2D BUTTON_HOVER = new Rectangle2D(200, 180, 200, 40);
}