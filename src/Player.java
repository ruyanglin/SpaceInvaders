import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Player extends GamePiece {
    public Player(ImageView imageView, Pane pane, double x, double y, double dx, double dy) {
        super(imageView, pane, x, y, dx, dy);
    }

    public boolean move(double direction) {
        if (this.x > MAX_SCREEN_WIDTH - super.w - dx) {
            this.x = MAX_SCREEN_WIDTH - 100;
            return true;
        } else if (this.x < 0  - dx) {
            return true;
        }

        this.x += direction;
        imageView.setX(x);
        return false;
    }

}
