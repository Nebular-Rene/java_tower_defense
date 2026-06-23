import java.awt.Color;
import java.util.ArrayList;

public class CannonTower extends Tower {
    
    public CannonTower(Vector3d pos) {
        super(pos, "Cannon", Color.LIGHT_GRAY, 167);
        this.bulletSpeed = 10f;
        this.innerColor = Color.WHITE;
        this.innerColorRGB = 250;
        this.bulletSize = 6;
        this.bulletHealth = 2;
        this.cooldownTime = 80;
    }

    @Override
    public void shoot(ArrayList<Enemy> enemies, ArrayList<Bullet> bullets) {
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        Enemy first = null; // Track the first enemy in range
        for (Enemy enemy : enemies) {
            boolean inRange = pos.dst(enemy.pos) < 167;
            boolean firstEnemy = first == null || enemy.progress > first.progress;
            if (inRange && firstEnemy) {
                first = enemy;
            }
        }

        if (first != null) {
            Vector3d p = pos.cpy().add(20, 20,0);
            // Vector3d aim = getAimSpot(first, 6.7f);
            bullets.add(new Bullet(p, first.pos, 10f, Color.GRAY, 6, 2));
            cooldown = this.cooldownTime; // Reset cooldown
        }
    }

    @Override
    public boolean Upgrade() {
        if (this.innerColorRGB > 0) {
            this.innerColorRGB -= 25;
            this.cooldownTime -= 4;
            this.innerColor = new Color(innerColorRGB, innerColorRGB, innerColorRGB);
            this.bulletSpeed += 0.2f;
            return true;
        }
        return false;        
    }
}