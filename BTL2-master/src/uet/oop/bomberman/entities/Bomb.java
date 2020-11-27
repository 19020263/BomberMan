package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

import java.awt.*;
import java.util.Random;

public class Bomb extends MovingEntity {
    private int timeCounter = 0;
    public boolean exploded;
    public Bomb(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        setLayer(2);
        exploded = false;
    }

    @Override
    public void update() {
        if(isAlive()) {
            if (timeCounter++ == 120) {
                explode();
                img = Sprite.bomb_exploded.getFxImage();
            }else
                img = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, timeCounter, 60).getFxImage();
        }else if(animated < 20){
            animated++;
            img = Sprite.movingSprite(Sprite.bomb_exploded1
                    , Sprite.bomb_exploded2, animated, 20).getFxImage();
        }else
            exploded = true;
    }

    public void explode() {
        Sound.bomb_explo.play();
        Sound.bomb_explo.seek(Sound.bomb_explo.getStartTime());
        explosion e = new explosion(x, y);
        e.render_explosion();
        alive = false;
    }

    public boolean isAllowedToPassThrough(MovingEntity e) {
        Rectangle r1 = getBounds();
        Rectangle r2;
        if (e instanceof Bomber) {
            Bomber bomber = (Bomber) e;
            r2 = new Rectangle(bomber.getX() + 4, bomber.getY() + 4, Sprite.SCALED_SIZE * 3 / 4, Sprite.SCALED_SIZE * 3 / 4);
        } else {
            r2 = new Rectangle(e.getX(), e.getY(), Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
        }
        return r1.intersects(r2);
    }
}
