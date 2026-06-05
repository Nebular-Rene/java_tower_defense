import java.awt.Color;
import java.awt.Graphics;

class Enemies {

    float x, y;
    float speed;
    Color color;
    boolean alive = true;
    float progress = 0; // Track the enemy's progress along the path
    int waypointIndex = 0; // Track the current waypoint index for movement

    static final int[][] WAYPOINTS = {
        {-20,  500},  // Start (links außerhalb)
        {220,  500},  // rechts laufen
        {220,   60},  // hoch
        {580,   60},  // rechts
        {580,  220},  // runter
        {460,  220},  // links
        {460,  420},  // runter
        {660,  420},  // rechts
        {660,  620},  // runter (außerhalb = Ziel)
    };

    public Enemies(Color color) {
        this.x = WAYPOINTS[0][0]; // Start x position
        this.y = WAYPOINTS[0][1]; // Start y position
        this.color = color;
        this.speed = setSpeed();
    }
    
    private float setSpeed() {
        if (color == Color.RED) {
            return 1.5f;
        } else if (color == Color.ORANGE) {
            return 1.8f;
        } else if (color == Color.YELLOW) {
            return 2.2f;
        } else if (color == Color.BLUE) {
            return 3f;
        }
        return 1f; // Default speed
    }

    public void move() {
        progress += speed; // Increment progress to determine movement along the path
        
        int targetX = WAYPOINTS[waypointIndex][0];
        int targetY = WAYPOINTS[waypointIndex][1];

        float distanceX = targetX - x;
        float distanceY = targetY - y;
        float distance = (float) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        if (distance < speed) {
            // Move to the next waypoint
            x = targetX;
            y = targetY;
            waypointIndex++;
            if (waypointIndex >= WAYPOINTS.length) {
                alive = false; // Enemy has reached the end of the path
            }
        } else {
            // Move towards the current waypoint
            x += (distanceX / distance) * speed;
            y += (distanceY / distance) * speed;
        }

    }

    public void hit() {
        // Handle what happens when the enemy is hit by a bullet (e.g., reduce health, check for death)
        if (color == Color.RED) {
            alive = false;          // Enemy is destroyed
        } else if (color == Color.ORANGE) {
            color = Color.RED;      // Further damage indication
        } else if (color == Color.YELLOW) {
            color = Color.ORANGE;   // Further damage indication
        } else if (color == Color.BLUE) {
            color = Color.YELLOW;   // Further damage indication
        }
        this.speed = setSpeed(); // Update speed based on new color (health) after being hit

    }

    public void draw(Graphics g) {
        g.setColor(color);
        int drawX = (int) x - 15;  // Center the circle at (x, y)
        int drawY = (int) y - 15;  // Center the circle at (x, y)
        g.fillOval(drawX, drawY, 30, 30);
    }
}