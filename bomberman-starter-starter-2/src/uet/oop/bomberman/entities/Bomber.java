package uet.oop.bomberman.entities;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

public class Bomber extends MoveEntity {
    public KeyCode keyCode;
    public Bomber(int x, int y, Image img) {
        super( x, y, img);
        keyCode = null;
        layer = 1;
    }

    public void goUp() {
        super.goUp();
    }

    public void goRight() {
        super.goRight();
    }

    public void goDown() {
        super.goDown();
    }

    public void goLeft() {
        super.goLeft();
    }
    @Override
    public void update() {
        if (keyCode == KeyCode.UP) {
            goUp();
            this.setImg(Sprite.movingSprite(Sprite.player_up, Sprite.player_up_1,
                        Sprite.player_up_2, animate, 20).getFxImage());
        }
        else if(keyCode == KeyCode.DOWN) {
            goDown();
            this.setImg(Sprite.movingSprite(Sprite.player_down, Sprite.player_down_1,
                        Sprite.player_down_2, animate, 20).getFxImage());
        }
        else if(keyCode == KeyCode.LEFT) {
            goLeft();
            this.setImg(Sprite.movingSprite(Sprite.player_left, Sprite.player_left_1,
                        Sprite.player_left_2, animate, 20).getFxImage());
        }
        else if(keyCode == KeyCode.RIGHT) {
            goRight();
            this.setImg(Sprite.movingSprite(Sprite.player_right, Sprite.player_right_1,
                        Sprite.player_right_2, animate, 20).getFxImage());
        }

        setAnimate();
    }

}
