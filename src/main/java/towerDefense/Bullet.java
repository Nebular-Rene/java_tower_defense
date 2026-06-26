package towerDefense;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Bullet {
    Vector3d pos;
    Vector3d movement;
    float targetX, targetY;
    float speed;
    Color color;
    int size;
    int health;
    double directionX, directionY;
    boolean hit = false;
    Enemy lastHitEnemy = null;

    public Bullet(Vector3d pos, Vector3d destination, float speed, Color color, int size, int health) {
        this.pos = pos.cpy();
        this.speed = speed;
        this.movement = destination.cpy().sub(this.pos).nor().scl(this.speed);
        this.color = color;
        this.size = size;
        this.health = health;
    }

    public boolean bulletInteraction(ArrayList<Enemy> enemies) {
        this.hit = false;

        for (Enemy enemy : enemies) {
            if (this.pos.dst(enemy.pos) < (15 + (this.size / 2))) { // Collision radius of 25 pixels
                if (enemy == lastHitEnemy) {
                    return false; // Already registered this collision
                }

                lastHitEnemy = enemy;
                enemy.hit();
                this.hit = true;

                if (this.health < 2) {
                    return true; // Remove the bullet
                } else {
                    this.health--;
                    return false;
                }
            }
        }

        lastHitEnemy = null;
        // Remove the bullet if it goes off-screen
        if (pos.x <= -10)
            return true;
        if (pos.x >= 810)
            return true;
        if (pos.y <= -10)
            return true;
        if (pos.y >= 610)
            return true;
        // Keep the bullet if it didn't hit anything
        return false;
    }

    public void move() {
        pos.add(movement);
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int)pos.x - (this.size / 2), (int)pos.y - (this.size / 2), this.size, this.size);
        g.setColor(Color.BLACK);
        g.drawRoundRect((int)pos.x - (this.size / 2), (int)pos.y - (this.size / 2), this.size, this.size, 20, 20);
    }

    public String toString() {
        return "Bullet[pos:"+pos.toString()+", move:"+movement.toString()+"]";
    }
}