package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.maps.Map;

import java.util.List;

public class Balloom extends Enemy {
    private int speed = 1;

    public Balloom(int x, int y, Image img) {
        super(x, y, img);
    }


    @Override
    public void update() {
        caculateMove(speed);
        if(checkMove() == true) {
            move();
        }
    }


}
