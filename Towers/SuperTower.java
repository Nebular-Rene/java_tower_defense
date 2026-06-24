import java.awt.Color;
import java.util.ArrayList;

public class SuperTower extends Tower {
    
    public SuperTower(Vector3d pos) {
        super(pos, "Super", Color.GREEN, 100);
        this.bulletSpeed = 4.5f;
        this.innerColor = Color.WHITE;
        this.innerColorRGB = 250;
        this.bulletSize = 5;
        this.bulletHealth = 1;
        this.cooldownTime = 15;
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
            bullets.add(new Bullet(p, first.pos, this.bulletSpeed, Color.BLUE, this.bulletSize, 1));
            cooldown = this.cooldownTime; // Reset cooldown
        }
    }  

    @Override
    public boolean Upgrade() {
        if (this.innerColorRGB > 0) {
            incLevel();
            this.innerColorRGB -= 25;
            this.cooldownTime -= 1;
            this.innerColor = new Color(innerColorRGB, innerColorRGB, innerColorRGB);
            this.bulletSpeed += 0.35f;
            this.range += 1;
            // 4 times (0, 3, 6, 9)
            if ((getLevel() % 3) == 0) {
                this.bulletSize += 1;
                this.range += 1;
            }
            return true;
        }
        return false;        
    }
}