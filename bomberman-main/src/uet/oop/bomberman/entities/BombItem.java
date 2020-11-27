package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public class BombItem extends Item {
    public BombItem(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void powerUp(Bomber bomber) {
        bomber.setBombRemain(bomber.getBombRemain() + 1);
    }

    @Override
    public void update() {

    }
}
