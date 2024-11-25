package main.java.game.inventory;

/**
 * Représente un emplacement dans l'inventaire qui peut contenir un objet et sa quantité
 */
public class InventorySlot {
    private Item item;        // L'objet stocké dans cet emplacement
    private int quantity;     // La quantité de cet objet

    /**
     * Crée un emplacement d'inventaire vide
     */
    public InventorySlot() {
        this.item = null;
        this.quantity = 0;
    }

    /**
     * Vérifie si l'emplacement est vide
     */
    public boolean isEmpty() {
        return item == null;
    }

    /**
     * Vérifie si on peut ajouter une certaine quantité d'un objet
     * @return true si l'objet peut être ajouté (emplacement vide ou même objet empilable)
     */
    public boolean canAddItem(Item item, int amount) {
        if (isEmpty()) return true;
        if (this.item.getId().equals(item.getId()) && this.item.isStackable()) {
            return quantity + amount <= this.item.getMaxStackSize();
        }
        return false;
    }

    /**
     * Tente d'ajouter une quantité d'un objet dans cet emplacement
     * @return true si l'ajout a réussi
     */
    public boolean addItem(Item item, int amount) {
        if (isEmpty()) {
            this.item = item;
            this.quantity = amount;
            return true;
        }
        if (this.item.getId().equals(item.getId()) && canAddItem(item, amount)) {
            this.quantity += amount;
            return true;
        }
        return false;
    }

    public Item getItem() { return item; }
    public int getQuantity() { return quantity; }

    /**
     * Retire une quantité de l'objet (sans descendre sous 0)
     */
    public void removeQuantity(int amount) {
        quantity = Math.max(0, quantity - amount);
    }

    /**
     * Vide complètement l'emplacement
     */
    public void clear() {
        item = null;
        quantity = 0;
    }
}
