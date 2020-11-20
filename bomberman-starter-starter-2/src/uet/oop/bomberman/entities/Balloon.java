package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

import java.util.Random;

public class Balloon extends MoveEntity {

    Random random = new Random();
    int ra = random.nextInt(2);

    public Balloon(int x, int y, Image img) {
        super( x, y, img);
        speed = 1;
    }

    @Override
    public void goRight() {
        super.goRight();
    }

    @Override
    public void goLeft() {
        super.goLeft();
    }

    @Override
    public void update() {
        goRight();
        this.setImg(Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2,
                Sprite.balloom_right3, animate, 40).getFxImage());
        setAnimate();
    }
}
