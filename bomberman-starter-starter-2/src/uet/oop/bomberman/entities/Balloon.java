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
        this.setImg(Sprite.balloom_left1.getFxImage());
    }

    public void goRight() {
        super.goRight();
        this.setImg(Sprite.balloom_right1.getFxImage());
    }

    public void goUp() {
        super.goUp();
        this.setImg(Sprite.balloom_right3.getFxImage());
    }

    public void goDown() {
        super.goDown();
        this.setImg(Sprite.balloom_right3.getFxImage());
    }

    @Override
    public void update() {
        Random random = new Random();
        int re = random.nextInt(4);
        switch (re) {
            case 0:
                goRight();
                break;
            case 1:
                goLeft();
                break;
            case 2:
                goDown();
                break;
            case 3:
                goUp();
                break;
            default:
                break;
        }
    }
}
