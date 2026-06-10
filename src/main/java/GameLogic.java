import javax.swing.*;
import java.util.ArrayList;
import java.awt.Color;

public class GameLogic {

    public ArrayList<Enemies> enemies = new ArrayList<>();
    public ArrayList<Tower> tower = new ArrayList<>();
    public ArrayList<Bullet> bullets = new ArrayList<>();
    public int money = 0;
    public int towerPrice = 0;
    public Timer timer;

    private int currentLevel = 0;
    private int spawnCooldown = 0;
    private int[] currentLevelConfig = {0, 0, 0, 0};
    private int [][] levels = {
        {6, 1, 0, 0},
        {10, 4, 0, 0},
        {12, 9, 4, 0},
        {20, 6, 3, 2},
        {0, 9, 4, 9},
        {0, 0, 10, 20},
        {0, 0, 0, 30},

        {6, 1, 0, 0},
        {10, 4, 0, 0},
        {12, 9, 4, 0},
        {20, 6, 3, 2},
        {0, 9, 4, 9},
        {0, 0, 10, 20},
        {0, 0, 0, 30},
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
            currentLevelConfig = levels[currentLevel % levels.length];
            currentLevel++;
        }
        if (currentLevelConfig[0] > 0) {
            enemies.add(new Enemies(Color.RED));
            currentLevelConfig[0]--;
            spawnCooldown = (40 / currentLevel) + 5;
        } else if (currentLevelConfig[1] > 0) {
            enemies.add(new Enemies(Color.ORANGE));
            currentLevelConfig[1]--;
            spawnCooldown = (80 / currentLevel) + 10;
        } else if (currentLevelConfig[2] > 0) {
            enemies.add(new Enemies(Color.YELLOW));
            currentLevelConfig[2]--;
            spawnCooldown = (120 / currentLevel) + 15;
        } else if (currentLevelConfig[3] > 0) {
            enemies.add(new Enemies(Color.BLUE));
            currentLevelConfig[3]--;
            spawnCooldown = (180 / currentLevel) + 20;
        }

    }

    public void start() {
        timer = new Timer(33, e -> {
            spawnLevel();
            // move Enemy
            for (Enemies enemy : enemies) {
                enemy.move();
            }
            enemies.removeIf(enemy -> enemy.x >= 800 || enemy.y >= 600);

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