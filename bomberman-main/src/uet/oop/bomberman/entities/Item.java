package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public abstract class Item extends StillEntity {
    public Item(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        setLayer(1);
    }

    public abstract void powerUp(Bomber bomber);
}
