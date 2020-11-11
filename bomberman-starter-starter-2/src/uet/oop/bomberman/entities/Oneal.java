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
        double distance = Math.pow(bomber.x - x, 2) + Math.pow(bomber.y - y, 2);
        if(Math.sqrt(distance) < 7) {
            if (bomber.x < x) {
                goLeft();
                this.setImg(Sprite.movingSprite(Sprite.oneal_left1, Sprite.oneal_left2,
                            Sprite.oneal_left3, animate, 20).getFxImage());
            }
            if (bomber.x > x) {
                goRight();
                this.setImg(Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2,
                            Sprite.oneal_right3, animate, 20).getFxImage());
            }
            if (bomber.y < y) {
                goUp();
                this.setImg(Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2,
                        Sprite.oneal_right3, animate, 20).getFxImage());
            }
            if (bomber.y > y) {
                goDown();
                this.setImg(Sprite.movingSprite(Sprite.oneal_right1, Sprite.oneal_right2,
                        Sprite.oneal_right3, animate, 20).getFxImage());
            }
        }
        setAnimate();
    }
}
