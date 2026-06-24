import java.awt.Color;
import java.util.ArrayList;

public class ArrowTower extends Tower {

    public ArrowTower(Vector3d pos) {
        super(pos, "Arrow", Color.RED, 140);
        this.bulletSpeed = 10f;
        this.innerColor = Color.WHITE;
        this.innerColorRGB = 250;
        this.bulletSize = 7;
        this.bulletHealth = 1;
        this.cooldownTime = 42;        
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
            bullets.add(new Bullet(p, first.pos, this.bulletSpeed, Color.WHITE, 7, this.bulletHealth));
            cooldown = this.cooldownTime; // Reset cooldown
        }
    }  
    
    @Override
    public boolean Upgrade() {
        if (this.innerColorRGB > 0) {
            incLevel();
            this.innerColorRGB -= 25;
            this.cooldownTime -= 3;
            this.innerColor = new Color(innerColorRGB, innerColorRGB, innerColorRGB);
            this.bulletSpeed += 0.13f;
            this.range += 3;
            // one time at 5 upgrades
            if (getLevel() == 5) {
                this.bulletHealth += 1;
            }
            return true;
        }
        return false;        
    }
}