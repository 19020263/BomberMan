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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class BombermanGame extends Application {
    
    public static int WIDTH = 20;
    public static int HEIGHT = 15;
    public static int level = 1;
    
    public static GraphicsContext gc;
    private Canvas canvas;
    private Scanner scanner;

    public static final List<Enemy> enemies = new ArrayList<>();
    public static final List<Entity> stillObjects = new ArrayList<>();
    public static final List<explosion> explosionList = new ArrayList<>();
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
        Media media = new Media(new File(path).toURI().toString()) ;
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);

        Sound.stage_theme.play();
        Sound.stage_theme.seek(Sound.stage_theme.getStartTime());

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                render();
                update();
                if(checkNextMap() && level < 5){
                    nextMap();
                }
            }
        };
        timer.start();
        //myBomber = new Bomber(11, 1, Sprite.player_right.getFxImage());

        scene.setOnKeyPressed(event -> myBomber.handleKeyPressedEvent(event.getCode()));
        scene.setOnKeyReleased(event -> myBomber.handleKeyReleasedEvent(event.getCode()));
    }

    private void nextMap(){
        level++;
        Sound.chuyen_man.play();
        Sound.chuyen_man.seek(Sound.chuyen_man.getStartTime());
        stillObjects.clear();
        explosionList.clear();
        load();
    }
    private boolean checkNextMap(){
        if(enemies.size() == 0){
            Rectangle r1 = myBomber.getBounds();
            int m = stillObjects.size();
            for (int i = 0; i < m; i++){
                if(stillObjects.get(i) instanceof Portal){
                    Rectangle r2 = stillObjects.get(i).getBounds();
                    if(r1.intersects(r2))
                        return true;
                }
            }
        }
        return false;
    }

    public void update() {

        for(int i = 0; i < enemies.size(); i++)
            enemies.get(i).update();
        for (int i = 0; i < explosionList.size(); i++)
            explosionList.get(i).update();

        myBomber.update();
        List<Bomb> bombs = myBomber.getBombs();
        for(Bomb bomb : bombs) {
            bomb.update();
        }

        for(int i = 0; i < stillObjects.size(); i++)
            stillObjects.get(i).update();
        handleCollisions();
        checkCollision();
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = stillObjects.size() - 1; i >= 0; i--) {
            stillObjects.get(i).render(gc);
        }
        enemies.forEach(g -> g.render(gc));
        List<Bomb> bombs = myBomber.getBombs();
        for(Bomb bomb : bombs) {
            bomb.render(gc);
        }
        myBomber.render(gc);
        explosionList.forEach(g -> g.render(gc));
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
        for (int i = 0; i < HEIGHT; i++) {
            String r = scanner.nextLine();
            for (int j = 0; j < WIDTH; j++) {
                if (r.charAt(j) == '#') {
                    stillObjects.add(new Wall(j, i, Sprite.wall.getFxImage()));
                } else {
                    stillObjects.add(new Grass(j, i, Sprite.grass.getFxImage()));
                    if (r.charAt(j) == '*') {
                        stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                    }
                    if (r.charAt(j) == 'x') {
                        stillObjects.add(new Portal(j, i, Sprite.portal.getFxImage()));
                        stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                    }
                    if (r.charAt(j) == '1') {
                        enemies.add(new Balloon(j, i, Sprite.balloom_left1.getFxImage()));
                    }
                    if (r.charAt(j) == '2') {
                        enemies.add(new Oneal(j, i, Sprite.oneal_left1.getFxImage(), myBomber));
                    }
                    if (r.charAt(j) == '3') {
                        enemies.add(new Kondoria(j, i, Sprite.kondoria_left1.getFxImage(), myBomber));
                    }
                    if (r.charAt(j) == '4') {
                        enemies.add(new Minvo(j, i, Sprite.minvo_left1.getFxImage(), myBomber));
                    }
                    if (r.charAt(j) == 'b') {
                        stillObjects.add(new BombItem(j, i, Sprite.powerup_bombs.getFxImage()));
                        stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                    }
                    if (r.charAt(j) == 'f') {
                        stillObjects.add(new FlameItem(j, i, Sprite.powerup_flames.getFxImage()));
                        stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                    }
                    if (r.charAt(j) == 's') {
                        stillObjects.add(new SpeedItem(j, i, Sprite.powerup_speed.getFxImage()));
                        stillObjects.add(new Brick(j, i, Sprite.brick.getFxImage()));
                    }
                    if (r.charAt(j) == 'p'){
                        myBomber = new Bomber(j, i, Sprite.player_right.getFxImage());
                    }
                }
            }
        }
        stillObjects.sort(new DescendingLayer());
    }

    public void handleCollisions() {
        List<Bomb> bombs = myBomber.getBombs();
        Rectangle r1 = myBomber.getBounds();
        //Bomber vs Bombs
        for (Bomb bomb : bombs) {
            Rectangle r2 = bomb.getBounds();
            if (!bomb.isAllowedToPassThrough(myBomber) && r1.intersects(r2)) {
                myBomber.stay();
                break;
            }
        }
        //Bomber vs StillObjects
        for (Entity stillObject : stillObjects) {
            Rectangle r2 = stillObject.getBounds();
            if (r1.intersects(r2)) {
                if (myBomber.getLayer() >= stillObject.getLayer()) {
                    myBomber.move();
                } else {
                    myBomber.stay();
                }
                break;
            }
        }
        //Bomber vs Enemies
        for (Enemy enemy : enemies) {
            Rectangle r2 = enemy.getBounds();
            if (r1.intersects(r2)) {
                myBomber.die();
            }
        }
        //Enemies vs Bombs
        for (Enemy enemy : enemies) {
            Rectangle r2 = enemy.getBounds();
            for (Bomb bomb : bombs) {
                Rectangle r3 = bomb.getBounds();
                if (!bomb.isAllowedToPassThrough(enemy) && r2.intersects(r3)) {
                    enemy.stay();
                    break;
                }
            }
        }
        //Enemies vs StillObjects
        for (Enemy enemy : enemies) {
            Rectangle r2 = enemy.getBounds();
            for (Entity stillObject : stillObjects) {
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
    }

    public void checkCollision() {
        //if(explosionList != null){
            for(int i = 0; i < explosionList.size(); i++) {
                Rectangle r1 = explosionList.get(i).getBounds();
                for (int j = 0; j < stillObjects.size(); j++) {
                    Rectangle r2 = stillObjects.get(j).getBounds();
                    if(r1.intersects(r2) && !(stillObjects.get(j) instanceof Item))
                        stillObjects.get(j).setAlive(false);
                }
                for(int j = 0; j < enemies.size(); j++){
                    Rectangle r2 = enemies.get(j).getBounds();
                    if(r1.intersects(r2))
                        enemies.get(j).setAlive(false);
                }
                Rectangle r2 = myBomber.getBounds();
                if(r1.intersects(r2))
                    myBomber.setAlive(false);
            }
        //}
    }
}

class DescendingLayer implements Comparator<Entity> {
    @Override
    public int compare(Entity o1, Entity o2) {
        return Integer.compare(o2.getLayer(), o1.getLayer());
    }
}