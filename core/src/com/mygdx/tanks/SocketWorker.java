package com.mygdx.tanks;

import model.Board;
import model.Direction;
import model.Missile;
import model.Tank;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by Kornel on 2016-06-15.
 */
public class SocketWorker implements Runnable {
    private  Socket socket;
    private Board board;
    private InputStream inputStream;
    private OutputStream outputStream;
    private DataInputStream dis;
    private DataOutputStream dos;
    private BufferedReader bufIn;
    private BufferedWriter bufOut;

    public SocketWorker(Socket socket, Board board){
        this.socket = socket;
        this.board = board;
    }

    public void run(){
        try{
            this.inputStream = this.socket.getInputStream();
            this.outputStream = this.socket.getOutputStream();
           // this.dis = new DataInputStream(inputStream);
           // this.dos = new DataOutputStream(outputStream);
            this.bufIn = new BufferedReader( new InputStreamReader( this.inputStream ) );
            this.bufOut = new BufferedWriter( new OutputStreamWriter( this.outputStream ) );


            SocketAddress sockaddr = this.socket.getRemoteSocketAddress();

            System.out.println("Nawiązano połaczenie z: " + sockaddr.toString() );
            process(bufIn,bufOut);
            //process(dis,dos);
            System.out.println("Klient zakończył połaczenie: " + sockaddr.toString() );

           // dos.close();
            //dis.close();
            bufOut.close();
            bufIn.close();
            inputStream.close();
            outputStream.close();



        } catch (Exception ex){
            System.out.println(ex);
        }
    }


    private void process(BufferedReader bufIn, BufferedWriter bufOut ){
        try {
            char[] buffer = new char[1024];
            int bytes = 0;


            String str;
            str = bufIn.readLine();
            int uid = Integer.parseInt(str);
            this.board.setId(uid);

            System.out.println(uid);

            while ((bytes = bufIn.read(buffer)) != -1) {

                System.out.print(buffer);
                String strw = new String(buffer);
                String[] wtf = strw.split(" ");
                int id = Integer.parseInt(wtf[0]);
                int x = Integer.parseInt(wtf[1]);
                int y = Integer.parseInt(wtf[2]);
                String[] wtf2 = wtf[3].split("\r\n");
                int direction = Integer.parseInt(wtf2[0]);

                if(id>=0 && id <=3){
                    switch (direction){
                        case 0: //left
                        {
                            this.board.tanksList.get(id).setDirection(Direction.LEFT);
                            break;
                        }
                        case 1: //down
                        {
                            this.board.tanksList.get(id).setDirection(Direction.DOWN);
                            break;
                        }
                        case 2: //right
                        {
                            this.board.tanksList.get(id).setDirection(Direction.RIGHT);
                            break;
                        }
                        case 3: //up
                        {
                            this.board.tanksList.get(id).setDirection(Direction.UP);
                            break;
                        }

                    }
                    this.board.tanksList.get(id).setLocation(x,y);

                } else {
                    switch (direction){
                        case 0: //left
                        {
                            Tank tank = new Tank(10, 5, x, y);
                            tank.setDirection(Direction.LEFT);
                            Missile ms = new Missile(tank,Direction.LEFT);
                            ms.x = x;
                            ms.y = y;
                            this.board.missilesList.add(ms);
                            break;
                        }
                        case 1: //down
                        {
                            Tank tank = new Tank(10, 5, x, y);
                            tank.setDirection(Direction.DOWN);
                            Missile ms = new Missile(tank,Direction.DOWN);
                            ms.x = x;
                            ms.y = y;
                            this.board.missilesList.add(ms);
                            break;
                        }
                        case 2: //right
                        {
                            Tank tank = new Tank(10, 5, x, y);
                            tank.setDirection(Direction.RIGHT);
                            Missile ms = new Missile(tank,Direction.RIGHT);
                            ms.x = x;
                            ms.y = y;
                            this.board.missilesList.add(ms);
                            break;
                        }
                        case 3: //up
                        {
                            Tank tank = new Tank(10, 5, x, y);
                            tank.setDirection(Direction.UP);
                            Missile ms = new Missile(tank,Direction.UP);
                            ms.x = x;
                            ms.y = y;
                            this.board.missilesList.add(ms);
                            break;
                        }

                    }
                }



                System.out.println(" ");
                //  akcje
            }

        } catch (Exception ex){
            System.out.println(ex);
        } finally {
            //this.magazyn.delUser();
        }
    }


    public void send(String str){
        try {
            //this.dos.writeBytes(str);
            this.bufOut.write(str);
            this.bufOut.newLine();
            this.bufOut.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
