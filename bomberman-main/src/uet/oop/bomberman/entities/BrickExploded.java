package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

public class BrickExploded extends Flame {
    public BrickExploded(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        setLayer(3);
    }

    @Override
    public void updateImg() {
        img = Sprite.movingSprite(Sprite.brick_exploded, Sprite.brick_exploded1, Sprite.brick_exploded2, timeCounter, 30).getFxImage();
    }
}
