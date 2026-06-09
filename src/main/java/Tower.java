import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

class Tower {

    int x, y;
    Color outerColor = Color.RED;
    Color innerColor = Color.PINK;
    String type;
    int cooldown = 0; // Cooldown timer for tower attacks

    public Tower(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
        
        this.cooldown = 0; // Initialize cooldown
    }

    public void Upgrade() {
        if (this.type == "Arrow") {
            this.type = "Cannon";
            outerColor = Color.DARK_GRAY;
            innerColor = Color.LIGHT_GRAY;
        }
        else if (this.type == "Cannon") {
            this.type = "Magic";
            outerColor = Color.BLUE;
            innerColor = Color.YELLOW;
        }
        else if (this.type == "Magic") {
            this.type = "Super";
            outerColor = Color.CYAN;
            innerColor = Color.BLACK;
        }
    }

    public void shoot(ArrayList<Enemies> enemies, ArrayList<Bullet> bullets) {
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        Enemies first = null; // Track the first enemy in range
        for (Enemies enemy : enemies) {
            float distanceX = enemy.x - (this.x + 20); // Center of enemy - center of tower
            float distanceY = enemy.y - (this.y + 20);
            boolean inRange = distanceX * distanceX + distanceY * distanceY < 150 * 150;
            boolean firstEnemy = first == null || enemy.progress > first.progress;
            if (inRange && firstEnemy) {
                first = enemy;
            }
        }

        if (first != null) {
            bullets.add(new Bullet(this.x + 15, this.y + 15, first.x, first.y, 6.7f, Color.RED, this.type));
            cooldown = 40; // Reset cooldown
        }
    }

    public void draw(Graphics g) {
        g.setColor(outerColor);
        int drawX = x;
        int drawY = y;
        g.fillRect(drawX, drawY, 40, 40);
        g.setColor(innerColor);
        g.fillOval(drawX + 10, drawY + 10, 20, 20);
    }
}