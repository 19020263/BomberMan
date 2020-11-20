package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

import java.util.Random;

public class Oneal extends MoveEntity {
    protected Entity bomber;


    public Oneal(int x, int y, Image img, Entity bomber) {
        super( x, y, img);
        this.bomber = bomber;
        speed = 8;
    }


    @Override
    public void update() {

    }
}
