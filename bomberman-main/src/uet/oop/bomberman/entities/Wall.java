package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public class Wall extends StillEntity {

    public Wall(int x, int y, Image img) {
        super(x, y, img);
        setLayer(5);
    }

    @Override
    public void update() {

    }
}
