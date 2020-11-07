package uet.oop.bomberman;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.graphics.Sprite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BombermanGame extends Application {

    public static int WIDTH;
    public static int HEIGHT;
    public static Scanner sc;
    public static String[][] map;
    public static GraphicsContext gc;
    private Canvas canvas;
    public static List<Entity> entities = new ArrayList<>();
    public static List<Entity> bomber = new ArrayList<>();
    public static List<Entity> stillObjects = new ArrayList<>();
    public static List<Entity> item = new ArrayList<>();    


    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        FileReader lev = new FileReader("Level1.txt");
        sc = new Scanner(lev);
        int level = sc.nextInt();
        HEIGHT = sc.nextInt();
        WIDTH = sc.nextInt();
        sc.nextLine();
        System.out.println(HEIGHT + " " + WIDTH);
        map = new String[HEIGHT][WIDTH];
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
        AnimationTimer timer = new AnimationTimer() {
            private long lastCall = 0;
            @Override
            public void handle(long l) {
                if (l - lastCall >= 2e8) {
                    update();
                    render();
                    lastCall = l;
                }
            }
        };
        timer.start();

        createMap();

        Bomber bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
        bomber.add(bomberman);

        scene.setOnKeyPressed(event -> {
            bomberman.keyCode = event.getCode();
            bomberman.update();
        });

        /*Entity bomberman = new Bomber(1,1, Sprite.player_right.getFxImage());
        entities.add(bomberman);
        scene.setOnKeyPressed(event -> move_bomber(event));*/
        //render();
    }

    public void createMap() {
        for (int i = 0; i < HEIGHT; i++) {
            String line = sc.nextLine();
            for (int j = 0; j < WIDTH; j++) {
                Entity object;
                if (line.charAt(j) == '#') {
                    map[i][j] = "#";
                    object = new Wall(j, i, Sprite.wall.getFxImage());
                } else if(line.charAt(j) == '*') {
                    map[i][j] = "*";
                    object = new Brick(j,i,Sprite.brick.getFxImage());
                } else if(line.charAt(j) == '1') {
                    map[i][j] = " ";
                    object = new Balloon(j,i,Sprite.balloom_left1.getFxImage());
                    entities.add(object);
                    object = new Grass (j, i, Sprite.grass.getFxImage());
                } else if(line.charAt(j) == '2') {
                    map[i][j] = " ";
                    object = new Oneal(j,i, Sprite.oneal_left1.getFxImage());
                    entities.add(object);
                    object = new Grass(j, i, Sprite.grass.getFxImage());
                } else if (line.charAt(j) == 'x') {
                    map[i][j] = "x";
                    object = new Brick(j, i, Sprite.brick.getFxImage());
                } else if (line.charAt(j) == 'f') {
                    map[i][j] = "f";
                    object = new Brick(j, i, Sprite.brick.getFxImage());
                } else if (line.charAt(j) == 's') {
                    map[i][j] = "s";
                    object = new Brick(j, i, Sprite.brick.getFxImage());
                } else if (line.charAt(j) == 'b') {
                    map[i][j] = "b";
                    object = new Brick(j, i, Sprite.brick.getFxImage());
                }else {
                    map[i][j] = " ";
                    object = new Grass(j, i, Sprite.grass.getFxImage());
                }
                stillObjects.add(object);
            }
        }
    }

    public void update() {
        entities.forEach(Entity::update);
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
        bomber.forEach(g -> g.render(gc));
    }

    public void move_bomber(KeyEvent e) {
        int index = entities.size() - 1;
        double x = entities.get(index).getX();
        double y = entities.get(index).getY();
        if (e.getCode() == KeyCode.UP) {
            entities.get(index).setY(y - 0.25);
        }
        else if(e.getCode() == KeyCode.DOWN) {
            entities.get(index).setY(y + 0.25);
        }
        else if(e.getCode() == KeyCode.LEFT) {
            entities.get(index).setX(x - 0.25);
        }
        else if(e.getCode() == KeyCode.RIGHT) {
            entities.get(index).setX(x + 0.25);
        }
        //gc.clearRect(x * Sprite.SCALED_SIZE,y * Sprite.SCALED_SIZE, Sprite.SCALED_SIZE,Sprite.SCALED_SIZE);
        Entity object = new Grass(x , y, Sprite.grass.getFxImage());
        object.render(gc);
        entities.get(index).render(gc);
        //render();
    }
}