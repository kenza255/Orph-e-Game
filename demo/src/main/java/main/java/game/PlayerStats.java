package main.java.game;

/**
 * Classe gérant les statistiques du joueur comme sa vie, ses pièces et ses actions
 */
public class PlayerStats {
    private int health;          // Points de vie actuels
    private int maxHealth;       // Points de vie maximum
    private int coins;           // Nombre de pièces collectées
    private boolean isBlocking;  // Si le joueur est en train de bloquer
    private long lastAttackTime; // Horodatage de la dernière attaque
    private static final long ATTACK_COOLDOWN = 500; // Délai entre deux attaques (en ms)

    /**
     * Initialise un nouveau joueur avec 5 points de vie et 0 pièces
     */
    public PlayerStats() {
        this.maxHealth = 5;
        this.health = maxHealth;
        this.coins = 0;
        this.isBlocking = false;
    }

    /**
     * Inflige des dégâts au joueur s'il ne bloque pas
     */
    public void takeDamage(int damage) {
        if (!isBlocking) {
            health = Math.max(0, health - damage);
        }
    }

    /**
     * Soigne le joueur sans dépasser sa vie maximum
     */
    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
    }

    /**
     * Ajoute des pièces à l'inventaire
     */
    public void addCoins(int amount) {
        coins += amount;
    }

    /**
     * Dépense des pièces si possible
     */
    public void spendCoins(int amount) {
        coins = Math.max(0, coins - amount);
    }

    /**
     * Vérifie si le joueur peut attaquer (délai écoulé)
     */
    public boolean canAttack() {
        return System.currentTimeMillis() - lastAttackTime >= ATTACK_COOLDOWN;
    }

    /**
     * Enregistre une nouvelle attaque
     */
    public void attack() {
        lastAttackTime = System.currentTimeMillis();
    }

    /**
     * Active/désactive le blocage
     */
    public void setBlocking(boolean blocking) {
        isBlocking = blocking;
    }

    // Getters
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getCoins() { return coins; }
    public boolean isBlocking() { return isBlocking; }
}