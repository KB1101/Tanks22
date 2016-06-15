package model;


public class Brick extends Block {
    public Brick(int positionX, int positionY){
        super(positionX,positionY);
        setStamina(1);
        symbol = 'C';
    }

}
