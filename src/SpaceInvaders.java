import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class SpaceInvaders extends Application {

    private final String titleImagePath = "images/logo.png";
    private final Effect glow = new Glow(1);

    String invader1 = getClass().getClassLoader().getResource("sounds/fastinvader1.wav").toString();
    AudioClip invader1Clip = new AudioClip(invader1);

    String invader2 = getClass().getClassLoader().getResource("sounds/fastinvader2.wav").toString();
    AudioClip invader2Clip = new AudioClip(invader2);

    String invader3 = getClass().getClassLoader().getResource("sounds/fastinvader3.wav").toString();
    AudioClip invader3Clip = new AudioClip(invader3);

    String invader4 = getClass().getClassLoader().getResource("sounds/fastinvader4.wav").toString();
    AudioClip invader4Clip = new AudioClip(invader4);

    long delta = 0;
    int level = 1;
    int counter = 0;

    Pane gameRoot;
    GamePane gamePane;
    @Override
    public void start(Stage stage) {
        // Create a scene graph with a root node
        // This will hold the objects that we want to display on the stage
        Pane menuRoot = new Pane();
        gameRoot = new Pane();
        gamePane = new GamePane(gameRoot, 1);
        gamePane.makeScene();


        Image titleImage = new Image(titleImagePath);
        ImageView titleImageView = new ImageView(titleImage);
        titleImageView.setX(390);
        titleImageView.setY(75);
        titleImageView.setPreserveRatio(true);

        Text instructionText = new Text(510, 400, "Instructions");
        instructionText.setFill(Color.BLACK);
        instructionText.setFont(Font.font(null, FontWeight.BOLD, 36));
        instructionText.setTextAlignment(TextAlignment.CENTER);
        instructionText.setUnderline(true);

        Text menuText = new Text(495, 465, "ENTER - Start Game\nQ - Quit");
        menuText.setFill(Color.BLACK);
        menuText.setFont(Font.font(null, FontWeight.BOLD, 24));
        menuText.setTextAlignment(TextAlignment.CENTER);


        Text levelText1 = new Text(508, 525, "1");
        levelText1.setFill(Color.RED);
        levelText1.setFont(Font.font(null, FontWeight.BOLD, 24));
        levelText1.setTextAlignment(TextAlignment.CENTER);
        levelText1.setEffect(glow);


        Text levelText2 = new Text(535, 525, "2");
        levelText2.setFill(Color.BLACK);
        levelText2.setFont(Font.font(null, FontWeight.BOLD, 24));
        levelText2.setTextAlignment(TextAlignment.CENTER);

        Text levelText3 = new Text(559, 525, "3");
        levelText3.setFill(Color.BLACK);
        levelText3.setFont(Font.font(null, FontWeight.BOLD, 24));
        levelText3.setTextAlignment(TextAlignment.CENTER);

        Text levelText = new Text(516, 525, " ,  ,  - select level");
        levelText.setFill(Color.BLACK);
        levelText.setFont(Font.font(null, FontWeight.BOLD, 24));
        levelText.setTextAlignment(TextAlignment.CENTER);

        Text controlText = new Text(475, 555, "◄ , ► - move left, right\n SPACE - shoot");
        controlText.setFill(Color.BLACK);
        controlText.setFont(Font.font(null, FontWeight.BOLD, 24));
        controlText.setTextAlignment(TextAlignment.CENTER);


        menuRoot.getChildren().addAll(titleImageView, instructionText, menuText,
                levelText1, levelText2, levelText3, levelText, controlText);

        Scene menuScene = new Scene(menuRoot, 1280, 960, Color.FLORALWHITE);

        // END MENU
        Pane endRoot = new Pane();
        Text titleText = new Text(490, 150, "YOU WON!!");
        titleText.setFill(Color.BLACK);
        titleText.setFont(Font.font(null, FontWeight.BOLD, 52));
        titleText.setTextAlignment(TextAlignment.CENTER);

        Text scoreText = new Text(550, 225, "Score: ");
        scoreText.setFill(Color.BLACK);
        scoreText.setFont(Font.font(null, FontWeight.BOLD, 40));
        scoreText.setTextAlignment(TextAlignment.CENTER);

        Text endMenuText = new Text(555, 290, "Q - Quit\nENTER - Restart");
        endMenuText.setFill(Color.BLACK);
        endMenuText.setFont(Font.font(null, FontWeight.BOLD, 24));
        endMenuText.setTextAlignment(TextAlignment.CENTER);

        endRoot.getChildren().addAll(titleText, scoreText, endMenuText);

        int SCREEN_HEIGHT = 960;
        int SCREEN_WIDTH = 1280;
        Scene endScene = new Scene(endRoot, SCREEN_WIDTH, SCREEN_HEIGHT);
        endScene.addEventFilter(KeyEvent.KEY_PRESSED, endMenuHandler(stage, menuScene));

        AnimationTimer timer = new AnimationTimer() {
            public long lastUpdate = 0;
            public long lastSoundUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastSoundUpdate >= 300000000) {
                    playBGM(counter);
                    counter += 1;

                    lastSoundUpdate = now ;
                }
                if (now - lastUpdate >= 30000000) {
                    gamePane.updateAliens();
                    gamePane.updateProjectiles();
                    gamePane.handleCollision();
                    if(gamePane.checkWin()) {
                        this.stop();
                        titleText.setText("YOU WON!!");
                        scoreText.setText("Score: " + gamePane.score);
                        stage.setScene(endScene);
                        gamePane.resetAliens();
                        gamePane.resetProjectiles();
                        gamePane.resetStats();
                        gamePane.resetPlayer();
                        gamePane.createPlayer();
                        gamePane.createAliens();
                    }
                    if (gamePane.checkLose()) {
                        this.stop();
                        titleText.setText("YOU LOST!!");
                        scoreText.setText("Score: " + gamePane.score);
                        stage.setScene(endScene);
                        gamePane.resetProjectiles();
                        gamePane.resetAliens();
                        gamePane.resetStats();
                        gamePane.resetPlayer();
                        gamePane.createPlayer();
                        gamePane.createAliens();

                    }
                    lastUpdate = now ;
                }

            }
        };



        Scene gameScene = new Scene(gamePane.gamePane, SCREEN_WIDTH, SCREEN_HEIGHT, Color.BLACK);
        gameScene.addEventFilter(KeyEvent.KEY_PRESSED, playerHandler(gamePane));

        menuScene.addEventFilter(KeyEvent.KEY_PRESSED,
                menuHandler(stage, gameScene, timer, levelText1, levelText2, levelText3));

        // Add the scene to the stage and show it
        stage.setTitle("Space Invaders");
        stage.setScene(menuScene);
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public void playBGM(int counter) {
        if (counter % 4 == 0) {
            invader1Clip.setRate(1);
            invader1Clip.play();
        } else if (counter % 4 == 1) {

            invader2Clip.setRate(0.5);
            invader2Clip.play();
        } else if (counter % 4 == 2) {

            invader3Clip.setRate(0.5);
            invader3Clip.play();
        } else if (counter % 4 == 3) {

            invader4Clip.setRate(0.5);
            invader4Clip.play();
        }

    }


    private EventHandler<KeyEvent> menuHandler(Stage stage, Scene gameScene, AnimationTimer timer, Text text1, Text text2, Text text3) {
        return keyEvent -> {

            switch (keyEvent.getCode()) {
                case ENTER:
                    // Switch to scene
                    gamePane.resetAliens();
                    gamePane.createAliens();

                    highlight(text1);
                    reset(text2);
                    reset(text3);
                    stage.setScene(gameScene);
                    timer.start(); // this timer will be stopped automatically by JavaFX when the program terminates
                    break;
                case DIGIT1:
                case NUMPAD1:
                    highlight(text1);
                    gamePane.updateInfoText(1, 3);
                    gamePane.level = 1;
                    level = 1;
                    reset(text2);
                    reset(text3);
                    break;
                case DIGIT2:
                case NUMPAD2:
                    highlight(text2);
                    gamePane.updateInfoText(2, 3);
                    gamePane.level = 2;
                    level = 2;
                    reset(text1);
                    reset(text3);
                    break;
                case DIGIT3:
                case NUMPAD3:
                    highlight(text3);
                    gamePane.updateInfoText(3, 3);
                    gamePane.level = 3;
                    level = 3;
                    reset(text1);
                    reset(text2);
                    break;
                case Q:
                    Platform.exit();
                    System.exit(0);
                    break;
            }
        };
    }

    private EventHandler<KeyEvent> playerHandler(GamePane gamePane) {
        return keyEvent -> {
            switch (keyEvent.getCode()) {
                case SPACE:
                    if(gamePane.playerFire(System.nanoTime() - delta)) {
                        delta = System.nanoTime();
                    }
                    break;
                case LEFT:
                    gamePane.updatePlayer(-10);
                    break;
                case RIGHT:
                    gamePane.updatePlayer( 10);
                    break;
                case Q:
                    Platform.exit();
                    System.exit(0);
                    break;
            }
        };
    }

    private EventHandler<KeyEvent> endMenuHandler(Stage stage, Scene menuScene) {
        return keyEvent -> {
            switch (keyEvent.getCode()) {
                case ENTER:
                    stage.setScene(menuScene);
                    break;
                case Q:
                    Platform.exit();
                    System.exit(0);
                    break;
            }
        };
    }

    private void reset(Text text) {
        text.setFill(Color.BLACK);
        text.setEffect(null);
    }

    private void highlight(Text text) {
        text.setFill(Color.RED);
        text.setEffect(glow);
    }
}
