package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public class Brick extends StillEntity {

    public Brick(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        setLayer(3);
    }

    @Override
    public void update() {

    }
}
