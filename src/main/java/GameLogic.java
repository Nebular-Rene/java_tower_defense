import javax.swing.*;
import java.util.ArrayList;
import java.awt.Color;

public class GameLogic {

    public ArrayList<Enemy> enemies = new ArrayList<>();
    public ArrayList<Tower> tower = new ArrayList<>();
    public ArrayList<Bullet> bullets = new ArrayList<>();
    public int money = 111110;
    public int towerPrice = 0;
    public Timer timer;

    private int currentLevel = 0;
    private int spawnCooldown = 0;
    private int[] currentLevelConfig = {0, 0, 0, 0};
    private int [][] levels = {
        {5, 0, 0, 0}, // red, orange, yellow, blue
        {9, 0, 0, 0},
        {5, 1, 0, 0},
        {8, 1, 0, 0},
        {14, 0, 0, 0},
        {12, 2, 0, 0},
        {0, 12, 0, 0},
        {9, 0, 0, 0},
        {10, 10, 0, 0},
        {12, 3, 1, 0},
        {8, 4, 1, 0},
        {18, 8, 0, 0},
        {10, 6, 2, 0},
        {22, 4, 1, 0},
        {0, 9, 3, 0},
        {0, 0, 0, 2},
        {15, 0, 0, 3},
        {5, 8, 4, 3},
        {1, 3, 6, 7},
        {0, 6, 7, 9},
        {6, 7, 6, 7},
        {6, 7, 67, 0},
        {0, 0, 0, 1},
        {0, 0, 1, 1},
        {0, 1, 1, 1},
        {1, 1, 1, 1},
        {0, 0, 0, 1000},

    };

    private GamePanel panel;

    // constructor
    public GameLogic(GamePanel panel) {
        this.panel = panel;
    }

    private boolean levelSpawned() {
        for (int i = 0; i < currentLevelConfig.length; i++) {
            if (currentLevelConfig[i] > 0) {
                return false;
            }
        }
        return true;
    }

    private void spawnLevel() {
        if (spawnCooldown > 0) {
            spawnCooldown--;
            return;
        }

        if (levelSpawned() && enemies.isEmpty()) {
            if (currentLevel >= levels.length) {
                GamePanel.gameOver = true;
                GamePanel.looseGame = false;
            }
            currentLevelConfig = levels[currentLevel % levels.length];
            currentLevel++;
        }
        if (currentLevelConfig[0] > 0) {
            enemies.add(new Enemy(Color.RED));
            currentLevelConfig[0]--;
            spawnCooldown = (400 / (currentLevel + 10)) + 10;
        } else if (currentLevelConfig[1] > 0) {
            enemies.add(new Enemy(Color.ORANGE));
            currentLevelConfig[1]--;
            spawnCooldown = (600 / (currentLevel + 10)) + 10;
        } else if (currentLevelConfig[2] > 0) {
            enemies.add(new Enemy(Color.YELLOW));
            currentLevelConfig[2]--;
            spawnCooldown = (800 / (currentLevel + 10)) + 10;
        } else if (currentLevelConfig[3] > 0) {
            enemies.add(new Enemy(Color.BLUE));
            currentLevelConfig[3]--;
            spawnCooldown = (1000 / (currentLevel + 10)) + 10;
        }

    }

    public void start() {
        timer = new Timer(33, e -> {
            spawnLevel();
            // Gegner bewegen
            for (Enemy enemy : enemies) {
                enemy.move();
            }

            // Tower shot
            for (Tower t : tower) {
                t.shoot(enemies, bullets);
            }

            // Bullets movement + collision
            for (int i = bullets.size() - 1; i >= 0; i--) {
                Bullet bullet = bullets.get(i);
                bullet.move();
                boolean endBullet = bullet.bulletInteraction(enemies);
                if (bullet.hit) {
                    money++;
                    enemies.removeIf(enemy -> !enemy.alive);
                }
                if (endBullet) {
                    bullets.remove(i);
                }
            }

            panel.repaint();
        });
    }

}