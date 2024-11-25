package main.java.game.inventory;

/**
 * Classe représentant l'inventaire du joueur
 * Gère le stockage et la manipulation des objets dans des emplacements
 */
public class Inventory {
    private final InventorySlot[] slots; // Tableau des emplacements d'inventaire
    private final int size; // Nombre total d'emplacements

    /**
     * Crée un inventaire avec le nombre d'emplacements spécifié
     */
    public Inventory(int size) {
        this.size = size;
        this.slots = new InventorySlot[size];
        for (int i = 0; i < size; i++) {
            slots[i] = new InventorySlot();
        }
    }

    /**
     * Ajoute un objet à l'inventaire
     * Cherche d'abord un emplacement avec le même type d'objet
     * Sinon utilise le premier emplacement vide
     */
    public boolean addItem(Item item, int amount) {
        for (InventorySlot slot : slots) {
            if (!slot.isEmpty() && slot.canAddItem(item, amount)) {
                return slot.addItem(item, amount);
            }
        }

        for (InventorySlot slot : slots) {
            if (slot.isEmpty()) {
                return slot.addItem(item, amount);
            }
        }
        return false;
    }

    /**
     * Retire une quantité donnée d'un objet de l'inventaire
     */
    public boolean removeItem(Item item, int quantity) {
        for (InventorySlot slot : slots) {
            if (!slot.isEmpty() && slot.getItem().equals(item)) {
                if (slot.getQuantity() >= quantity) {
                    slot.removeQuantity(quantity);
                    if (slot.getQuantity() <= 0) {
                        slot.clear();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Retourne l'emplacement à l'index spécifié
     */
    public InventorySlot getSlot(int index) {
        return slots[index];
    }

    /**
     * Retourne le nombre total d'emplacements
     */
    public int getSize() {
        return size;
    }
}
