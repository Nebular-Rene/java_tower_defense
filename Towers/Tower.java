import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

abstract class Tower {

    public Vector3d pos;

    Color outerColor;
    Color innerColor = Color.WHITE;
    String type;
    int cooldown = 0; // Cooldown timer for tower attacks

    public Tower(Vector3d pos, String type, Color color) {
        this.pos = pos;
        this.type = type;
        this.outerColor = color;
        
        this.cooldown = 0; // Initialize cooldown
    }

    // functions, all towers need:
    public abstract void shoot(ArrayList<Enemy> enemies, ArrayList<Bullet> bullets);

    public void Upgrade() {
        if (this.type == "Arrow") {
            this.type = "Cannon";
            this.outerColor = Color.DARK_GRAY;
            innerColor = Color.LIGHT_GRAY;
        }
        else if (this.type == "Cannon") {
            this.type = "Magic";
            this.outerColor = Color.BLUE;
            innerColor = Color.YELLOW;
        }
        else if (this.type == "Magic") {
            this.type = "Super";
            this.outerColor = Color.CYAN;
            innerColor = Color.BLACK;
        }
    }

    

    // not yet used
    private Vector3d getAimSpot(Enemy enemy, double bulletSpeed) {
        Vector3d totarget = this.pos.cpy().sub(enemy.pos);

        double a = enemy.movement.dot(enemy.movement) - (bulletSpeed * bulletSpeed);
        double b = 2 * enemy.movement.dot(totarget);
        double c = totarget.dot(totarget);

        double p = -b / (2 * a);
        double q = Math.sqrt((b * b) - 4 * a * c) / (2 * a);

        double t1 = p - q;
        double t2 = p + q;
        double t;

        if (t1 > t2 && t2 > 0)
        {
            t = t2;
        }
        else
        {
            t = t1;
        }

        return enemy.pos.cpy().add(enemy.movement).scl(t);
    }

    public void draw(Graphics g) {
        g.setColor(this.outerColor);
        int drawX = (int)pos.x;
        int drawY = (int)pos.y;
        g.fillRect(drawX, drawY, 40, 40);
        g.setColor(innerColor);
        g.fillOval(drawX + 10, drawY + 10, 20, 20);
    }
}