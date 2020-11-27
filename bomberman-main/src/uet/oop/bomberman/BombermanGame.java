package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.graphics.Sprite;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.List;
import java.util.jar.Manifest;

public class BombermanGame extends Application {

    public static int WIDTH = 20;
    public static int HEIGHT = 15;
    public static int level = 1;

    private GraphicsContext gc;
    private Canvas canvas;
    private Scanner scanner;

    public static final List<Enemy> enemies = new ArrayList<>();
    private final List<StillEntity> stillObjects = new ArrayList<>();
    private Bomber myBomber;
    private String path = "res/sound/Bomberman SFX4.wav";

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        load();
        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        Group root = new Group();
        root.getChildren().add(canvas);

        // Tao scene
        Scene scene = new Scene(root);

        // Them scene vao stage
        stage.setScene(scene);
        stage.show();

        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);

        Sound.stage_theme.play();
        Sound.stage_theme.seek(Sound.stage_theme.getStartTime());

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                render();
                update();
                if (checkNextMap() && level < 5) {
                    nextMap();
                }
                if (!myBomber.isAlive()) {
                    myBomber.setImg(Sprite.player_dead1.getFxImage());
                }
            }
        };
        timer.start();

        scene.setOnKeyPressed(event -> myBomber.handleKeyPressedEvent(event.getCode()));
        scene.setOnKeyReleased(event -> myBomber.handleKeyReleasedEvent(event.getCode()));
    }

    private void nextMap() {
        level++;
        Sound.chuyen_man.play();
        Sound.chuyen_man.seek(Sound.chuyen_man.getStartTime());
        stillObjects.clear();
        load();
    }

    private boolean checkNextMap() {
        if (enemies.size() == 0) {
            Rectangle r1 = myBomber.getBounds();
            int m = stillObjects.size();
            for (int i = 0; i < m; i++) {
                if (stillObjects.get(i) instanceof Portal) {
                    Rectangle r2 = stillObjects.get(i).getBounds();
                    if (r1.intersects(r2))
                        return true;
                }
            }
        }
        return false;
    }

    public void update() {
        try {
            enemies.forEach(Entity::update);
            myBomber.update();
            List<Bomb> bombs = myBomber.getBombs();
            for (Bomb bomb : bombs) {
                bomb.update();
                if (!bomb.isAlive() && !bomb.isExploded() && bomb.getFlames().size() == 0) bomb.addFlame(stillObjects);
            }
            handleCollisions();
        } catch (ConcurrentModificationException ex) {
        }
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = stillObjects.size() - 1; i >= 0; i--) {
            stillObjects.get(i).render(gc);
        }
        enemies.forEach(g -> g.render(gc));
        List<Bomb> bombs = myBomber.getBombs();
        for (Bomb bomb : bombs) {
            if (bomb.isAlive()) bomb.render(gc);
            else {
                List<Flame> flames = bomb.getFlames();
                for (Flame flame : flames) {
                    flame.render(gc);
                }
            }
        }
        myBomber.render(gc);
    }

    public void load() {
        try {
            scanner = new Scanner(new FileReader("res/levels/level" + level + ".txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scanner.nextInt();
        HEIGHT = scanner.nextInt();
        WIDTH = scanner.nextInt();
        scanner.nextLine();
        createMap();
    }

    public void createMap() {
        for (int y = 0; y < HEIGHT; y++) {
            String r = scanner.nextLine();
            for (int x = 0; x < WIDTH; x++) {
                if (r.charAt(x) == '#') {
                    stillObjects.add(new Wall(x, y, Sprite.wall.getFxImage()));
                } else {
                    stillObjects.add(new Grass(x, y, Sprite.grass.getFxImage()));
                    if (r.charAt(x) == '*') {
                        stillObjects.add(new Brick(x, y, Sprite.brick.getFxImage()));
                    }
                    if (r.charAt(x) == 'x') {
                        stillObjects.add(new Portal(x, y, Sprite.portal.getFxImage()));
                        stillObjects.add(new Brick(x, y, Sprite.brick.getFxImage()));
                    }
                    if (r.charAt(x) == 'p') {
                        myBomber = new Bomber(x, y, Sprite.player_right.getFxImage());
                    }
                    if (r.charAt(x) == '1') {
                        enemies.add(new Balloon(x, y, Sprite.balloom_left1.getFxImage()));
                    }
                    if (r.charAt(x) == '2') {
                        enemies.add(new Oneal(x, y, Sprite.oneal_left1.getFxImage(), myBomber));
                    }
                    if (r.charAt(x) == '3') {
                        enemies.add(new Kondoria(x, y, Sprite.kondoria_left1.getFxImage(), myBomber));
                    }
                    if (r.charAt(x) == '4') {
                        enemies.add(new Minvo(x, y, Sprite.minvo_left1.getFxImage(), myBomber));
                    }
                    if (r.charAt(x) == 'b') {
                        stillObjects.add(new BombItem(x, y, Sprite.powerup_bombs.getFxImage()));
                        stillObjects.add(new Brick(x, y, Sprite.brick.getFxImage()));
                    }
                    if (r.charAt(x) == 'f') {
                        stillObjects.add(new FlameItem(x, y, Sprite.powerup_flames.getFxImage()));
                        stillObjects.add(new Brick(x, y, Sprite.brick.getFxImage()));
                    }
                    if (r.charAt(x) == 's') {
                        stillObjects.add(new SpeedItem(x, y, Sprite.powerup_speed.getFxImage()));
                        stillObjects.add(new Brick(x, y, Sprite.brick.getFxImage()));
                    }
                }
            }
        }
        stillObjects.sort(new DescendingLayer());
    }

    public void handleCollisions() {
        List<Bomb> bombs = myBomber.getBombs();
        Rectangle r1 = myBomber.getDesBounds();
        //Bomber vs Bombs
        for (Bomb bomb : bombs) {
            Rectangle r2 = bomb.getBounds();
            if (!bomb.isAllowedToPassThrough(myBomber) && r1.intersects(r2)) {
                myBomber.stay();
                break;
            }
        }
        //Bomber vs Flames
        for (Bomb bomb : bombs) {
            List<Flame> flames = bomb.getFlames();
            for (Flame flame : flames) {
                Rectangle r2 = flame.getBounds();
                if (flame instanceof BrickExploded) {
                    if (r1.intersects(r2)) myBomber.stay();
                } else {
                    Rectangle r3 = myBomber.getBounds();
                    if (r3.intersects(r2)) myBomber.die();
                }
            }
        }
        //Bomber vs StillObjects
        for (int i = 0; i < stillObjects.size(); i++) {
            StillEntity stillObject = stillObjects.get(i);
            Rectangle r2 = stillObject.getBounds();
            if (r1.intersects(r2)) {
                if (myBomber.getLayer() >= stillObject.getLayer()) {
                    myBomber.move();
                    if (stillObject instanceof Item) {
                        Item item = (Item) stillObject;
                        item.powerUp(myBomber);
                        stillObjects.remove(i--);
                    }
                } else {
                    myBomber.stay();
                }
                break;
            }
        }
        //Bomber vs Enemies
        for (Enemy enemy : enemies) {
            Rectangle r2 = enemy.getDesBounds();
            if (r1.intersects(r2)) {
                myBomber.die();
            }
        }
        //Enemies vs Bombs
        for (Enemy enemy : enemies) {
            Rectangle r2 = enemy.getDesBounds();
            for (Bomb bomb : bombs) {
                Rectangle r3 = bomb.getBounds();
                if (!bomb.isAllowedToPassThrough(enemy) && r2.intersects(r3)) {
                    enemy.stay();
                    break;
                }
            }
        }
        //Enemies vs Flames
        for (Enemy enemy : enemies) {
            Rectangle r2 = enemy.getDesBounds();
            for (Bomb bomb : bombs) {
                List<Flame> flames = bomb.getFlames();
                for (Flame flame : flames) {
                    Rectangle r3 = flame.getBounds();
                    if (flame instanceof BrickExploded) {
                        if (r2.intersects(r3)) enemy.stay();
                    } else {
                        Rectangle r4 = enemy.getBounds();
                        if (r3.intersects(r4)) enemy.die();
                    }
                }
            }
        }
        //Enemies vs StillObjects
        for (Enemy enemy : enemies) {
            Rectangle r2 = enemy.getDesBounds();
            for (StillEntity stillObject : stillObjects) {
                Rectangle r3 = stillObject.getBounds();
                if (r2.intersects(r3)) {
                    if (enemy.getLayer() >= stillObject.getLayer()) {
                        enemy.move();
                    } else {
                        enemy.stay();
                    }
                    break;
                }
            }
        }
        //Bombs vs Flames
        for (int i = 0; i < bombs.size() - 1; i++) {
            List<Flame> flames = bombs.get(i).getFlames();
            for (int j = i + 1; j < bombs.size(); j++) {
                Bomb bomb = bombs.get(j);
                Rectangle r2 = bomb.getBounds();
                for (Flame flame : flames) {
                    Rectangle r3 = flame.getBounds();
                    if (r2.intersects(r3)) {
                        bomb.getExplodedBy(bombs.get(i));
                    }
                }
            }
        }
    }
}

class DescendingLayer implements Comparator<Entity> {
    @Override
    public int compare(Entity o1, Entity o2) {
        return Integer.compare(o2.getLayer(), o1.getLayer());
    }
}