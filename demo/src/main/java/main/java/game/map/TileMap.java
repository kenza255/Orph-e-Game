package main.java.game.map;

import main.java.game.Camera;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;

/**
 * Représente la carte complète du jeu, composée de plusieurs couches de tuiles
 * Charge les données depuis un fichier TMX (format Tiled Map Editor)
 */
public class TileMap {
    private Image tileset;              // Image contenant toutes les tuiles
    private List<TileLayer> layers;     // Liste des couches de la carte
    private int width, height;          // Dimensions de la carte en nombre de tuiles
    private int tileWidth, tileHeight;  // Dimensions d'une tuile en pixels
    private static final double TILE_SCALE = 2.0;  // Facteur d'échelle pour l'affichage des tuiles

    /**
     * Charge une carte à partir d'un fichier TMX
     * @param mapPath Chemin vers le fichier TMX dans les ressources
     */
    public TileMap(String mapPath) {
        layers = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream(mapPath);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            Element mapElement = doc.getDocumentElement();
            
            width = Integer.parseInt(mapElement.getAttribute("width"));
            height = Integer.parseInt(mapElement.getAttribute("height"));
            tileWidth = Integer.parseInt(mapElement.getAttribute("tilewidth"));
            tileHeight = Integer.parseInt(mapElement.getAttribute("tileheight"));
            
            loadTileset(mapElement);

            // Charge chaque couche de la carte
            NodeList layerNodes = mapElement.getElementsByTagName("layer");
            for (int i = 0; i < layerNodes.getLength(); i++) {
                Element layerElement = (Element) layerNodes.item(i);
                String layerName = layerElement.getAttribute("name");
                
                int[][] tileData = new int[height][width];
                
                NodeList dataNodes = layerElement.getElementsByTagName("data");
                if (dataNodes.getLength() > 0) {
                    Element dataElement = (Element) dataNodes.item(0);
                    String[] tiles = dataElement.getTextContent().trim().split(",");
                    int index = 0;
                    
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            if (index < tiles.length) {
                                try {
                                    tileData[y][x] = Integer.parseInt(tiles[index].trim());
                                } catch (NumberFormatException e) {
                                    tileData[y][x] = 0;
                                }
                            }
                            index++;
                        }
                    }
                }
                
                layers.add(new TileLayer(layerName, tileData, 0, 0));
            }
            
        } catch (Exception e) {
            System.err.println("Error loading map: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Charge l'image du tileset à partir des données de la carte
     */
    private void loadTileset(Element mapElement) {
        try {
            NodeList tilesetNodes = mapElement.getElementsByTagName("tileset");
            if (tilesetNodes.getLength() > 0) {
                Element tilesetElement = (Element) tilesetNodes.item(0);
                String source = tilesetElement.getAttribute("source");
                String tsxPath = "/Maps/Assets/" + source.substring(source.lastIndexOf("/") + 1);
                
                InputStream tsxStream = getClass().getResourceAsStream(tsxPath);
                if (tsxStream == null) {
                    throw new RuntimeException("Cannot find tileset file: " + tsxPath);
                }

                Document tsxDoc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(tsxStream);
                Element tsxRoot = tsxDoc.getDocumentElement();

                NodeList imageNodes = tsxRoot.getElementsByTagName("image");
                if (imageNodes.getLength() > 0) {
                    Element imageElement = (Element) imageNodes.item(0);
                    String imagePath = "/Maps/Assets/" + imageElement.getAttribute("source");
                    
                    URL imageUrl = getClass().getResource(imagePath);
                    if (imageUrl == null) {
                        throw new RuntimeException("Cannot find tileset image: " + imagePath);
                    }
                    
                    tileset = new Image(imageUrl.toExternalForm());
                    System.out.println("Successfully loaded tileset: " + imagePath);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading tileset: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Dessine la carte à l'écran en tenant compte de la position de la caméra
     * Optimise le rendu en n'affichant que les tuiles visibles
     */
    public void render(GraphicsContext gc, Camera camera) {
        if (tileset == null) return;

        int startX = Math.max(0, (int)(camera.getX() / (tileWidth * TILE_SCALE)));
        int startY = Math.max(0, (int)(camera.getY() / (tileHeight * TILE_SCALE)));
        int endX = Math.min(width, startX + (int)(camera.getViewportWidth() / (tileWidth * TILE_SCALE)) + 2);
        int endY = Math.min(height, startY + (int)(camera.getViewportHeight() / (tileHeight * TILE_SCALE)) + 2);

        for (TileLayer layer : layers) {
            layer.render(gc, tileset, startX, startY, endX, endY, tileWidth, tileHeight, TILE_SCALE, camera);
        }
    }

    private void drawTile(GraphicsContext gc, int tileId, int x, int y, Camera camera) {
        int tilesPerRow = (int)(tileset.getWidth() / tileWidth);
        int sourceX = (tileId % tilesPerRow) * tileWidth;
        int sourceY = (tileId / tilesPerRow) * tileHeight;
        
        double destX = x * tileWidth * TILE_SCALE - camera.getX();
        double destY = y * tileHeight * TILE_SCALE - camera.getY();
        
        gc.drawImage(tileset, 
            sourceX, sourceY, tileWidth, tileHeight,
            destX, destY, tileWidth * TILE_SCALE, tileHeight * TILE_SCALE);
    }
    
    /**
     * Retourne la largeur totale de la carte en pixels
     */
    public double getTotalWidth() {
        return width * tileWidth * TILE_SCALE;
    }
    
    /**
     * Retourne la hauteur totale de la carte en pixels
     */
    public double getTotalHeight() {
        return height * tileHeight * TILE_SCALE;
    }

    /**
     * Vérifie s'il y a une collision à la position donnée
     * @return true s'il y a collision, false sinon
     */
    public boolean hasCollisionAt(double x, double y) {
        int tileX = (int)(x / (tileWidth * TILE_SCALE));
        int tileY = (int)(y / (tileHeight * TILE_SCALE));
        
        if (tileX < 0 || tileX >= width || tileY < 0 || tileY >= height) {
            return true;
        }
        
        for (TileLayer layer : layers) {
            if (layer.isCollision() && layer.hasCollisionAt(tileX, tileY)) {
                return true;
            }
        }
        return false;
    }
}