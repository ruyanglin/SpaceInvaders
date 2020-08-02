import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Projectile extends GamePiece {
    public Projectile(ImageView imageView, Pane pane, double x, double y, double dx, double dy) {
        super(imageView, pane, x, y, dx, dy);
        this.w = 15;
        this.h = 25;

        imageView.setFitWidth(this.w);
        imageView.setFitHeight(this.h);
    }

    public boolean alienProjectileMove() {
        if (this.y > 960) {
            return true;
        }
        this.y += dy;
        imageView.setY(this.y);
        return false;
    }

    public boolean playerProjectileMove() {
        if (this.y < -100) {
            return true;
        }
        this.y -= dy;
        imageView.setY(this.y);
        return false;
    }


}
