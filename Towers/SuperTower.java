import java.awt.Color;
import java.util.ArrayList;

public class SuperTower extends Tower {
    
    public SuperTower(Vector3d pos) {
        super(pos, "Super", Color.GREEN, 100);
    }

    @Override
    public void shoot(ArrayList<Enemy> enemies, ArrayList<Bullet> bullets) {
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        Enemy first = null; // Track the first enemy in range
        for (Enemy enemy : enemies) {
            boolean inRange = pos.dst(enemy.pos) < 100;
            boolean firstEnemy = first == null || enemy.progress > first.progress;
            if (inRange && firstEnemy) {
                first = enemy;
            }
        }

        if (first != null) {
            Vector3d p = pos.cpy().add(20, 20,0);
            // Vector3d aim = getAimSpot(first, 6.7f);
            bullets.add(new Bullet(p, first.pos, 3f, Color.BLUE, 5, 1));
            cooldown = 10; // Reset cooldown
        }
    }  
}
