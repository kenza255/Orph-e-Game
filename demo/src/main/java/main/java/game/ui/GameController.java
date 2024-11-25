package main.java.game.ui;

import javafx.fxml.FXML;
import main.java.game.Merchant;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import main.java.game.GameLoop;
import main.java.game.Player;
import main.java.game.map.TileMap;
import main.java.game.Camera;
import main.java.game.GameState;
import main.java.game.inventory.ui.InventoryUI;
import main.java.game.inventory.Item;
import main.java.game.WorldItem;
import main.java.game.Enemy;
import main.java.game.shop.ShopUI;
import main.java.game.quest.ui.QuestUI;
import main.java.game.quest.Quest;
import main.java.game.quest.OrpheusQuest;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import java.util.ArrayList;
import javafx.geometry.VPos;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;
import java.util.List;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import java.io.*;

/**
 * Contrôleur principal du jeu qui gère :
 * - Le rendu graphique
 * - Les interactions utilisateur (clavier/souris)
 * - La logique de jeu (collisions, combat, etc.)
 * - Les différentes interfaces (inventaire, boutique, quêtes)
 */
public class GameController {
    
    // Éléments de l'interface graphique
    @FXML private Canvas gameCanvas;
    @FXML private StackPane gameRoot;
    @FXML private QuestUI questController;
    
    // Éléments principaux du jeu
    private TileMap tileMap;            // La carte du jeu
    private Player player;              // Le joueur
    private GraphicsContext gc;         // Contexte graphique pour dessiner
    private GameLoop gameLoop;          // Boucle principale du jeu
    private boolean[] keys = new boolean[4];  // État des touches directionnelles
    private Camera camera;              // Caméra qui suit le joueur
    private long lastUpdateTime = System.nanoTime();
    
    // Gestion des interfaces
    private InventoryUI inventoryUI;
    private Parent inventoryRoot;
    private boolean inventoryOpen = false;
    
    // Éléments du monde
    private List<WorldItem> worldItems = new ArrayList<>();  // Objets au sol
    private List<Enemy> enemies = new ArrayList<>();         // Ennemis
    private Merchant merchant;                               // Marchand
    
    // Interface de la boutique
    private Parent shopRoot;
    private ShopUI shopUI;
    private boolean shopOpen = false;
    
    // Interface des quêtes
    private Parent questRoot;
    private QuestUI questUI;
    private boolean questOpen = false;
    private Quest orpheusQuest;
    
    // Gestion de la mort du joueur
    private boolean isDead = false;
    private long deathAnimationStartTime;
    private static final double DEATH_ANIMATION_DURATION = 3.0;

    /**
     * Initialise tous les éléments du jeu :
     * - Charge la carte
     * - Crée le joueur et les ennemis
     * - Configure les interfaces
     * - Démarre la boucle de jeu
     */
    @FXML
    public void initialize() {
        gc = gameCanvas.getGraphicsContext2D();
        tileMap = new TileMap("/Maps/Map_debut.tmx");
        
        gameCanvas.setWidth(960);
        gameCanvas.setHeight(640);
        
        camera = new Camera(gameCanvas.getWidth(), gameCanvas.getHeight());
        camera.setBounds(tileMap.getTotalWidth(), tileMap.getTotalHeight());
        
        player = new Player();
        player.setTileMap(tileMap);
        
        Item testItem = new Item("test", "Test", null, "Un item de test", false, 1);
        WorldItem worldItem = new WorldItem(500, 300, testItem);
        worldItems.add(worldItem);
        
        enemies.add(new Enemy(600, 300));
        merchant = new Merchant(400, 400);
        
        if (questController != null) {
            questController.setPlayer(player);
        }
        
        camera.centerOn(player.getX() + player.width/2, player.getY() + player.height/2);
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/inventory.fxml"));
            inventoryRoot = loader.load();
            inventoryUI = loader.getController();
            inventoryUI.setInventory(player.getInventory());
            inventoryRoot.setVisible(false);
            gameRoot.getChildren().add(inventoryRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/shop.fxml"));
            shopRoot = loader.load();
            shopUI = loader.getController();
            shopUI.setPlayer(player);
            shopRoot.setVisible(false);
            gameRoot.getChildren().add(shopRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }

        orpheusQuest = new OrpheusQuest();
        try {
            FXMLLoader questLoader = new FXMLLoader(getClass().getResource("/fxml/quest.fxml"));
            questRoot = questLoader.load();
            questUI = questLoader.getController();
            questUI.setPlayer(player);
            questUI.setQuest(orpheusQuest);
            questRoot.setVisible(false);
            gameRoot.getChildren().add(questRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        gameCanvas.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                setupKeyHandlers(newScene);
            }
        });
        
        gameCanvas.setOnMousePressed(this::handleMousePress);
        gameCanvas.setOnMouseReleased(this::handleMouseRelease);
        
        gameLoop = new GameLoop() {
            @Override
            public void tick(double deltaTime) {
                update();
            }
        };
        gameLoop.start();
    }

