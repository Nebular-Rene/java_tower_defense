import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

class Bullet {
    int x, y;
    float targetX, targetY;
    float speed;
    Color color;
    String type;
    double directionX, directionY;

    public Bullet(int x, int y, float targetX, float targetY, float speed, Color color, String type) {
        this.x = x;
        this.y = y;
        this.targetX = targetX;
        this.targetY = targetY;
        this.speed = speed;
        this.color = color;
        this.type = type;

        double distanceX = targetX - x;
        double distanceY = targetY - y;
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        this.directionX = distanceX / distance;
        this.directionY = distanceY / distance;
    }

    public boolean bulletInteraction(ArrayList<Enemies> enemies) {
        for (Enemies Enemies : enemies) {
            float distanceX = (Enemies.x) - this.x; // Center of enemy - bullet position
            float distanceY = (Enemies.y) - this.y;
            if (distanceX * distanceX + distanceY * distanceY < 25 * 25) { // Collision radius of 25 pixels
                Enemies.hit();
                return true; // Remove the bullet
            }
        }
        if (targetX >= 800 || targetY >= 600) {
            return true; // Remove the bullet if it goes off-screen
        }
        return false; // Keep the bullet if it didn't hit anything
    }

    public void move() {
        x += directionX * speed;
        y += directionY * speed;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, 8, 8);
    }
}