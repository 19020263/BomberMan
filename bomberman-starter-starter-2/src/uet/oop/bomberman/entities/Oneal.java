package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public class Oneal extends MoveEntity {
    protected Entity bomber;

    public Oneal(int x, int y, Image img, Entity bomber) {
        super( x, y, img);
        this.bomber = bomber;
    }

    public void goLeft() {
        super.goLeft();
        this.setImg(Sprite.oneal_left1.getFxImage());
    }

    public void goRight() {
        super.goRight();
        this.setImg(Sprite.oneal_right1.getFxImage());
    }

    public void goUp() {
        super.goUp();
        this.setImg(Sprite.oneal_right3.getFxImage());
    }

    public void goDown() {
        super.goDown();
        this.setImg(Sprite.oneal_right3.getFxImage());
    }

    @Override
    public void update() {
        double distanceX = Math.abs(bomber.x - x);
        double distanceY = Math.abs(bomber.y - y);
        if(distanceX < 5) {
            if (bomber.x < x) {
                goLeft();
            }
            if (bomber.x > x) {
                goRight();
            }
        }
        if(distanceY < 5) {
            if (bomber.y < y) {
                goUp();
            }
            if (bomber.y > y) {
                goDown();
            }
        }
    }
}