    /**
     * Configure les contrôles clavier du jeu
     */
    private void setupKeyHandlers(Scene scene) {
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP:    keys[0] = true; break;
                case DOWN:  keys[1] = true; break;
                case LEFT:  keys[2] = true; break;
                case RIGHT: keys[3] = true; break;
                case E:     toggleInventory(); break;
                case A:     handleItemPickup(); break;
                case T:     
                    if (merchant.isNearPlayer(player)) {
                        toggleShop();
                    }
                    break;
                case Q:     toggleQuest(); break;
                default: break;
            }
        });

        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case UP:    keys[0] = false; break;
                case DOWN:  keys[1] = false; break;
                case LEFT:  keys[2] = false; break;
                case RIGHT: keys[3] = false; break;
                default: break;
            }
        });
    }

    /**
     * Gère les attaques et le blocage avec la souris
     */
    private void handleMousePress(MouseEvent e) {
        if (e.isPrimaryButtonDown()) {
            // Attaque
            for (Enemy enemy : enemies) {
                if (enemy.isNearPlayer(player) && player.getStats().canAttack()) {
                    enemy.takeDamage(player);
                    player.getStats().attack();
                }
            }
        } else if (e.isSecondaryButtonDown()) {
            // Blocage
            player.getStats().setBlocking(true);
        }
    }

    private void handleMouseRelease(MouseEvent e) {
        if (e.getButton() == MouseButton.SECONDARY) {
            player.getStats().setBlocking(false);
        }
    }

    /**
     * Permet au joueur de ramasser un objet proche
     */
    private void handleItemPickup() {
        if (!inventoryOpen) {
            for (WorldItem worldItem : new ArrayList<>(worldItems)) {
                if (worldItem.isNearPlayer(player)) {
                    if (inventoryUI.getInventory().addItem(worldItem.getItem(), 1)) {
                        worldItems.remove(worldItem);
                        System.out.println("Item récupéré : " + worldItem.getItem().getName());
                    }
                }
            }
        }
    }

    private void toggleInventory() {
        inventoryOpen = !inventoryOpen;
        if (inventoryRoot != null) {
            inventoryRoot.setVisible(inventoryOpen);
            if (inventoryOpen) {
                inventoryUI.updateUI();
            }
        }
    }

    private void toggleShop() {
        shopOpen = !shopOpen;
        if (shopRoot != null) {
            shopRoot.setVisible(shopOpen);
            if (shopOpen) {
                shopUI.updateUI();
            }
        }
    }

    private void toggleQuest() {
        questOpen = !questOpen;
        if (questRoot != null) {
            questRoot.setVisible(questOpen);
        }
    }

    /**
     * Mise à jour principale du jeu :
     * - Mouvements du joueur
     * - Comportement des ennemis
     * - Gestion des collisions
     * - Rendu graphique
     */
    private void update() {
        if (!inventoryOpen && !shopOpen && !questOpen) {
            long currentTime = System.nanoTime();
            double deltaTime = (currentTime - lastUpdateTime) / 1_000_000_000.0;
            lastUpdateTime = currentTime;

            if (player.getStats().getHealth() <= 0 && !isDead) {
                handlePlayerDeath(currentTime);
                return;
            }

            if (isDead) {
                handleDeathAnimation(currentTime);
                return;
            }

            updatePlayerMovement(deltaTime);
            updateEnemies(deltaTime);
            updateCamera();
            checkCollisions();
            render();
        }
    }

    private void handlePlayerDeath(long currentTime) {
        isDead = true;
        deathAnimationStartTime = currentTime;
        for (int i = 0; i < keys.length; i++) {
            keys[i] = false;
        }
    }

    private void handleDeathAnimation(long currentTime) {
        double elapsedTime = (currentTime - deathAnimationStartTime) / 1_000_000_000.0;
        
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        
        gc.setFill(Color.RED);
        gc.setFont(new Font("Times New Roman", 72));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("VOUS ÊTES MORT", 
                   gameCanvas.getWidth() / 2, 
                   gameCanvas.getHeight() / 2);
        
        if (elapsedTime > DEATH_ANIMATION_DURATION) {
            resetGame();
        }
    }

    private void resetGame() {
        isDead = false;
        player.getStats().setHealth(player.getStats().getMaxHealth());
        player.setX(100);
        player.setY(100);
        camera.centerOn(player.getX() + player.width/2, player.getY() + player.height/2);
    }

    public void loadSaveGame() throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save/game.sav"))) {
            GameState gameState = (GameState) ois.readObject();
            if (gameState != null) {
                player.setX(gameState.getPlayerX());
                player.setY(gameState.getPlayerY());
            }
        } catch (ClassNotFoundException e) {
            throw new IOException("Error loading save game", e);
        }
    }

    private void updatePlayerMovement(double deltaTime) {
        if (keys[0]) player.moveUp();
        if (keys[1]) player.moveDown();
        if (keys[2]) player.moveLeft();
        if (keys[3]) player.moveRight();
        
        if (!keys[0] && !keys[1]) player.stopVertical();
        if (!keys[2] && !keys[3]) player.stopHorizontal();
        
        player.update(deltaTime);
        player.updateAnimation(System.nanoTime());
    }

    private void updateEnemies(double deltaTime) {
        for (Enemy enemy : enemies) {
            enemy.update(deltaTime);
            if (enemy.isNearPlayer(player)) {
                enemy.dealDamage(player); 
            }
        }
    }

    private void updateCamera() {
        camera.centerOn(player.getX() + player.width/2, player.getY() + player.height/2);
    }

    private void checkCollisions() {
        for (Enemy enemy : enemies) {
            if (enemy.isNearPlayer(player)) {
            }
        }
    }

    /**
     * Dessine tous les éléments du jeu :
     * - La carte
     * - Les objets au sol
     * - Les ennemis
     * - Le marchand
     * - Le joueur
     * - L'interface utilisateur
     */
    private void render() {
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        
        tileMap.render(gc, camera);
        
        for (WorldItem item : worldItems) {
            item.render(gc, camera.getX(), camera.getY());
        }
        
        for (Enemy enemy : enemies) {
            enemy.render(gc, camera.getX(), camera.getY());
        }
        
        merchant.render(gc, camera.getX(), camera.getY());
        
        player.render(gc, camera.getX(), camera.getY());
        
        UIRenderer.renderUI(gc, player);
    }
}
