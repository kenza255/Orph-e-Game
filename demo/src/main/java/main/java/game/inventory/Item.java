package main.java.game.inventory;

import javafx.scene.image.Image;
import java.io.InputStream;

/**
 * Représente un objet du jeu avec ses propriétés
 * Chaque objet a un identifiant unique, un nom, une icône et une description
 * Les objets peuvent être empilables (stackable) jusqu'à une certaine quantité (maxStackSize)
 * Certains objets sont consommables (comme les potions)
 */
public class Item {
    private final String id;          // Identifiant unique de l'objet
    private final String name;        // Nom affiché de l'objet
    private final Image icon;         // Icône de l'objet affichée dans l'interface
    private final String description; // Description détaillée de l'objet
    private boolean stackable;        // Indique si l'objet peut être empilé
    private int maxStackSize;         // Nombre maximum d'objets dans une pile
    private boolean consumable;       // Indique si l'objet peut être consommé

    /**
     * Crée un nouvel objet avec ses propriétés
     * Charge l'icône depuis le chemin spécifié, utilise une image par défaut si non trouvée
     */
    public Item(String id, String name, String iconPath, String description, boolean stackable, int maxStackSize) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.stackable = stackable;
        this.maxStackSize = maxStackSize;
        this.consumable = id.equals("potion");

        InputStream imageStream = getClass().getResourceAsStream("/items/" + iconPath);
        if (imageStream != null) {
            this.icon = new Image(imageStream);
        } else {
            this.icon = new Image(getClass().getResourceAsStream("/sprites/player/Player.png"));
        }
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Image getIcon() { return icon; }
    public String getDescription() { return description; }
    public boolean isStackable() { return stackable; }
    public int getMaxStackSize() { return maxStackSize; }
    public boolean isConsumable() { return consumable; }
}
