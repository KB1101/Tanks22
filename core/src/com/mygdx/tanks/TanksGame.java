package com.mygdx.tanks;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

import static model.Direction.LEFT;

public class TanksGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture greenTankTexture, redTankTexture, blueTankTexture, orangeTankTexture;
    Texture shrubTexture, brickTexture, stoneTexture, missileTexture;
    //Tank tank = new Tank(1,5, Constants.TANK_START_X * Constants.TANK_SIZE, Constants.TANK_START_Y * Constants.TANK_SIZE);
    @Deprecated int activePlayerId = 1;
    Board board;

    private Date date;
    private long timeStart;
    private long timeEnd;
    private long reloadTime; //milisekundy
    private double missileSpeed;     // jednostki odswiezen


    @Override
    public void create () {
        board = new Board("plansza.txt");
        batch = new SpriteBatch();
        stoneTexture = new Texture("niezniszczalny.png");
        redTankTexture = new Texture("czerwonyCzolg.png");
        blueTankTexture = new Texture("niebieskiCzolg.png");
        orangeTankTexture = new Texture("zoltyCzolg.png");
        greenTankTexture = new Texture("zielonyCzolg.png");
        shrubTexture = new Texture("krzak.png");
        brickTexture = new Texture("cegla.png");
        missileTexture = new Texture("pocisk.png");

        this.date = new Date();
        this.timeStart = date.getTime();
        this.timeEnd = date.getTime();
        this.reloadTime = 500; //ms
        this.missileSpeed = 600.0; // jednostek
    }

    @Override
    public void render () {
        update();
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        drawMissiles();
        //launchMissile();
        removeRedundantMissiles();
        drawBoard();
        batch.end();
        this.date = new Date(); // aktualizuje czas
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }


    @Override
    public void resume() {
        super.resume();
    }

    private void checkForCollisions(Block object, int j)
    {
        if(object.getSymbol() != 'Z') {
            Rectangle rect = new Rectangle((int) object.getX(), (int) object.getY(), 25, 25);
            for (int i = 0; i < this.board.missilesList.size(); i++) {
                if (rect.contains(this.board.missilesList.get(i).getCenterX(), this.board.missilesList.get(i).getCenterY())) {
                    this.board.missilesList.remove(i);
                    board.objectsList.get(j).setStamina(board.objectsList.get(j).getStamina() -1);
                }
            }
        }
    }

    private void updateBoardState()
    {
        ArrayList<Block> temp = new ArrayList<Block>();
        for (Block object: board.objectsList){
            if (object.getStamina() != 0){
                temp.add(object);
            }
        }
        board.objectsList.clear();
        for (Block object:temp){
            board.objectsList.add(object);
        }
        deleteUselessTanks();
    }

    private Texture returnProperTexture(int id)
    {
        switch (id){
            case 0:{
                return redTankTexture;
            }
            case 1:{
                return  greenTankTexture;
            }
            case 2:{
                return orangeTankTexture;
            }
            case 3:{
                return blueTankTexture;
            }
            default:{
                return null;
            }
        }
    }

    private void drawTanks()
    {
        for (Tank tank:board.tanksList){
            double x = tank.getX();
            double y = tank.getY();
            batch.draw(new TextureRegion(returnProperTexture(tank.getPlayerId())), (float)x, (float)y,
                    (float) tank.getCenterX()-(float)x, (float) tank.getCenterY()-(float)y,
                    (float) tank.getWidth(), (float) tank.getHeight(), 1f, 1f, (float) tank.getDirection().getValue()*90);
        }
    }

    private void drawBoard(){
        drawTanks();
        int j=0;
        for (Block object: board.objectsList){
            switch (object.getSymbol()){
                case 'C':{
                    batch.draw(brickTexture, (int)object.getX(), (int)object.getY());
                    break;
                }
                case 'K':{
                    batch.draw(stoneTexture, (int)object.getX(), (int)object.getY());
                    break;
                }
                case 'Z':{
                    batch.draw(shrubTexture, (int)object.getX(), (int)object.getY());
                    break;
                }
            }
            checkForCollisions(object, j);
            // szybka kolizja pociskow -- potem zastapi ja serwer
            j++;
        }
        updateBoardState();
    }

    private void updateMissilePosition(Missile missile)
    {
        double missileStep = this.missileSpeed *( 1.0 / Gdx.graphics.getFramesPerSecond()); // predkosc = jednoski / ramke
        switch ( missile.getDirection()){
            case LEFT: {
                missile.x -= missileStep;
                break;
            }
            case RIGHT: {
                missile.x += missileStep;
                break;
            }
            case UP: {
                missile.y += missileStep;
                break;
            }
            case DOWN: {
                missile.y -= missileStep;
                break;
            }
        }
    }

    private void drawMissiles(){
        //Podobna funkcja jak dla rysowania czołgu
        for (Missile missile : board.missilesList){
            batch.draw(new TextureRegion(missileTexture),
                    (float) missile.getX(), (float) missile.getY(),
                    (float) missile.getCenterX()-(float) missile.getX(), (float) missile.getCenterY()-(float) missile.getY(),
                    (float) missile.getWidth(), (float) missile.getHeight(),
                    1f, 1f,
                    (float) missile.getDirection().getValue()*90);
            updateMissilePosition(missile);
        }
    }

    private void removeRedundantMissiles(){
        //Usuwanie pocisków po wylocie z planszy
        for (int i = 0; i< board.missilesList.size(); i++){
            Missile missile = board.missilesList.get(i);
            switch(missile.getDirection()){
                case LEFT: {
                    if (missile.getX() <= 0) board.missilesList.remove(i);
                    break;
                }
                case RIGHT: {
                    if (missile.getX() >= Constants.WIDTH) board.missilesList.remove(i);
                    break;
                }
                case UP: {
                    if (missile.getY() >= Constants.HEIGHT) board.missilesList.remove(i);
                    break;
                }
                case DOWN: {
                    if (missile.getY() <= 0) board.missilesList.remove(i);
                    break;


                }
            }
        }
    }

    private Point getMissileStartingPosition()
    {
        Point start = new Point();
        start.x = 0;
        start.y = 0;
        Tank tank = board.tanksList.get(activePlayerId);
        switch (tank.getDirection()) {
            case LEFT: {
                start.x = (int) (tank.getX()-10);
                start.y = (int) (tank.getY() + tank.height / 2 - 5);
                break;
            }
            case RIGHT: {
                start.x = (int) (tank.getX() + tank.width);
                start.y = (int) (tank.getY() + tank.height / 2 - 5);
                break;
            }
            case UP: {
                start.x = (int) (tank.getX() + tank.width / 2 - 5);
                start.y = (int) (tank.getY() + tank.height);
                break;
            }
            case DOWN: {
                start.x = (int) (tank.getX() + tank.width / 2 - 5);
                start.y = (int) (tank.getY()-5);
                break;
            }

        }
        return start;
    }

    private void launchMissile(){
        Tank tank = board.tanksList.get(activePlayerId);

        if(this.timeStart >= timeEnd) {  //sprawdza czy upłyna zadany czas przaładowania
            Point start = getMissileStartingPosition();
            //start_x i start_y to początkowa pozycja pocisku
            Missile missile = new Missile(tank, tank.getDirection());
            missile.x = start.x;
            missile.y = start.y;
            //board.missilesList.add(missile);
            String kordy = "11 ";
            kordy+=Integer.toString(missile.x)+" ";
            kordy+=Integer.toString(missile.y)+" ";
            kordy+=Integer.toString(tank.getDirection().getValue());

            timeEnd = this.date.getTime() + this.reloadTime;
            this.board.send(kordy);
        }
    }

    private void collisionDetector(int  x, int y){
        Tank activeTank = board.tanksList.get(activePlayerId);
        //uniemożliwienie wyjechania poza planszę
        if (activeTank.getX() >= Constants.WIDTH - Constants.TANK_SIZE || activeTank.getX() <=0 ||
                activeTank.getY() <= 0 || activeTank.getY() >= Constants.HEIGHT - Constants.TANK_SIZE)
        {
            activeTank.x = x;
            activeTank.y = y;
        } else {  //sprawdzenie kolizji, czyli dzięki temu czołg nie wjeżdża na bloki (chyba, że to zarośla)
            boolean isCollision = false;
            for (Block object : board.objectsList)
            {
                if (object.intersection(activeTank).width >2 && object.intersection(activeTank).height >2 && object.getSymbol() != 'Z')
                {
                    isCollision = true;
                    break;
                }
            }
            if (isCollision)   //Jeśli wystąpiła kolizja z blokiem, to cofnij na pole sprzed zmiany
            {
                activeTank.x = x;
                activeTank.y = y;
            }
        }
        tankWithTankCollision(activeTank, x, y);
        int missileId=-1;
        int i=0;
        for (Missile missile:board.missilesList){
            if (activeTank.intersection(missile).width >5 && activeTank.intersection(missile).height >5) {
                activeTank.setLives(activeTank.getLives()-1);
                missileId =i;
            }
            i++;
        }
        if (missileId != -1)
            deleteMissile(missileId);
    }

    private void deleteMissile(int id){
        board.missilesList.remove(id);
    }

    private void tankWithTankCollision(Tank activeTank, int x, int y)
    {
        for (Tank tank:board.tanksList){
            if (tank.equals(activeTank)) continue;      //dla swojego nie sprawdzaj!
            if (activeTank.intersection(tank).width>0 && activeTank.intersection(tank).height >0 ){
                activeTank.x=x;
                activeTank.y=y;
            }
        }
    }

    private void deleteUselessTanks()
    {
        for (int i=0; i<board.tanksList.size(); i++){
            if (board.tanksList.get(i).getLives() == 0)
                board.tanksList.remove(i);
        }
    }

    private void update(){
        this.timeStart = date.getTime();
        Tank tank = board.tanksList.get(activePlayerId);
        int x = (int) tank.getX();
        int y = (int) tank.getY();
        String str=Integer.toString(activePlayerId);
        //Odczyt klawiszy
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            tank.x-= Constants.TANK_SPEED;
            tank.setDirection(LEFT);
            str+=" "+Integer.toString(tank.x)+" "+Integer.toString(tank.y)+" "+Integer.toString(tank.getDirection().getValue());
            this.board.send(str);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            tank.x+= Constants.TANK_SPEED;
            tank.setDirection(Direction.RIGHT);
            str+=" "+Integer.toString(tank.x)+" "+Integer.toString(tank.y)+" "+Integer.toString(tank.getDirection().getValue());
            this.board.send(str);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            tank.y+= Constants.TANK_SPEED;
            tank.setDirection(Direction.UP);
            str+=" "+Integer.toString(tank.x)+" "+Integer.toString(tank.y)+" "+Integer.toString(tank.getDirection().getValue());
            this.board.send(str);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            tank.y-= Constants.TANK_SPEED;
            tank.setDirection(Direction.DOWN);
            str+=" "+Integer.toString(tank.x)+" "+Integer.toString(tank.y)+" "+Integer.toString(tank.getDirection().getValue());
            this.board.send(str);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            this.launchMissile();
        }
        collisionDetector(x,y);
    }


    @Override
    public void dispose() {
        batch.dispose();
        stoneTexture.dispose();
        redTankTexture.dispose();
        blueTankTexture.dispose();
        orangeTankTexture.dispose();
        greenTankTexture.dispose();
        shrubTexture.dispose();
        missileTexture.dispose();
        brickTexture.dispose();
    }

    public TanksGame() {
        super();
    }
}