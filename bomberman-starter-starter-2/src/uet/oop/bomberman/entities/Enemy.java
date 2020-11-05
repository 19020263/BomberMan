package uet.oop.bomberman.entities;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.maps.Map;

import java.util.List;
import java.util.Random;


public abstract class Enemy {
    protected int x;
    protected int y;
    protected int _x;
    protected int _y;
    protected Image img;

    public Enemy(int x, int y, Image img) {
        this.x = x;
        this.y = y;
        this.img = img;
    }

    public void render(GraphicsContext gc) {
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        ImageView iv = new ImageView(img);
        Image base = iv.snapshot(params, null);

        gc.drawImage(base, x * Sprite.SCALED_SIZE, y * Sprite.SCALED_SIZE);
    }

    public abstract void update();

    public void caculateMove(int speed) {
        Random random = new Random();
        int ra = random.nextInt(4);
        if (ra == 0) {
            _x = x + speed;
            _y = y;
        } else if (ra == 1) {
            _y = y + speed;
            _x = x;
        } else if (ra == 2) {
            _x = x - speed;
            _y = y;
        } else if (ra == 3) {
            _y = y - speed;
            _x = x;
        }
    }

    public void move() {
        x = _x;
        y = _y;
    }

    public boolean checkMove() {
        boolean cm = true;
        List<List<Character>> tileMap = Map.getTileMap();
        if (tileMap.get(_y).get(_x) == '#' || tileMap.get(_y).get(_x) == '*') {
            cm = false;
        }
        return cm;
    }
}
