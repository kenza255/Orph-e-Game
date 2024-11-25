package main.java.game;

/**
 * Gère les statistiques du joueur comme sa vie, ses pièces et ses actions de combat
 */
public class Stats {
    private int health;          // Points de vie actuels
    private int maxHealth;       // Points de vie maximum
    private int coins;           // Monnaie du jeu
    private boolean isBlocking;  // Indique si le joueur est en position de défense
    private long lastAttackTime = 0;  // Horodatage de la dernière attaque
    private static final long ATTACK_COOLDOWN = 1000;  // Délai entre deux attaques (en ms)
    
    /**
     * Crée des stats par défaut avec 5 PV et 0 pièces
     */
    public Stats() {
        this.maxHealth = 5;
        this.health = maxHealth;
        this.coins = 0;
        this.isBlocking = false;
    }
    
    /**
     * Inflige des dégâts au joueur (réduits de moitié si en défense)
     */
    public void takeDamage(int damage) {
        if (isBlocking) {
            damage = Math.max(1, damage / 2); 
        }
        health = Math.max(0, health - damage);
    }
    
    /**
     * Soigne le joueur sans dépasser sa vie maximum
     */
    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
    }
    
    public void addCoins(int amount) {
        coins += amount;
    }
    
    /**
     * Tente de dépenser des pièces. Retourne true si l'achat est possible
     */
    public boolean spendCoins(int amount) {
        if (coins >= amount) {
            coins -= amount;
            return true;
        }
        return false;
    }
    
    public void setBlocking(boolean blocking) {
        this.isBlocking = blocking;
    }
    
    /**
     * Vérifie si le délai entre deux attaques est écoulé
     */
    public boolean canAttack() {
        long currentTime = System.currentTimeMillis();
        return currentTime - lastAttackTime >= ATTACK_COOLDOWN;
    }

    public void attack() {
        lastAttackTime = System.currentTimeMillis();
    }
    
    // Getters
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getCoins() { return coins; }
    public boolean isBlocking() { return isBlocking; }
    
    // Setters
    public void setHealth(int health) { 
        this.health = Math.min(maxHealth, Math.max(0, health)); 
    }
    public void setMaxHealth(int maxHealth) { 
        this.maxHealth = maxHealth; 
        this.health = Math.min(this.health, maxHealth);
    }
}