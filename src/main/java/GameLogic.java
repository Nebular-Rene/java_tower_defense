import javax.swing.*;
import java.util.ArrayList;

public class GameLogic {

    public ArrayList<Enemies> enemies = new ArrayList<>();
    public ArrayList<Tower> tower = new ArrayList<>();
    public ArrayList<Bullet> bullets = new ArrayList<>();
    public int money = 0;

    private int towerPrice = 0;
    private GamePanel panel;

    // constructor
    public GameLogic(GamePanel panel) {
        this.panel = panel;

        enemies.add(new Enemies(java.awt.Color.RED));
        enemies.add(new Enemies(java.awt.Color.ORANGE));
        enemies.add(new Enemies(java.awt.Color.YELLOW));
        enemies.add(new Enemies(java.awt.Color.BLUE));

        tower.add(new Tower(160, 120, java.awt.Color.BLUE, java.awt.Color.WHITE, "Arrow"));
        tower.add(new Tower(240, 80, java.awt.Color.BLUE, java.awt.Color.WHITE, "Arrow"));
    }

    public void start() {
        Timer timer = new Timer(33, e -> {
            // Gegner bewegen
            for (Enemies enemy : enemies) {
                enemy.move();
            }
            enemies.removeIf(enemy -> enemy.x >= 800 || enemy.y >= 600);

            // Tower schießen
            for (Tower t : tower) {
                t.shoot(enemies, bullets);
            }

            // Bullets bewegen + Kollision
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
        timer.start();
    }

}