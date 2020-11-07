package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public class Grass extends Entity {

    public Grass(double x, double y, Image img) {
        super(x, y, img);
        layer = 0;
    }

    @Override
    public void update() {

    }
}
