package model;



public class Shrub extends Block {
    public Shrub(int positionX, int positionY){
        super(positionX,positionY);
        setStamina(100000);
        symbol = 'Z';
    }
}
