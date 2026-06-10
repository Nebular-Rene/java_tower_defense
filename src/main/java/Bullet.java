import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

class Bullet {
    Vector3d pos;
    Vector3d movement;
    float targetX, targetY;
    float speed;
    Color color;
    String type;
    double directionX, directionY;
    boolean hit = false;

    public Bullet(Vector3d pos, Vector3d destination, float speed, Color color, String type) {
        this.pos = pos.cpy();
        this.speed = speed;
        this.movement = destination.cpy().sub(this.pos).nor().scl(this.speed);
        this.color = color;
        this.type = type;
    }

    public boolean bulletInteraction(ArrayList<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            if (this.pos.dst(enemy.pos)<25) { // Collision radius of 25 pixels
                enemy.hit();
                this.hit = true;
                return true; // Remove the bullet
            }
        }
        // Remove the bullet if it goes off-screen
        if (pos.x <= 0)
            return true;
        if (pos.x >= 800)
            return true;
        if (pos.y <= 0)
            return true;
        if (pos.y >= 600)
            return true;
        // Keep the bullet if it didn't hit anything
        return false;
    }

    public void move() {
        pos.add(movement);
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int)pos.x, (int)pos.y, 8, 8);
    }

    public String toString() {
        return "Bullet[pos:"+pos.toString()+", move:"+movement.toString()+"]";
    }
}