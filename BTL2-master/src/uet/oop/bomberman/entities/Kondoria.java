package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.awt.*;
import java.util.Random;

public class Kondoria extends Enemy {
    protected Bomber bomber;
    protected int direction;
    Random random = new Random();

    public Kondoria(int xUnit, int yUnit, Image img, Bomber bomber) {
        super(xUnit, yUnit, img);
        this.bomber = bomber;
        setLayer(4);
        setSpeed(1);
        generateDirection();
        alive = true;
    }

    public void goLeft() {
        super.goLeft();
        img = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_left2, Sprite.kondoria_left3, left++, 18).getFxImage();
    }

    public void goRight() {
        super.goRight();
        img = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, right++, 18).getFxImage();
    }

    public void goUp() {
        super.goUp();
        img = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_left2, Sprite.kondoria_left3, up++, 18).getFxImage();
    }

    public void goDown() {
        super.goDown();
        img = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, down++, 18).getFxImage();
    }

    @Override
    public void stay() {
        super.stay();
    }

    @Override
    public void update() {
        if(isAlive()) {
            generateDirection();
            if (direction == 0) goLeft();
            if (direction == 1) goRight();
            if (direction == 2) goUp();
            if (direction == 3) goDown();
        }else if(animated < 30){
            animated++;
            img = Sprite.kondoria_dead.getFxImage();
        }else
            BombermanGame.enemies.remove(this);
    }

    public void generateDirection() {
        int vertical = random.nextInt(2);

        if(vertical == 1) {
            int v = calculateRowDirection();
            if(v != -1)
                direction = v;
            else
                direction = calculateColDirection();

        } else {
            int h = calculateColDirection();

            if(h != -1)
                direction = h;
            else
                direction = calculateRowDirection();
        }
    }

    private int calculateColDirection() {
        if(bomber.x / Sprite.SCALED_SIZE < x / Sprite.SCALED_SIZE)
            return 0;
        else if(bomber.x / Sprite.SCALED_SIZE > x / Sprite.SCALED_SIZE)
            return 1;

        return -1;
    }

    private int calculateRowDirection() {
        if(bomber.y / Sprite.SCALED_SIZE < y / Sprite.SCALED_SIZE)
            return 2;
        else if(bomber.y / Sprite.SCALED_SIZE > y / Sprite.SCALED_SIZE)
            return 3;
        return -1;
    }
}
