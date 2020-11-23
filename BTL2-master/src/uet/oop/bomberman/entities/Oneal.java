package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.awt.*;
import java.util.Random;

public class Oneal extends Enemy {
    private int direction;
    protected Bomber bomber;
    private Rectangle onealRadius = new Rectangle(x - 160, y - 160, 160 * 2, 160 * 2);

    public Oneal(int xUnit, int yUnit, Image img, Bomber bomber) {
        super(xUnit, yUnit, img);
        this.bomber = bomber;
        setLayer(1);
        setSpeed(2);
        generateDirection();
    }

    public void goLeft() {
        super.goLeft();
        img = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, left++, 18).getFxImage();
    }

    public void goRight() {
        super.goRight();
        img = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, right++, 28).getFxImage();
    }

    public void goUp() {
        super.goUp();
        img = Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2, Sprite.oneal_left3, up++, 18).getFxImage();
    }

    public void goDown() {
        super.goDown();
        img = Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2, Sprite.oneal_right3, down++, 18).getFxImage();
    }

    @Override
    public void stay() {
        super.stay();
        generateDirection();
    }



    @Override
    public void update() {
        if (direction == 0) goLeft();
        if (direction == 1) goRight();
        if (direction == 2) goUp();
        if (direction == 3) goDown();

        if(isAlive()){

        }else if(animated < 30){
            animated++;
            img = Sprite.oneal_dead.getFxImage();
        }else
            BombermanGame.enemies.remove(this);
    }

    public void generateDirection() {
        Rectangle re = bomber.getBounds();
        if (onealRadius.intersects(re)) {
            if (bomber.x / Sprite.SCALED_SIZE - x / Sprite.SCALED_SIZE < 0) direction = 0;
            if (bomber.x / Sprite.SCALED_SIZE - x / Sprite.SCALED_SIZE > 0) direction = 1;
            if (bomber.y / Sprite.SCALED_SIZE - y / Sprite.SCALED_SIZE < 0) direction = 2;
            if (bomber.y / Sprite.SCALED_SIZE - y / Sprite.SCALED_SIZE > 0) direction = 3;
        } else {
            Random random = new Random();
            direction = random.nextInt(4);
        }
    }
}
