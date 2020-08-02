import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

public interface Piece {

    void draw(Pane pane);

    void animate(Scene scene);
}
