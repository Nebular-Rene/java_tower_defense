import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TowerDefense extends JPanel implements ActionListener, MouseListener {

    private Timer timer;

    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Tower> towers = new ArrayList<>();
    private ArrayList<Bullet> bullets = new ArrayList<>();

    private int money = 100;
    private int spawnCounter = 0;

    public TowerDefense() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);

        addMouseListener(this);

        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        spawnCounter++;

        if (spawnCounter >= 120) {
            enemies.add(new Enemy());
            spawnCounter = 0;
        }

        for (Tower tower : towers) {
            tower.update(enemies, bullets);
        }

        for (Bullet bullet : bullets) {
            bullet.update();
        }

        for (Enemy enemy : enemies) {
            enemy.update();
        }

        checkCollisions();

        enemies.removeIf(enemy -> enemy.health <= 0 || enemy.x > 820);
        bullets.removeIf(bullet -> bullet.dead);

        repaint();
    }

    private void checkCollisions() {

        for (Bullet bullet : bullets) {

            for (Enemy enemy : enemies) {

                double dx = bullet.x - enemy.x;
                double dy = bullet.y - enemy.y;

                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance < 15) {

                    enemy.health -= 10;
                    bullet.dead = true;

                    if (enemy.health <= 0) {
                        money += 10;
                    }
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Pfad
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 280, 800, 40);

        // Türme
        g.setColor(Color.BLUE);
        for (Tower tower : towers) {
            g.fillRect(tower.x - 15, tower.y - 15, 30, 30);
        }

        // Gegner
        g.setColor(Color.RED);
        for (Enemy enemy : enemies) {
            g.fillOval((int) enemy.x - 10, (int) enemy.y - 10, 20, 20);
        }

        // Kugeln
        g.setColor(Color.BLACK);
        for (Bullet bullet : bullets) {
            g.fillOval((int) bullet.x - 3, (int) bullet.y - 3, 6, 6);
        }

        g.drawString("Geld: " + money, 20, 20);
        g.drawString("Turm kostet 25", 20, 40);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (money >= 25) {
            towers.add(new Tower(e.getX(), e.getY()));
            money -= 25;
        }
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {

        JFrame frame = new JFrame("Tower Defense");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new TowerDefense());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}



class Tower {

    int x;
    int y;

    int cooldown = 0;

    public Tower(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(ArrayList<Enemy> enemies,
                       ArrayList<Bullet> bullets) {

        cooldown--;

        Enemy target = null;

        for (Enemy enemy : enemies) {

            double dx = enemy.x - x;
            double dy = enemy.y - y;

            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance < 150) {
                target = enemy;
                break;
            }
        }

        if (target != null && cooldown <= 0) {

            bullets.add(new Bullet(
                    x,
                    y,
                    target.x,
                    target.y
            ));

            cooldown = 30;
        }
    }
}

class Bullet {

    double x;
    double y;

    double vx;
    double vy;

    boolean dead = false;

    public Bullet(double startX,
                  double startY,
                  double targetX,
                  double targetY) {

        x = startX;
        y = startY;

        double dx = targetX - startX;
        double dy = targetY - startY;

        double length = Math.sqrt(dx * dx + dy * dy);

        double speed = 5;

        vx = dx / length * speed;
        vy = dy / length * speed;
    }

    public void update() {

        x += vx;
        y += vy;

        if (x < 0 || x > 800 || y < 0 || y > 600) {
            dead = true;
        }
    }
}