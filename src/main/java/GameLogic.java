import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.Color;
import java.util.Random;

public class GameLogic {

    public ArrayList<Enemy> enemies = new ArrayList<>();
    public ArrayList<Tower> tower = new ArrayList<>();
    public ArrayList<Bullet> bullets = new ArrayList<>();
    public int money = 0;
    public int towerPrice = 0;
    public int towerPriceArrow = 0;
    public int towerPriceCannon = 30;
    public int towerPriceMagic = 50;
    public int towerPriceSuper = 80;
    public Timer timer;

    private ArrayList<Color> spawnQueue = new ArrayList<>();
    private Random random = new Random();
    private int currentLevel = 0;
    private boolean increaseLevel = true;
    private int spawnCooldown = 0;
    private int[] currentLevelConfig = {0, 0, 0, 0};
    private int [][] levels = {
        // skip first level, cause i`m sloppy
        {}, // red, orange, yellow, blue
        {4, 0, 0, 0},
        {6, 0, 0, 0},
        {7, 0, 0, 0},
        {3, 1, 0, 0},
        {1, 2, 0, 0},
        {5, 1, 0, 0},
        {8, 1, 0, 0},
        {8, 1, 0, 0},
        {16, 0, 0, 0},
        {14, 0, 0, 0},
        {12, 2, 0, 0},
        {9, 3, 0, 0},
        {8, 4, 0, 0},
        {1, 12, 0, 0},
        {9, 0, 0, 0},
        {5, 5, 0, 0},
        {6, 7, 0, 0},
        {10, 10, 0, 0},
        {10, 10, 0, 0},
        {5, 3, 1, 0},
        {12, 3, 1, 0},
        {8, 4, 1, 0},
        {10, 5, 1, 0},
        {18, 8, 0, 0},
        {10, 3, 2, 0},
        {22, 4, 1, 0},
        {3, 9, 3, 0},
        {2, 0, 0, 2},
        {15, 0, 0, 3},
        {2, 2, 2, 2},
        {3, 3, 3, 3},
        {5, 8, 4, 3},
        {25, 9, 4, 0},
        {1, 3, 6, 7},
        {0, 6, 7, 9},
        {6, 7, 6, 7},
        {6, 7, 67, 1},
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

    private int calculateCooldown(Color c) {
        int base;
        if (c == Color.RED) {
            base = 300;
        } else if (c == Color.ORANGE) {
            base = 600;
        } else if (c == Color.YELLOW) {
            base = 900;
        } else { // blue
            base = 1200;
        }

        int randomNumber = random.nextInt(10); // 0-10
        int cooldown = Math.max(5, (base / (currentLevel + 10)) + randomNumber);

        return cooldown;
    }

    private void spawnLevel() {
        if (spawnCooldown > 0) {
            spawnCooldown--;
            return;
        }

        if (spawnQueue.isEmpty()) {
            if (increaseLevel) {
                if (!enemies.isEmpty()) {
                    return;
                }
                
                currentLevel++;

                if (currentLevel >= levels.length) {
                    GamePanel.gameOver = true;
                    GamePanel.looseGame = false;
                    return;
                }
            } else {
                int randomCooldownNumber = random.nextInt(10);
                spawnCooldown = (1000 / (currentLevel + 10)) + randomCooldownNumber;
            }
            increaseLevel = !increaseLevel;

            currentLevelConfig = levels[currentLevel % levels.length];

            spawnQueue.clear();

            Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.BLUE};
            for (int i = 0; i < currentLevelConfig.length; i++) {
                for (int j = 0; j < currentLevelConfig[i]; j++) {
                spawnQueue.add(colors[i]);
                }
            }
            Collections.shuffle(spawnQueue, random);

        } else {
            Color next = spawnQueue.remove(0);
            enemies.add(new Enemy(next));
            spawnCooldown = calculateCooldown(next);
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