package uet.oop.bomberman.entities;

import javafx.scene.image.Image;

public abstract class Flame extends StillEntity {
    protected int timeCounter = 0;
    private boolean onFire;
    public Flame(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        onFire = true;
    }

    @Override
    public void update() {
        if (onFire) {
            updateImg();
            if (timeCounter++ == 30) {
                onFire = false;
            }
        }
    }

    public abstract void updateImg();

    public boolean isOnFire() {
        return onFire;
    }
}
