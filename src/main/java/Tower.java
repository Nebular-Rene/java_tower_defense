import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

class Tower {

    int x, y;
    Color outerColor;
    Color innerColor;
    String type;
    int cooldown = 0; // Cooldown timer for tower attacks

    public Tower(int x, int y, Color outerColor, Color innerColor, String type) {
        this.x = x;
        this.y = y;
        this.outerColor = outerColor;
        this.innerColor = innerColor;
        this.type = type;
        this.cooldown = 0; // Initialize cooldown
    }

    public void Upgrade() {
        if (this.type == "Arrow") {
            this.type = "Cannon";
        }
        else if (this.type == "Cannon") {
            this.type = "Magic";
        }
        else if (this.type == "Magic") {
            this.type = "Super";
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
            bullets.add(new Bullet(this.x + 20, this.y + 20, first.x, first.y, 6.7f, Color.RED, this.type));
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