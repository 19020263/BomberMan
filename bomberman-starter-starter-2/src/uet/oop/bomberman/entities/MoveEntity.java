package uet.oop.bomberman.entities;


import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

public class MoveEntity extends Entity {
    public static final double speed = 0.25;
    protected int up = 0;
    protected int down = 0;
    protected int left = 0;
    protected int right = 0;
    protected double previous_x = 0;
    protected double previous_y = 0;
    public MoveEntity(double x, double y, Image img) {
        super(x,y,img);
    }

    private void getPrevious() {
        x = previous_x;
        y = previous_y;
    }
    private void setPrevious() {
        previous_x = x;
        previous_y = y;
    }

    public void goUp() {
        setPrevious();
        y -= speed;
        move();
    }

    public void goLeft() {
        setPrevious();
        x -= speed;
        move();
    }

    public void goRight() {
        setPrevious();
        x += speed;
        move();
    }

    public void goDown() {
        setPrevious();
        y += speed;
        move();
    }

    public void move() {
        int n = BombermanGame.stillObjects.size();
        int m = Sprite.SCALED_SIZE;
        boolean check = true;
        for(int i = 0; i < n; i++) {
            Entity g = BombermanGame.stillObjects.get(i);
            double _x = x * m;
            double _y = y * m;
            double _gx = g.getX() * m;
            double _gy = g.getY() * m;
            if (((_x < _gx) && (_x + m > _gx)) || ((_gx <= _x) && (m + _gx > _x))) {
                if (((_y < _gy) && (m + _y > _gy)) || ((_gy <= _y) && (m + _gy > _y))) {
                    if(g.layer > layer){
                        check = false;
                        break;
                    }
                }
            }
        }
        if(!check) {
            getPrevious();
        }
        Entity object = new Grass(previous_x , previous_y, Sprite.grass.getFxImage());
        object.render(BombermanGame.gc);
        render(BombermanGame.gc);
    }
    @Override
    public void update() {

    }
}
