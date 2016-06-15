package model;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import com.mygdx.tanks.Constants;
import com.mygdx.tanks.SocketWorker;

public class Board {
    public ArrayList <Block> objectsList;
    public ArrayList <Missile> missilesList;
    public ArrayList <Tank> tanksList;

    public static final int PORT = 8088;
    public static final String ip = "localhost";

    public Board(String path) {
        objectsList = new ArrayList<Block>();
        missilesList = new ArrayList<Missile>();
        tanksList = new ArrayList<Tank>();
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
                        case '-': {
                            break;
                        }
                        default: {
                            this.tanksList.add(new Tank(sign-'0',5, j*25, Constants.HEIGHT -i*25));
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Nie znaleziono pliku!");
        } catch (IOException ex) {
            ex.getMessage();
        }
        createSocket();

    }
    private  void createSocket(){
        Socket socket = new Socket(ip, PORT);
        SocketWorker sw = new SocketWorker(socket,this);
        new Thread(sw).start();
    }
}
