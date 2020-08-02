import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public abstract class GamePiece {
    Image image;
    ImageView imageView;

    Pane pane;
    public final double MAX_SCREEN_WIDTH = 1280;
    public final double MAX_SCREEN_HEIGHT = 960;

    public double x;
    public double y;

    public double dx;
    public double dy;

    public boolean isDead = false;

    public double w;
    public double h;

    public GamePiece(ImageView imageView, Pane pane, double x, double y, double dx, double dy) {
        this.imageView = imageView;
        this.pane = pane;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;


        this.w = 60;
        this.h = 50;
        this.imageView.setX(x);
        this.imageView.setY(y);
        this.imageView.setFitHeight(h);
        this.imageView.setFitWidth(w);
    }

    public void draw() {
        pane.getChildren().add(imageView);
    }

}
