package com.mygdx.tanks;

import model.Board;

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

    public SocketWorker(Socket socket, Board board){
        this.socket = socket;
        this.board = board;
    }

    public void run(){
        try{
            this.inputStream = this.socket.getInputStream();
            this.outputStream = this.socket.getOutputStream();
            this.dis = new DataInputStream(inputStream);
            this.dos = new DataOutputStream(outputStream);
            SocketAddress sockaddr = this.socket.getRemoteSocketAddress();

            System.out.println("Nawiązano połaczenie z: " + sockaddr.toString() );
            process(dis,dos);
            System.out.println("Klient zakończył połaczenie: " + sockaddr.toString() );

            dos.close();
            dis.close();
            inputStream.close();
            outputStream.close();



        } catch (Exception ex){
            System.out.println(ex);
        }
    }

    private void process(DataInputStream dis,DataOutputStream dos ){
        try {
            byte[] buffer = new byte[1024];
            int bytes = 0;

            bytes = dis.read(buffer);
            int uid = buffer[0];




            String suid = Integer.toString(uid);
            dos.writeBytes(suid+"\n\r");
            System.out.write(buffer, 0, bytes);



            while ((bytes = dis.read(buffer)) != -1) {
                System.out.write(buffer, 0, bytes);
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
            this.dos.writeBytes(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
