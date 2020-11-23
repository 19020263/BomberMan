package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public class Portal extends StillEntity {
    public Portal(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        setLayer(1);
    }

    @Override
    public void update() {

    }
}
