import java.awt.Color;
import java.util.ArrayList;

public class MagicTower extends Tower {
    
    public MagicTower(Vector3d pos) {
        super(pos, "Magic", Color.BLUE, 115);
        this.bulletSpeed = 6.7f;
        this.innerColor = Color.WHITE;
        this.innerColorRGB = 250;
        this.bulletSize = 15;
        this.bulletHealth = 5;
        this.cooldownTime = 250;
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
            bullets.add(new Bullet(p, first.pos, this.bulletSpeed, Color.WHITE, this.bulletSize, this.bulletHealth));
            cooldown = this.cooldownTime; // Reset cooldown
        }
    }  

    @Override
    public boolean Upgrade() {
        if (this.innerColorRGB > 0) {
            this.innerColorRGB -= 25;
            this.cooldownTime -= 15;
            this.innerColor = new Color(innerColorRGB, innerColorRGB, innerColorRGB);
            this.bulletSpeed += 0.15f;
            this.bulletHealth += 1;
            this.range += 2;
            this.bulletSize += 1;
            return true;
        }
        return false;        
    }
}