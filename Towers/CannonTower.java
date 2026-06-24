import java.awt.Color;
import java.util.ArrayList;

public class CannonTower extends Tower {
    
    public CannonTower(Vector3d pos) {
        super(pos, "Cannon", Color.LIGHT_GRAY, 167);
        this.bulletSpeed = 16.5f;
        this.innerColor = Color.WHITE;
        this.innerColorRGB = 250;
        this.bulletSize = 6;
        this.bulletHealth = 1;
        this.cooldownTime = 70;
    }

    @Override
    public void shoot(ArrayList<Enemy> enemies, ArrayList<Bullet> bullets) {
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        Enemy first = null; // Track the first enemy in range
        for (Enemy enemy : enemies) {
            boolean inRange = pos.dst(enemy.pos) < this.range;
            boolean firstEnemy = first == null || enemy.progress > first.progress;
            if (inRange && firstEnemy) {
                first = enemy;
            }
        }

        if (first != null) {
            Vector3d p = pos.cpy().add(20, 20,0);
            // Vector3d aim = getAimSpot(first, 6.7f);
            bullets.add(new Bullet(p, first.pos, this.bulletSpeed, Color.GRAY, 6, this.bulletHealth));
            cooldown = this.cooldownTime; // Reset cooldown
        }
    }

    @Override
    public boolean Upgrade() {
        if (this.innerColorRGB > 0) {
            this.innerColorRGB -= 25;
            this.cooldownTime -= 5;
            this.innerColor = new Color(innerColorRGB, innerColorRGB, innerColorRGB);
            this.bulletSpeed += 1f;
            this.range += 5;
            // hits 3 times (40, 28, 16)
            if (this.bulletSpeed % 4 == 0) {
                this.bulletHealth += 1;
            }
            return true;
        }
        return false;        
    }
}