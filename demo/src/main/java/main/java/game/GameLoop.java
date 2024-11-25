package main.java.game;

import javafx.animation.AnimationTimer;

/**
 * Classe abstraite qui gère la boucle de jeu principale
 * Permet de mettre à jour le jeu à intervalle régulier
 */
public abstract class GameLoop extends AnimationTimer {
    // Stocke le timestamp de la dernière mise à jour
    private long lastUpdate = 0;

    /**
     * Appelé automatiquement à chaque frame par JavaFX
     * Calcule le temps écoulé depuis la dernière mise à jour
     */
    @Override
    public void handle(long now) {
        if (lastUpdate == 0) {
            lastUpdate = now;
            return;
        }

        // Convertit la différence de temps en secondes
        double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
        lastUpdate = now;

        tick(deltaTime);
    }

    /**
     * Méthode abstraite à implémenter pour mettre à jour la logique du jeu
     * @param deltaTime Temps écoulé depuis la dernière mise à jour en secondes
     */
    public abstract void tick(double deltaTime);
}