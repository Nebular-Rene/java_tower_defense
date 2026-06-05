import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class TowerDefense {

    private JFrame frame;
    private GamePanel panel;
    private int cooldown = 0; // Cooldown timer for tower attacks

    public void start() {
        panel = new GamePanel();

        frame = new JFrame("Tower Defense");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // ================= PANEL =================
    private class GamePanel extends JPanel {

        private ArrayList<Enemies> enemies = new ArrayList<>();
        private ArrayList<Tower> tower = new ArrayList<>();
        private ArrayList<Bullet> bullets = new ArrayList<>();
        private final int GRID_SIZE = 40; // Size of the grid cells
        private boolean[][] occupied; // Track occupied grid cells for tower placement
        private BufferedImage backgroundImage; // Cached static background
        private ArrayList<Rectangle> pathRects; // Path rectangles for occupancy and drawing

        public GamePanel() {
            setPreferredSize(new Dimension(800, 600));
            occupied = new boolean[800 / GRID_SIZE][600 / GRID_SIZE]; // Initialize the occupied grid
            pathRects = new ArrayList<>();
            initPathRects();
            createBackgroundImage();
            markPathOccupied();

            enemies.add(new Enemies(Color.RED));
            enemies.add(new Enemies(Color.ORANGE));
            enemies.add(new Enemies(Color.YELLOW));
            enemies.add(new Enemies(Color.BLUE));

            tower.add(new Tower(160, 120, Color.BLUE, Color.WHITE, "Arrow"));
            tower.add(new Tower(0, 0, Color.BLUE, Color.WHITE, "Arrow"));
            tower.add(new Tower(0, 40, Color.BLUE, Color.BLACK, "Cannon"));
            tower.add(new Tower(0, 80, Color.RED, Color.WHITE, "Magic"));
            tower.add(new Tower(0, 120, Color.RED, Color.BLACK, "Super"));


            Timer timer = new Timer(33, e -> {
                // enemy movement and cleanup
                for (Enemies Enemies : enemies) {
                    Enemies.move();
                }
                enemies.removeIf(enemy -> enemy.x >= 800 || enemy.y >= 600);

                // tower attack logic
                for (Tower Tower : tower) {
                    Tower.shoot(enemies, bullets);
                }
                
                // Move bullets
                for (Bullet Bullet : bullets) {
                    Bullet.move();
                }
                // Check for bullet-enemy collisions
                bullets.removeIf(bullet -> bullet.bulletInteraction(enemies));

                enemies.removeIf(enemy -> !enemy.alive);

                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.drawImage(backgroundImage, 0, 0, null);
            drawGrid(g);
            drawObjects(g);
        }

        private void initPathRects() {
            pathRects.add(new Rectangle(0, 480, 240, 40));
            pathRects.add(new Rectangle(200, 40, 40, 440));
            pathRects.add(new Rectangle(200, 40, 400, 40));
            pathRects.add(new Rectangle(560, 40, 40, 200));
            pathRects.add(new Rectangle(440, 200, 120, 40));
            pathRects.add(new Rectangle(440, 200, 40, 200));
            pathRects.add(new Rectangle(440, 400, 200, 40));
            pathRects.add(new Rectangle(640, 400, 40, 200));
        }

        private void createBackgroundImage() {
            backgroundImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = backgroundImage.createGraphics();
            drawBackground(g2d);
            g2d.dispose();
        }

        private void markPathOccupied() {
            for (int x = 0; x < 800 / GRID_SIZE; x++) {
                for (int y = 0; y < 600 / GRID_SIZE; y++) {
                    int centerX = x * GRID_SIZE + GRID_SIZE / 2;
                    int centerY = y * GRID_SIZE + GRID_SIZE / 2;
                    for (Rectangle rect : pathRects) {
                        if (rect.contains(centerX, centerY)) {
                            occupied[x][y] = true; // Mark cells on the path as occupied
                            break;
                        }
                    }
                }
            }
        }

        private void drawGrid(Graphics g) {
            g.setColor(new Color(0, 0, 0, 30)); // leicht transparent
            for (int x = 0; x < 800; x += GRID_SIZE) {
                g.drawLine(x, 0, x, 600);
            }
            for (int y = 0; y < 600; y += GRID_SIZE) {
                g.drawLine(0, y, 800, y);
            }
        }

        private void drawBackground(Graphics g) {

            g.setColor(new Color(34, 177, 76)); // Green
            g.fillRect(0, 0, getWidth(), getHeight());

            // Enemies path (x, y, width, height)
            g.setColor(Color.GRAY);
            g.fillRect(0, 480, 240, 40);      // bottom left to nearly center
            g.fillRect(200, 40, 40, 440);     // upwards path from the first part  
            g.fillRect(200, 40, 400, 40);     // horizontal path to the right
            g.fillRect(560, 40, 40, 200);     // vertical path down to the bottom right
            g.fillRect(440, 200, 120, 40);    // horizontal path to the left
            g.fillRect(440, 200, 40, 200);    // vertical path down to the bottom left
            g.fillRect(440, 400, 200, 40);    // horizontal path to the right
            g.fillRect(640, 400, 40, 200);    // vertical path down to the edge
        }

        private void drawObjects(Graphics g) {
            for (Tower Tower : tower) {
                Tower.draw(g);
            }

            for (Enemies Enemies : enemies) {
                Enemies.draw(g);
            }

            for (Bullet Bullet : bullets) {
                Bullet.draw(g);
            }
        }
    }
}