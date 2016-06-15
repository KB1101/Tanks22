package model;

import java.io.*;
import java.util.ArrayList;
import com.mygdx.tanks.Constants;

public class Board {
    public ArrayList <Block> objectsList;
    public ArrayList <Missile> missilesList;

    public Board(String path) {
        objectsList = new ArrayList<Block>();
        missilesList = new ArrayList<Missile>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("plansza.txt"));
            br.read();
            for (int i = 1; i <= 32; i++) {
                String line = br.readLine();
                for (int j = 0; j < 32; j++) {
                    char sign = line.charAt(j * 2);
                    switch (sign) {
                        case 'C': {
                            this.objectsList.add(new Brick(j*25, Constants.HEIGHT -i*25));
                            break;
                        }
                        case 'K': {
                            this.objectsList.add(new Stone(j*25, Constants.HEIGHT -i*25));
                            break;
                        }
                        case 'Z': {
                            this.objectsList.add(new Shrub(j*25, Constants.HEIGHT -i*25));
                            break;
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Nie znaleziono pliku!");
        } catch (IOException ex) {
            ex.getMessage();
        }

    }

}
