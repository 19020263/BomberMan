package uet.oop.bomberman.maps;

import uet.oop.bomberman.entities.Enemy;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Grass;
import uet.oop.bomberman.entities.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.entities.Balloom;
import uet.oop.bomberman.entities.Brick;
import uet.oop.bomberman.entities.Portal;
import uet.oop.bomberman.entities.Oneal;
import uet.oop.bomberman.entities.BombItem;
import uet.oop.bomberman.entities.SpeedItem;
import uet.oop.bomberman.entities.FlameItem;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Map {
    private static int HEIGHT;
    private static int WIDTH;
    private static List<Entity> entities = new ArrayList<>();
    private static List<Enemy> enemies = new ArrayList<>();
    private static List<Entity> stillObjects = new ArrayList<>();
    private static List<List<Character>> TileMap = new ArrayList<>();

    public static void createMap() throws FileNotFoundException {
        FileReader level = new FileReader("sources/map.txt");
        Scanner scanner = new Scanner(level);
        int lvl = scanner.nextInt();
        HEIGHT = scanner.nextInt();
        WIDTH = scanner.nextInt();

        scanner.nextLine();
        for (int i = 0; i < HEIGHT; i++) {
            String r = scanner.nextLine();
            List<Character> list = new ArrayList<Character>();
            for(char c : r.toCharArray()) {
                if(c == '#' || c == '*' || c == ' ' ) {
                    list.add(c);
                } else {
                    list.add(' ');
                }
            }
            TileMap.add(list);
            for (int j = 0; j < WIDTH; j++) {
                Entity object1;
                Enemy object2;
                if (r.charAt(j) == '#') {
                    object1 = new Wall(j, i, Sprite.wall.getFxImage());
                } else {
                    object1 = new Grass(j, i, Sprite.grass.getFxImage());
                    stillObjects.add(object1);
                    if (r.charAt(j) == '*') {
                        object1 = new Brick(j, i, Sprite.brick.getFxImage());
                    } else if (r.charAt(j) == 'x') {
                        object1 = new Portal(j, i, Sprite.portal.getFxImage());
                        stillObjects.add(object1);
                        object1 = new Brick(j, i, Sprite.brick.getFxImage());
                    } else if (r.charAt(j) == '1') {
                        object2 = new Balloom(j, i, Sprite.balloom_left1.getFxImage());
                        enemies.add(object2);
                        continue;
                    } else if (r.charAt(j) == '2') {
                        object2 = new Oneal(j, i, Sprite.oneal_left1.getFxImage());
                        enemies.add(object2);
                        continue;
                    } else if (r.charAt(j) == 'b') {
                        object1 = new BombItem(j, i, Sprite.powerup_bombs.getFxImage());
                        stillObjects.add(object1);
                        object1 = new Brick(j, i, Sprite.brick.getFxImage());
                    } else if (r.charAt(j) == 'f') {
                        object1 = new FlameItem(j, i, Sprite.powerup_flames.getFxImage());
                        stillObjects.add(object1);
                        object1 = new Brick(j, i, Sprite.brick.getFxImage());
                    } else if (r.charAt(j) == 's') {
                        object1 = new SpeedItem(j, i, Sprite.powerup_speed.getFxImage());
                        stillObjects.add(object1);
                        object1 = new Brick(j, i, Sprite.brick.getFxImage());
                    }
                }
                stillObjects.add(object1);
            }
        }
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static List<Enemy> getEnemies() {
        return enemies;
    }

    public static List<Entity> getEntities() {
        return entities;
    }

    public static List<Entity> getStillObjects() {
        return stillObjects;
    }

    public static List<List<Character>> getTileMap() {
        return TileMap;
    }
}
