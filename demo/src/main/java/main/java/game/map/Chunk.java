package main.java.game.map;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une portion de la carte du jeu
 * Contient plusieurs couches de tuiles superposées (sol, décoration, etc.)
 */
public class Chunk {
    private List<TileLayer> layers;  // Liste des couches de tuiles
    private int x, y;                // Position du chunk dans la carte

    /**
     * Crée un nouveau chunk vide
     */
    public Chunk() {
        this.layers = new ArrayList<>();
    }

    /**
     * Retourne la liste des couches de tuiles
     */
    public List<TileLayer> getLayers() {
        return layers;
    }

    /**
     * Ajoute une nouvelle couche de tuiles au chunk
     */
    public void addLayer(TileLayer layer) {
        layers.add(layer);
    }
}