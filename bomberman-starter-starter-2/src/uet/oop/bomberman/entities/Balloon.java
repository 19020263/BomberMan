package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.Random;

public class Balloon extends MoveEntity {
    public Balloon(int x, int y, Image img) {
        super( x, y, img);
        layer = 1;
    }

    public void goLeft() {
        super.goLeft();
    }

    public void goRight() {
        super.goRight();
    }

    public void goUp() {
        super.goUp();
    }

    public void goDown() {
        super.goDown();
    }

    @Override
    public void update() {
        Random random = new Random();
        int re = random.nextInt(4);
        switch (re) {
            case 0:
                goRight();
                this.setImg(Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2,
                            Sprite.balloom_right3, animate, 10).getFxImage());
                break;
            case 1:
                goLeft();
                this.setImg(Sprite.movingSprite(Sprite.balloom_left1, Sprite.balloom_left2,
                            Sprite.balloom_left3, animate, 10).getFxImage());
                break;
            case 2:
                goDown();
                this.setImg(Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2,
                            Sprite.balloom_right3, animate, 10).getFxImage());
                break;
            case 3:
                goUp();
                this.setImg(Sprite.movingSprite(Sprite.balloom_right1, Sprite.balloom_right2,
                            Sprite.balloom_right3, animate, 10).getFxImage());
                break;
            default:
                break;
        }

        setAnimate();
    }
}
