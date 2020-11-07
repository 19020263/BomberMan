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
    }

    public void goRight() {
        super.goRight();
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
    }

    public void goDown() {
        super.goDown();
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
    }

    public void goLeft() {
        super.goLeft();
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

    }
    @Override
    public void update() {
        int index = BombermanGame.entities.size() - 1;
        double x = BombermanGame.entities.get(index).getX();
        double y = BombermanGame.entities.get(index).getY();
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
        //gc.clearRect(x * Sprite.SCALED_SIZE,y * Sprite.SCALED_SIZE, Sprite.SCALED_SIZE,Sprite.SCALED_SIZE);
        Entity object = new Grass(previous_x , previous_y, Sprite.grass.getFxImage());
        object.render(BombermanGame.gc);
        BombermanGame.entities.get(index).render(BombermanGame.gc);
        //render();
    }


}
