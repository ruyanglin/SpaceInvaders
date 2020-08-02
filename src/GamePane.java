import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GamePane {

    // General Constants
    final String alienOnePath = "images/enemy1.png";
    final String alienTwoPath = "images/enemy2.png";
    final String alienThreePath = "images/enemy3.png";

    final String bulletOne = "images/bullet1.png";
    final String bulletTwo = "images/bullet2.png";
    final String bulletThree = "images/bullet3.png";

    final String playerPath = "images/player.png";
    final String playerBullet = "images/player_bullet.png";

    String explosion = getClass().getClassLoader().getResource("sounds/explosion.wav").toString();
    AudioClip explosionClip = new AudioClip(explosion);

    String playerKilledSound = getClass().getClassLoader().getResource("sounds/invaderkilled.wav").toString();
    AudioClip playerKilledClip = new AudioClip(playerKilledSound);

    String playerShootSound = getClass().getClassLoader().getResource("sounds/shoot.wav").toString();
    AudioClip playerShootClip = new AudioClip(playerShootSound);


    private static final double PLAYER_BULLET_SPEED = 6.0;
    private static final double ENEMY1_BULLET_SPEED = 4.0;
    private static final double ENEMY2_BULLET_SPEED = 5.0;
    private static final double ENEMY3_BULLET_SPEED = 6.0;

    private final double MAX_SCREEN_WIDTH = 1280;
    private final double MAX_SCREEN_HEIGHT = 960;

    // Variables
    public Pane gamePane;
    public int level;

    public static Player player;
    public int score = 0;
    public ArrayList<Projectile> alienProjectiles = new ArrayList<>();
    public ArrayList<Projectile> playerProjectiles = new ArrayList<>();
    public static Alien[][] aliens = new Alien[5][10];


    private int lives = 3;
    private int deadAliens = 0;
    private static boolean lose = false;
    private Text scoreText = new Text(50, 35, "Score: " + score);

    private Text infoText = new Text();

    public GamePane(Pane gamePane, int level) {
        this.gamePane = gamePane;
        this.level = level;

        scoreText.setFill(Color.WHITESMOKE);
        scoreText.setFont(Font.font(null, FontWeight.BOLD, 30));

        infoText.setText("Lives: " + lives + "     Level: " + level);
        infoText.setX(800);
        infoText.setY(35);
        infoText.setFill(Color.WHITESMOKE);
        infoText.setFont(Font.font(null, FontWeight.BOLD, 30));
        gamePane.getChildren().add(scoreText);
        gamePane.getChildren().add(infoText);
    }

    public void makeScene() {
        createAliens();
        createPlayer();
    }

    public void createAliens() {
        int alienType = 3;
        double x = 1;
        double y = 50;

        String alienPath = alienThreePath;

        for (int i = 0; i < 5; i++) {
            if (i == 1 || i == 2) {
                alienType = 2;
                alienPath = alienTwoPath;
            } else if (i == 3 || i == 4) {
                alienType = 1;
                alienPath = alienOnePath;
            }

            for (int j = 0; j < 10; j++) {
                ImageView alienImageView = new ImageView(new Image(alienPath));
                aliens[i][j] = new Alien(alienImageView, gamePane, level, alienType, x, y, 1 + level * level, 15);
                aliens[i][j].draw();
                x += 70;
            }
            x = 1;
            y += 55;
        }
    }


    public void createPlayer() {
        double x = 620;
        double y = 900;
        ImageView alienImageView = new ImageView(new Image(playerPath));
        player = new Player(alienImageView, gamePane, x, y, 0, 0);
        player.draw();
    }

    public void updatePlayer(double playerDirection) {
        player.move(playerDirection);
    }

    public boolean playerFire(long delta) {
        // 500000000 is half a second (in nanoseconds)
        if (delta < 500000000) {
            return false;
        }
        ImageView bulletIV = new ImageView(new Image(playerBullet));
        Projectile bullet = new Projectile(bulletIV, gamePane, player.x + 23, player.y, 0, PLAYER_BULLET_SPEED);
        playerProjectiles.add(bullet);
        bullet.draw();
        playerShootClip.play();
        return true;
    }

    public void updateAliens() {
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 5; j++) {
                if (!aliens[j][i].isDead) {
                    if (aliens[j][i].move()) {
                        adjust(i);
                        moveDown();
                        Alien.changeDirection(level);
                        return;
                    }
                }
            }
        }
    }

    public void adjust(int lastCol) {
        for (int i = 0; i < 5; i++) {
            if (!aliens[i][lastCol].isDead && Alien.dx > 0) {
                aliens[i][lastCol].x += Alien.dx;
            }
        }
    }

    public void moveDown() {
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 5; j++) {
                aliens[j][i].moveDown();
            }
        }
    }


    public void updateProjectiles() {
        Random rnd = new Random();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                if (!aliens[i][j].isDead && (rnd.nextDouble() < (level + deadAliens*2)/500f)) {
                    Projectile projectile = makeProjectile(aliens[i][j]);
                    if (alienProjectiles.size() <= (level + 2) * 2) {
                        alienProjectiles.add(projectile);
                        projectile.draw();
                    }
                }
            }
        }
        alienProjectiles.removeIf(Projectile::alienProjectileMove);
        playerProjectiles.removeIf(Projectile::playerProjectileMove);
    }

    public void handleCollision() {
        Alien cur;
        // Player Bullet and alien collision
        outer: for (Iterator<Projectile> it = playerProjectiles.iterator(); it.hasNext(); ) {
            Projectile projectile = it.next();
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 10; j++) {
                    cur = aliens[i][j];
                    if (!cur.isDead && projectile.imageView.intersects(cur.x, cur.y, cur.w, cur.h)) {
                        explosionClip.play();
                        cur.isDead = true;
                        cur.imageView.setVisible(false);
                        gamePane.getChildren().remove(projectile.imageView);
                        it.remove();
                        score += cur.score;
                        Alien.updateDX();
                        deadAliens++;
                        updateScoreText(score);
                        break outer;
                    }
                }
            }
        }

        // Alien Bullet and player collision
        for (Iterator<Projectile> it = alienProjectiles.iterator(); it.hasNext(); ) {
            Projectile projectile = it.next();
            if (projectile.imageView.intersects(player.x, player.y, player.w, player.h)) {
                playerKilledClip.play();
                lives -= 1;
                updateInfoText(level, lives);
                player.imageView.setVisible(false);
                createPlayer();
                projectile.imageView.setVisible(false);
                it.remove();
                break;
            }
        }

        // Player and alien collision
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                cur = aliens[i][j];
                if (!cur.isDead && player.imageView.intersects(cur.x, cur.y, cur.w, cur.h)) {
                    lose = true;
                    return;
                }
            }
        }
    }

    public Projectile makeProjectile(Alien alien) {
        if (alien.alienType == 1) {
            ImageView alienProjectileIV = new ImageView(new Image(bulletOne));
            return new Projectile(
                    alienProjectileIV, gamePane, alien.x + 30, alien.y, 0, ENEMY1_BULLET_SPEED);
        } else if (alien.alienType == 2) {
            ImageView alienProjectileIV = new ImageView(new Image(bulletTwo));
            return new Projectile(
                    alienProjectileIV, gamePane, alien.x + 30, alien.y, 0, ENEMY2_BULLET_SPEED);
        } else {
            ImageView alienProjectileIV = new ImageView(new Image(bulletThree));
            return new Projectile(
                    alienProjectileIV, gamePane, alien.x + 30, alien.y, 0, ENEMY3_BULLET_SPEED);
        }
    }

    public void updateScoreText(int score) {
        scoreText.setText("Score: " + score);
    }

    public void updateInfoText(int level, int lives) {
        infoText.setText("Lives: " + lives + "     Level: " + level);
    }

    public boolean checkLose() {
        return lives == 0 || lose;
    }

    public boolean checkWin() {
        if (score % 900 != 0 && score != 0) {
            return false;
        }

        boolean isCleared = isCleared();
        if (isCleared && level == 3) {
            resetAliens();
            resetProjectiles();
            resetPlayer();
            createPlayer();
            createAliens();
            return true;
        } else if (isCleared){
            level = level + 1;
            updateInfoText(level, lives);
            resetAliens();
            resetProjectiles();
            resetPlayer();
            createAliens();
            createPlayer();
        }
        return false;
    }

    public boolean isCleared() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                if (!aliens[i][j].isDead) {
                    return false;
                }
            }
        }
        return true;
    }

    public void resetAliens() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                aliens[i][j].imageView.setVisible(false);
                gamePane.getChildren().remove(aliens[i][j].imageView);
            }
        }
    }

    public void resetPlayer() {
        player.imageView.setVisible(false);
        gamePane.getChildren().remove(player.imageView);
    }

    public void resetProjectiles() {
        for (Iterator<Projectile> it = playerProjectiles.iterator(); it.hasNext(); ) {
            Projectile projectile = it.next();
            gamePane.getChildren().remove(projectile.imageView);
            it.remove();
        }

        for (Iterator<Projectile> it = alienProjectiles.iterator(); it.hasNext(); ) {
            Projectile projectile = it.next();
            gamePane.getChildren().remove(projectile.imageView);
            it.remove();
        }
    }

    public void resetStats() {
        this.level = 1;
        lives = 3;
        lose = false;
        score = 0;
        Alien.setDx(1 + level * level);
        updateInfoText(level, lives);
        updateScoreText(score);
    }
}
