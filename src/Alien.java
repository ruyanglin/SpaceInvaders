import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Alien extends GamePiece {


    public double x;
    public double y;
    public static double dx;
    public double dy;
    public int alienType;
    public int score;
    public double level;
    ImageView alienImage;

    public Alien(ImageView alienImage, Pane pane, double level, int alienType, double x, double y, double newdx, double dy) {
        super(alienImage, pane, x, y, dx, dy);
        this.level = level;
        this.x = x;
        this.y = y;
        dx = newdx;
        this.dy = dy;
        this.alienType = alienType;
        this.alienImage = alienImage;
        this.score = getScore();
    }


    public boolean move() {
        if (this.x > MAX_SCREEN_WIDTH - super.w - dx) {
            return true;
        } else if (this.x < 0 - dx) {
            return true;
        }

        this.x += dx;
        imageView.setX(x);
        return false;
    }

    public void moveDown() {
        this.y += this.dy;
        imageView.setY(y);
    }

    public static void changeDirection(int newlevel) {
        switch(newlevel) {
            case 1:
                dx *= -1.11;
                break;
            case 2:
                dx *= -1.07;
                break;
            case 3:
                dx *= -1.04;
                break;
        }
    }

    public static void updateDX() {
        if (dx < 0) {
            dx -= 0.075;
            return;
        }
        dx  += 0.075;
    }

    public static void setDx(double newDX) {
        if (dx < 0) {
            dx = -newDX;
        } else {
            dx = newDX;
        }
    }

    public int getScore() {
        switch (alienType) {
            case 1:
                score = 10;
                break;
            case 2:
                score = 20;
                break;
            case 3:
                score = 30;
                break;
        }
        return score;
    }
}
