package uet.oop.bomberman.entities;


import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

import java.awt.*;
import java.util.List;

public abstract class MoveEntity extends Entity {
    protected int animate = 0;
    protected int MAX_ANIMATE = 7500;
    protected int previous_x;
    protected int previous_y;
    public MoveEntity(int x, int y, Image img) {
        super(x,y,img);
        previous_x = x;
        previous_y = y;
    }

    public void goRight() {
        previous_x = x;
        previous_y = y;
        if (checkMove()) {
            x += speed;
        }
        reRender();
    }

    public void goLeft() {
        previous_x = x;
        previous_y = y;
        if (checkMove()) {
            x -= speed;
        }
        reRender();
    }


    public boolean checkMove() {
        boolean canMove = true;
        Rectangle re = new Rectangle(previous_x, previous_y, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
        for (Entity entity : BombermanGame.stillObjects) {
            if (entity instanceof Brick || entity instanceof Wall) {
                if (re.intersects(entity.getBounds())) {
                    canMove = false;
                    break;
                }
            }
        }
        return canMove;
    }

    public void reRender() {
        Entity object = new Grass(previous_x , previous_y, Sprite.grass.getFxImage());
        object.render(BombermanGame.gc);
        render(BombermanGame.gc);
    }

    @Override
    public void update() {

    }

    public void setAnimate() {
        if (animate < MAX_ANIMATE) {
            animate++;
        } else {
            animate = 0;
        }
    }
}
