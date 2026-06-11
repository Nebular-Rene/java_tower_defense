import java.awt.Color;
import java.awt.Graphics;

class Enemy {

    public Vector3d pos;
    public Vector3d movement;

    float speed;
    Color color;
    boolean alive = true;
    float progress = 0; // Track the enemy's progress along the path
    int waypointIndex = 0; // Track the current waypoint index for movement
    boolean gameLoose = false;

    static final Vector3d[] WAYPOINTS = {
        new Vector3d(-20, 500, 0), // Start (links outside)
        new Vector3d(220, 500, 0), // right
        new Vector3d(220, 60, 0),  // up
        new Vector3d(580, 60, 0),  // right
        new Vector3d(580, 220, 0), // down
        new Vector3d(460, 220, 0), // left
        new Vector3d(460, 420, 0), // down
        new Vector3d(660, 420, 0), // right
        new Vector3d(660, 620, 0),  // down (outside = finish)
        new Vector3d(0, 0, 0),  // extra waypoint - might not be too clean but it works ;)
    };

    public Enemy(Color color) {
        this.pos = WAYPOINTS[0].cpy(); // Start y position
        this.movement = WAYPOINTS[1].cpy().nor().scl(speed);
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
        return 1f; // Default speed - shouldnt exist btw
    }

    public void move() {
        progress += speed;
        Vector3d destination = WAYPOINTS[waypointIndex].cpy();

        double distance = pos.dst(destination);

        if (distance < speed) {
            // Move to the next waypoint
            pos = destination;
            waypointIndex++;
            if (waypointIndex >= (WAYPOINTS.length - 1)) {
                GamePanel.gameOver = true;
                GamePanel.looseGame = true;
                alive = false; // Enemy has reached the end of the path
            } else {
                movement = WAYPOINTS[waypointIndex].cpy().sub(pos).nor().scl(speed);
            }
        } else {
            // Move towards the current waypoint
            pos.add(movement);
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
        int drawX = (int) pos.x - 15;  // Center the circle at (x, y)
        int drawY = (int) pos.y - 15;  // Center the circle at (x, y)
        g.fillOval(drawX, drawY, 30, 30);
    }
}