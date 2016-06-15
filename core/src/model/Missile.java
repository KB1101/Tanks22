package model;

import java.awt.*;
import com.mygdx.tanks.Constants;

public class Missile extends Rectangle {
    private Direction direction;
    private Tank shooter;
    private int endX;
    private int endY;

    public Missile(Tank shooter, Direction direction){
        super();
        this.width = 13;
        this.height = 10;
        if (direction == Direction.LEFT || direction == Direction.DOWN)
        {
            this.endX = 0;
            this.endY = 0;
        } else {
            this.endX = Constants.WIDTH;
            this.endY = Constants.HEIGHT;
        }
        this.direction = shooter.getDirection();
        this.shooter = shooter;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Tank getShooter() {
        return this.shooter;
    }

    public void setShooter(Tank shooter) {
        this.shooter = shooter;
    }
}
