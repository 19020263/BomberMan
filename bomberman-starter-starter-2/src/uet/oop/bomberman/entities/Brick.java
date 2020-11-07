package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public class Brick extends Entity {
    public Brick(int x, int y, Image img) {
        super( x, y, img);
        layer = 2;
    }
    @Override
    public void update() {

    }
}
