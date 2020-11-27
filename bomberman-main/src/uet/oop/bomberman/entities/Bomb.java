package uet.oop.bomberman.entities;

import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bomb extends MovingEntity {
    private int timeCounter = 0;
    private final List<Flame> flames = new ArrayList<>();
    private int leftRange;
    private int rightRange;
    private int upperRange;
    private int lowerRange;
    private boolean leftBounded;
    private boolean rightBounded;
    private boolean upperBounded;
    private boolean lowerBounded;

    boolean exploded = false;

    public Bomb(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        setLayer(2);
        leftBounded = false;
        rightBounded = false;
        upperBounded = false;
        lowerBounded = false;
    }

    @Override
    public void update() {
        if (alive) {
            img = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, timeCounter, 60).getFxImage();
            if (timeCounter++ == 120) {
                alive = false;
            }
        } else  {
            for (int i = flames.size() - 1; i >= 0; i--) {
                Flame flame = flames.get(i);
                if (flame.isOnFire()) flame.update();
                else flames.remove(i);
                if (flames.isEmpty()) exploded = true;
            }
        }
    }

    public void getExplodedBy(Bomb bomb) {
        alive = false;
        if (bomb.getX() > x) {
            if (bomb.isLeftBounded() && bomb.getLeftRange() >= (bomb.getX() - x) / 32) {
                setLeftRange(bomb.getLeftRange() - (bomb.getX() - x) / 32);
            }
            if (rightRange > (bomb.getX() - x) / 32 + bomb.getRightRange()) {
                setRightRange((bomb.getX() - x) / 32 + bomb.getRightRange());
            }
        }
        if (bomb.getX() < x) {
            if (bomb.isRightBounded() && bomb.getRightRange() >= (x - bomb.getX()) / 32) {
                setRightRange(bomb.getRightRange() - (x - bomb.getX()) / 32);
            }
            if (leftRange > (x - bomb.getX()) / 32 + bomb.getLeftRange()) {
                setLeftRange((x - bomb.getX()) / 32 + bomb.getLeftRange());
            }
        }
        if (bomb.getY() > y) {
            if (bomb.isUpperBounded() && bomb.getUpperRange() >= (bomb.getY() - y) / 32){
                setUpperRange(bomb.getUpperRange() - (bomb.getY() - y) / 32);
            }
            if (lowerRange > (bomb.getY() - y) / 32 + bomb.getLowerRange()) {
                setLowerRange((bomb.getY() - y) / 32 + bomb.getLowerRange());
            }
        }
        if (bomb.getY() < y) {
            if (bomb.isLowerBounded() && bomb.getLowerRange() >= (y - bomb.getY()) / 32) {
                setLowerRange(bomb.getLowerRange() - (y - bomb.getY()) / 32);
            }
            if (upperRange > (y - bomb.getY()) / 32 + bomb.getUpperRange()) {
                setUpperRange((y - bomb.getY()) / 32 + bomb.getUpperRange());
            }
        }
    }

    public boolean isExploded() {
        return exploded;
    }

    public void addFlame(List<StillEntity> board) {
        List<Flame> leftFlames = new ArrayList<>();
        List<Flame> rightFlames = new ArrayList<>();
        List<Flame> upperFlames = new ArrayList<>();
        List<Flame> lowerFlames = new ArrayList<>();
        List<StillEntity> onLeftRange = new ArrayList<>();
        List<StillEntity> onRightRange = new ArrayList<>();
        List<StillEntity> onUpperRange = new ArrayList<>();
        List<StillEntity> onLowerRange = new ArrayList<>();
        int xBUnit = x / 32;
        int yBUnit = y / 32;
        for (StillEntity stillEntity : board) {
            int xUnit = stillEntity.getX() / 32;
            int yUnit = stillEntity.getY() / 32;
            //Upper flame
            if (upperRange > 0 && xUnit == xBUnit && yUnit >= yBUnit - upperRange && yUnit <= yBUnit - 1) {
                if (stillEntity instanceof Wall) {
                    upperRange = yBUnit - yUnit - 1;
                    upperBounded = true;
                } else if (stillEntity instanceof Brick) {
                    upperRange = yBUnit - yUnit - 1;
                    upperBounded = true;
                    onUpperRange.clear();
                    upperFlames.clear();
                    upperFlames.add(new BrickExploded(xUnit, yUnit, Sprite.brick_exploded.getFxImage()));
                    onUpperRange.add(stillEntity);
                } else if (stillEntity instanceof Item) {
                    onUpperRange.add(stillEntity);
                } else {
                    if (yUnit == yBUnit - upperRange) {
                        upperFlames.add(new VerticalTopLastFlame(xUnit, yUnit, Sprite.explosion_vertical_top_last.getFxImage()));
                    } else {
                        upperFlames.add(new VerticalFlame(xUnit, yUnit, Sprite.explosion_vertical.getFxImage()));
                    }
                }
            }
            //Left flame
            if (leftRange > 0 && yUnit == yBUnit && xUnit >= xBUnit - leftRange && xUnit <= xBUnit - 1) {
                if (stillEntity instanceof Wall) {
                    leftRange = xBUnit - xUnit - 1;
                    leftBounded = true;
                } else if (stillEntity instanceof Brick) {
                    leftRange = xBUnit - xUnit - 1;
                    leftBounded = true;
                    onLeftRange.clear();
                    leftFlames.clear();
                    leftFlames.add(new BrickExploded(xUnit, yUnit, Sprite.brick_exploded.getFxImage()));
                    onLeftRange.add(stillEntity);
                } else if (stillEntity instanceof Item) {
                    onLeftRange.add(stillEntity);
                } else {
                    if (xUnit == xBUnit - leftRange) {
                        leftFlames.add(new HorizontalLeftLastFlame(xUnit, yUnit, Sprite.explosion_horizontal_left_last.getFxImage()));
                    } else {
                        leftFlames.add(new HorizontalFlame(xUnit, yUnit, Sprite.explosion_horizontal.getFxImage()));
                    }
                }
            }
            //Right flame
            if (rightRange > 0 && yUnit == yBUnit && xUnit >= xBUnit + 1 && xUnit <= xBUnit + rightRange) {
                if (stillEntity instanceof Wall) {
                    rightRange = xUnit - xBUnit - 1;
                    rightBounded = true;
                } else if (stillEntity instanceof Brick) {
                    rightRange = xUnit - xBUnit - 1;
                    rightBounded = true;
                    rightFlames.add(new BrickExploded(xUnit, yUnit, Sprite.brick_exploded.getFxImage()));
                    onRightRange.add(stillEntity);
                } else if (stillEntity instanceof Item) {
                    onRightRange.add(stillEntity);
                } else {
                    if (xUnit == xBUnit + rightRange) {
                        rightFlames.add(new HorizontalRightLastFlame(xUnit, yUnit, Sprite.explosion_horizontal_right_last.getFxImage()));
                    } else {
                        rightFlames.add(new HorizontalFlame(xUnit, yUnit, Sprite.explosion_horizontal.getFxImage()));
                    }
                }
            }
            //Lower flame
            if (lowerRange > 0 && xUnit == xBUnit && yUnit >= yBUnit + 1 && yUnit <= yBUnit + lowerRange) {
                if (stillEntity instanceof Wall) {
                    lowerRange = yUnit - yBUnit - 1;
                    lowerBounded = true;
                } else if (stillEntity instanceof Brick) {
                    lowerRange = yUnit - yBUnit - 1;
                    lowerBounded = true;
                    lowerFlames.add(new BrickExploded(xUnit, yUnit, Sprite.brick_exploded.getFxImage()));
                    onLowerRange.add(stillEntity);
                } else if (stillEntity instanceof Item) {
                    onLowerRange.add(stillEntity);
                } else {
                    if (yUnit == yBUnit + lowerRange) {
                        lowerFlames.add(new VerticalDownLastFlame(xUnit, yUnit, Sprite.explosion_vertical_down_last.getFxImage()));
                    } else {
                        lowerFlames.add(new VerticalFlame(xUnit, yUnit, Sprite.explosion_vertical.getFxImage()));
                    }
                }
            }
        }
        flames.add(new BombExploded(xBUnit, yBUnit, Sprite.bomb_exploded.getFxImage()));
        flames.addAll(leftFlames);
        flames.addAll(rightFlames);
        flames.addAll(upperFlames);
        flames.addAll(lowerFlames);
        board.removeAll(onLeftRange);
        board.removeAll(onRightRange);
        board.removeAll(onUpperRange);
        board.removeAll(onLowerRange);
    }

    public List<Flame> getFlames() {
        return flames;
    }

    public boolean isAllowedToPassThrough(MovingEntity e) {
        Rectangle r1 = getBounds();
        Rectangle r2;
        if (e instanceof Bomber) {
            Bomber bomber = (Bomber) e;
            r2 = bomber.getBounds();
        } else {
            r2 = e.getBounds();
        }
        return r1.intersects(r2);
    }

    public void setPower(int power) {
        setLeftRange(power);
        setRightRange(power);
        setUpperRange(power);
        setLowerRange(power);
    }

    public int getLeftRange() {
        return leftRange;
    }

    public void setLeftRange(int leftRange) {
        this.leftRange = leftRange;
    }

    public int getRightRange() {
        return rightRange;
    }

    public void setRightRange(int rightRange) {
        this.rightRange = rightRange;
    }

    public int getUpperRange() {
        return upperRange;
    }

    public void setUpperRange(int upperRange) {
        this.upperRange = upperRange;
    }

    public int getLowerRange() {
        return lowerRange;
    }

    public void setLowerRange(int lowerRange) {
        this.lowerRange = lowerRange;
    }

    public boolean isLeftBounded() {
        return leftBounded;
    }

    public boolean isRightBounded() {
        return rightBounded;
    }

    public boolean isUpperBounded() {
        return upperBounded;
    }

    public boolean isLowerBounded() {
        return lowerBounded;
    }
}
