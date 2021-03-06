package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.graphics.Sprite;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.List;

public class BombermanGame extends Application {

    public static int WIDTH = 20;
    public static int HEIGHT = 15;
    public final static int MaxLevel = 5;
    public static int level = 1;
    private boolean newMap = true;
    private int time = 0;
    private Label exits;
    private Label play_again;
    private Label play;
    private GraphicsContext gc;
    private Canvas canvas;
    private Scanner scanner;
    boolean reload = true;
    boolean run = false;

    public static final List<Enemy> enemies = new ArrayList<>();
    private final List<StillEntity> stillObjects = new ArrayList<>();
    private Bomber myBomber = new Bomber(0 , 0, Sprite.player_right.getFxImage());

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        Sound.stage_theme.play();
        Sound.stage_theme.setAutoPlay(true);
        Sound.stage_theme.setCycleCount(99999);

        stage.setScene(start_game());
        exits.setOnMouseClicked(event -> {
            stage.close();
        });
        play.setOnMouseClicked(event -> {
            run = true;
            loadMap();
        });
        AnimationTimer timer = new AnimationTimer() {

            @Override
            public void handle(long l) {
                if (run) {
                    if (newMap) {
                        if (time == 0) {
                            stage.setScene(message());
                        }
                        if (time < 120) {
                            time++;
                        } else {
                            myBomber.setDirection(null);
                            stage.setScene(load());
                            newMap = false;
                        }
                    } else {
                        render();
                        update();
                        if (end_game(stage))
                            mouseHandel(stage);
                        if (checkNextMap() && level < MaxLevel) {
                            nextMap();
                        }
                        keyHandel(stage, myBomber);
                    }
                }
            }
        };
        timer.start();
        stage.show();
    }

    private Scene start_game()  {

        BackgroundFill backgroundFill = new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        Label lb = label("BOMBERMAN GAME",110, 100,60);
        lb.setTextFill(Color.ORANGE);
        play = label("PLAY", 350,180,30);
        exits = label("EXIT", 350,220,30);
        AnchorPane ap = new AnchorPane(lb,play,exits);
        ap.setBackground(background);
        return new Scene(ap,800,400);

    }
    private Label label(String str, double x, double y, double size){
        Label l = new Label(str);
        l.setLayoutX(x);
        l.setLayoutY(y);
        l.setTextFill(Color.WHITE);
        l.setAlignment(Pos.CENTER);
        l.setFont(new Font(size));
        return l;
    }

    private boolean end_game(Stage stage){
        if(myBomber.isDeath()){
            if(reload) {
                stage.setScene(lose_win("GAME OVER"));
                reload = false;
                return true;
            }
        }
        if(checkNextMap() && level == MaxLevel){
            if(reload){
                stage.setScene(lose_win("YOU WIN"));
                reload = false;
                return true;
            }

        }
        return false;
    }

    private Scene lose_win(String str){
        BackgroundFill backgroundFill = new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        Label lb = label(str, 436, 158, 30);
        exits = label("EXIT", 586, 223, 20);
        play_again = label("PLAY AGAIN", 386, 223, 20);
        AnchorPane anchorPane = new AnchorPane(lb, exits, play_again);
        anchorPane.setBackground(background);
        return new Scene(anchorPane,992, 416);
    }

    private void mouseHandel(Stage stage){
        exits.setOnMouseClicked(event -> {
            stage.close();
        });
        play_again.setOnMouseClicked(event -> {
            level = 1;
            newMap = true;
            stillObjects.clear();
            enemies.clear();
            myBomber = null;
            loadMap();
            time = 0;
            reload = true;
        });
    }
    private void keyHandel(Stage stage, Bomber b){
        stage.getScene().setOnKeyPressed(event -> b.handleKeyPressedEvent(event.getCode()));
        stage.getScene().setOnKeyReleased(event -> b.handleKeyReleasedEvent(event.getCode()));
    }

    private void nextMap() {
        level++;
        newMap = true;
        time = 0;
        Sound.chuyen_man.play();
        myBomber.clear_bom();
        stillObjects.clear();
        loadMap();
    }

    private Scene message(){
        BackgroundFill backgroundFill = new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        Label lb = label("LEVEL " + level, 461, 173, 30);
        AnchorPane anchorPane = new AnchorPane(lb);
        anchorPane.setBackground(background);
        return new Scene(anchorPane,992, 416);
    }

    private Scene load(){
        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();
        // Tao root container
        Group root = new Group();
        root.getChildren().add(canvas);
        // Tao scene
        return new Scene(root);

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

    public void loadMap() {
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
                        if(myBomber != null) {
                            myBomber.setX(x);
                            myBomber.setY(y);
                            myBomber.setImg(Sprite.player_right.getFxImage());
                        }
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
                    if (r3.intersects(r2))
                        myBomber.die();
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