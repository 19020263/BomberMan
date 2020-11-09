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
        if(up == 0) {
            this.setImg(Sprite.player_up.getFxImage());
            up = 1;
        } else if(up == 1) {
            this.setImg(Sprite.player_up_1.getFxImage());
            up = 2;
        } else if(up == 2) {
            this.setImg(Sprite.player_up_2.getFxImage());
            up = 0;
        }
        super.goUp();
    }

    public void goRight() {
        if(right == 0) {
            this.setImg(Sprite.player_right_1.getFxImage());
            right = 1;
        } else if(right == 1) {
            this.setImg(Sprite.player_right_2.getFxImage());
            right = 2;
        } else if(right == 2) {
            this.setImg(Sprite.player_right.getFxImage());
            right = 0;
        }
        super.goRight();
    }

    public void goDown() {
        if(down == 0) {
            this.setImg(Sprite.player_down.getFxImage());
            down = 1;
        } else if(down == 1) {
            this.setImg(Sprite.player_down_1.getFxImage());
            down = 2;
        } else if(down == 2) {
            this.setImg(Sprite.player_down_2.getFxImage());
            down = 0;
        }
        super.goDown();
    }

    public void goLeft() {
        if(left == 0) {
            this.setImg(Sprite.player_left.getFxImage());
            left = 1;
        } else if(left == 1) {
            this.setImg(Sprite.player_left_1.getFxImage());
            left = 2;
        } else if(left == 2) {
            this.setImg(Sprite.player_left_2.getFxImage());
            left = 0;
        }
        super.goLeft();
    }
    @Override
    public void update() {
        if (keyCode == KeyCode.UP) {
            goUp();
        }
        else if(keyCode == KeyCode.DOWN) {
            goDown();
        }
        else if(keyCode == KeyCode.LEFT) {
            goLeft();
        }
        else if(keyCode == KeyCode.RIGHT) {
            goRight();
        }
    }

}
