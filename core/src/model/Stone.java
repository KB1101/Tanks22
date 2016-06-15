package model;

public class Stone extends Block {

    public Stone(int positionX, int positionY){
        super(positionX, positionY);
        setStamina(100000);
        symbol = 'K';
    }
}
