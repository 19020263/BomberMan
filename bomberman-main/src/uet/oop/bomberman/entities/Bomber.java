package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import uet.oop.bomberman.Sound;
import uet.oop.bomberman.graphics.Sprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Bomber extends MovingEntity {
    private int bombRemain;
    private boolean placeBombCommand = false;
    private final List<Bomb> bombs = new ArrayList<>();
    private boolean death;
    private KeyCode direction;

    private int power;

    public Bomber(int x, int y, Image img) {
        super( x, y, img);
        setLayer(1);
        setSpeed(2);
        setBombRemain(1);
        setPower(1);
        direction = null;
        alive = true;
        death = false;
    }

    @Override
    public void update() {
        if(isAlive()) {
            if (direction == KeyCode.LEFT) {
                goLeft();
                Sound.di_chuyen1.play();
                Sound.di_chuyen1.seek(Sound.di_chuyen1.getStartTime());
            }
            if (direction == KeyCode.RIGHT) {
                goRight();
                Sound.di_chuyen1.play();
                Sound.di_chuyen1.seek(Sound.di_chuyen1.getStartTime());
            }
            if (direction == KeyCode.UP) {
                goUp();
                Sound.di_chuyen2.play();
                Sound.di_chuyen2.seek(Sound.di_chuyen2.getStartTime());
            }
            if (direction == KeyCode.DOWN) {
                goDown();
                Sound.di_chuyen2.play();
                Sound.di_chuyen2.seek(Sound.di_chuyen2.getStartTime());
            }
        }else if (animated < 30){
            animated++;
            img = Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2
                    , Sprite.player_dead3, animated, 30).getFxImage();
        }else {
            death = true;
        }
        if (placeBombCommand) {
            placeBomb();
        }
        for (int i = 0; i < bombs.size(); i++) {
            Bomb bomb = bombs.get(i);
            if (bomb.isExploded()) {
                bombs.remove(i--);
                bombRemain++;
            }
        }
    }

    public void handleKeyPressedEvent(KeyCode keyCode) {
        if(isAlive()) {
            if (keyCode == KeyCode.LEFT || keyCode == KeyCode.RIGHT
                    || keyCode == KeyCode.UP || keyCode == KeyCode.DOWN) {
                this.direction = keyCode;
            }
            if (keyCode == KeyCode.SPACE) {
                if (bombRemain > 0) {
                    Sound.dat_bom.play();
                    Sound.dat_bom.seek(Sound.dat_bom.getStartTime());
                }
                placeBombCommand = true;
            }
        }
    }

    public void handleKeyReleasedEvent(KeyCode keyCode) {
        if(isAlive()) {
            if (direction == keyCode) {
                if (direction == KeyCode.LEFT) {
                    img = Sprite.player_left.getFxImage();
                }
                if (direction == KeyCode.RIGHT) {
                    img = Sprite.player_right.getFxImage();
                }
                if (direction == KeyCode.UP) {
                    System.out.println("yes");
                    img = Sprite.player_up.getFxImage();
                }
                if (direction == KeyCode.DOWN) {
                    img = Sprite.player_down.getFxImage();
                }
                direction = null;
            }
            if (keyCode == KeyCode.SPACE) {
                placeBombCommand = false;
            }
        }
    }

    public void goLeft() {
        super.goLeft();
        img = Sprite.movingSprite(Sprite.player_left, Sprite.player_left_1, Sprite.player_left_2, left++, 18).getFxImage();
    }

    public void goRight() {
        super.goRight();
        img = Sprite.movingSprite(Sprite.player_right, Sprite.player_right_1, Sprite.player_right_2, right++, 18).getFxImage();
    }

    public void goUp() {
        super.goUp();
        img = Sprite.movingSprite(Sprite.player_up, Sprite.player_up_1, Sprite.player_up_2, up++, 18).getFxImage();
    }

    public void goDown() {
        super.goDown();
        img = Sprite.movingSprite(Sprite.player_down, Sprite.player_down_1, Sprite.player_down_2, down++, 18).getFxImage();
    }
    
    public void placeBomb() {
        if (bombRemain > 0) {
            int xB = (int) Math.round((x) / (double) Sprite.SCALED_SIZE);
            int yB = (int) Math.round((y + 4) / (double) Sprite.SCALED_SIZE);
            for (Bomb bomb : bombs) {
                if (xB * Sprite.SCALED_SIZE == bomb.getX() && yB * Sprite.SCALED_SIZE == bomb.getY()) return;
            }
            Bomb bomb = new Bomb(xB, yB, Sprite.bomb.getFxImage());
            bomb.setPower(power);
            bombs.add(bomb);
            bombRemain--;
        }
    }
    public int getBombRemain() {
        return bombRemain;
    }

    public void setBombRemain(int bombRemain) {
        this.bombRemain = bombRemain;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isDeath(){
        return death;
    }
    public void die() {
        Sound.chet.play();
        Sound.chet.seek(Sound.chet.getStartTime());
        alive = false;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        Sound.get_item.play();
        Sound.get_item.seek(Sound.get_item.getStartTime());
        this.power = power;
    }

    public Rectangle getDesBounds() {
        return new Rectangle(desX, desY + 8, Sprite.SCALED_SIZE * 3 / 4, Sprite.SCALED_SIZE * 3 / 4);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y + 8, Sprite.SCALED_SIZE * 3 / 4, Sprite.SCALED_SIZE * 3 / 4);
    }

    public void setImg(Image img) {
        this.img = img;
    }

}
