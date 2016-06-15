package model;


import java.awt.*;

/**
 * Created by Dawid on 2016-04-12.
 */
public class Tank extends Rectangle{
    private int playerId;
    private int lives;
    private Direction direction;
    private boolean isMoving;

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }





    public Tank(int playerId, int lives, int positionX, int positionY){
        super(positionX,positionY,50,50);
        this.playerId = playerId;
        this.lives =lives;
        this.isMoving =false;
        this.direction = Direction.LEFT;
    }
}
