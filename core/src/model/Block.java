package model;


import java.awt.*;


public class Block extends Rectangle{
    protected int stamina;
    protected char symbol;

    public Block(int positionX, int positionY){
        super(positionX, positionY, 25, 25);
    }


    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }


}
