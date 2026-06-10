import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GamePanel extends JPanel {

    public boolean playButton = false;

    private final int GRID_SIZE = 40;
    private boolean[][] occupied;
    private BufferedImage backgroundImage;
    private ArrayList<Rectangle> pathRects;
    private int buyPrice = 0;
    private boolean placeMode = false;

    private GameLogic logic;

    public GamePanel() {
        setLayout(null);
        setPreferredSize(new Dimension(800, 600));
        occupied = new boolean[800 / GRID_SIZE][600 / GRID_SIZE];
        pathRects = new ArrayList<>();
        initPathRects();
        createBackgroundImage();
        markPathOccupied();

        // region Buy Tower Button
        JButton buyTowerButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Color frameColor = (logic != null && logic.money >= logic.towerPrice)
                    ? new Color(50, 200, 100)
                    : new Color(255, 100, 0);
                setForeground(frameColor);
                
                setText("Buy Tower " + (logic != null ? logic.towerPrice : "?") + "$");

                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(20, 20, 20));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                g2d.setColor(frameColor);
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        buyTowerButton.setContentAreaFilled(false);
        buyTowerButton.setBorderPainted(false);
        buyTowerButton.setFont(new Font("Arial", Font.BOLD, 14));
        buyTowerButton.setFocusPainted(false);
        buyTowerButton.setBounds(645, 5, 150, 30);

        buyTowerButton.addActionListener(e -> {
            if (logic != null && logic.money >= logic.towerPrice) {
                placeMode = true;
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (placeMode) {
                    placeTower(e.getX(), e.getY());
                }
            }
        });

        this.add(buyTowerButton);
        // endregion

        // region Start/Stop Button
        JButton startStopButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // background
                g2d.setColor(new Color(20, 20, 20));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // frame
                Color frameColor = new Color(50, 200, 100);
                g2d.setColor(frameColor);
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                // form
                g2d.setColor(frameColor);
                if (playButton) {
                    // x, y, width, height, horizontal-, vertical- roundness of corners
                    g2d.fillRoundRect(6, 6, 24, 24, 8, 8);

                }
                else {
                    int[] xPoints = {7, 7, 30};
                    int[] yPoints = {7, 30, 18};
                    g2d.fillPolygon(xPoints, yPoints, 3);
                }
                


                g2d.dispose();
                // super.paintComponent(g);
            }
        };
        startStopButton.setContentAreaFilled(false);
        startStopButton.setBorderPainted(false);
        //startStopButton.setFont(new Font("Arial", Font.BOLD, 1));
        startStopButton.setFocusPainted(false);
        startStopButton.setBounds(2, 2, 36, 36);

        startStopButton.addActionListener(e -> {
            if (playButton) {
                playButton = false;
                logic.timer.stop();
            }
            else {
                playButton = true;
                logic.timer.start();
            }
        });

        this.add(startStopButton);
        // endregion
    }

    public void setLogic(GameLogic logic) {
        this.logic = logic;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, null);
        drawGrid(g);
        if (logic != null) {
            drawObjects(g);
        }

        // Show Money
        String moneyText = logic.money + "$";
        g.setFont(new Font("Arial", Font.BOLD, 16));
        int textWidth = g.getFontMetrics().stringWidth(moneyText);
        int textHeight = g.getFontMetrics().getAscent();
        int x = 780 - textWidth; // grows to the left
        int y = 40 + (GRID_SIZE - textHeight); // finds the center of the grid

        // background
        g.setColor(new Color(20, 20, 20, 180));
        // smoothen the edges
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // place number background
        ((Graphics2D) g).fillRoundRect(x - 10, y - textHeight, textWidth + 10 * 2, textHeight + 10, 12, 12);
        
        g.setColor(new Color(255, 200, 0)); //goldyellow
        g.drawString(moneyText, x, y);

    }

    private void drawObjects(Graphics g) {
        for (Tower tower : logic.tower) {
            tower.draw(g);
        }
        for (Enemies enemy : logic.enemies) {
            enemy.draw(g);
            }
        for (Bullet bullet : logic.bullets) {
            bullet.draw(g);
        }
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
                        occupied[x][y] = true;
                        break;
                    }
                }
            }
        }
    }

    private void drawGrid(Graphics g) {
        g.setColor(new Color(0, 0, 0, 30));
        for (int x = 0; x < 800; x += GRID_SIZE) {
            g.drawLine(x, 0, x, 600);
        }
        for (int y = 0; y < 600; y += GRID_SIZE) {
            g.drawLine(0, y, 800, y);
        }
    }

    private void drawBackground(Graphics g) {
        g.setColor(new Color(34, 177, 76));
        g.fillRect(0, 0, 800, 600);

        g.setColor(Color.GRAY);
        g.fillRect(0, 480, 240, 40);
        g.fillRect(200, 40, 40, 440);
        g.fillRect(200, 40, 400, 40);
        g.fillRect(560, 40, 40, 200);
        g.fillRect(440, 200, 120, 40);
        g.fillRect(440, 200, 40, 200);
        g.fillRect(440, 400, 200, 40);
        g.fillRect(640, 400, 40, 200);
    }

    private void placeTower(int mouseX, int mouseY) {
        int gridX = mouseX / GRID_SIZE;
        int gridY = mouseY / GRID_SIZE;

        if (gridX < 0 || gridX >= 800 || gridY < 0 || gridY >= 600) {
            return;
        }

        if (!occupied[gridX][gridY]) {
            int towerX = gridX * GRID_SIZE;
            int towerY = gridY * GRID_SIZE;

            logic.tower.add(new Tower(towerX, towerY, "Arrow"));
            occupied[gridX][gridY] = true;
            logic.money -= logic.towerPrice;
            logic.towerPrice += 20;
            placeMode = false;
            repaint();
            System.out.println("placed Tower!");
        }
        else {
            System.out.println("Denied Tower placement!");
        }
    }
}