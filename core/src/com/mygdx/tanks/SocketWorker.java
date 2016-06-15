package com.mygdx.tanks;

import model.Board;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by Kornel on 2016-06-15.
 */
public class SocketWorker implements Runnable {
    private  Socket socket;
    private Board board;
    public SocketWorker(Socket socket, Board board){
        this.socket = socket;
        this.board = board;
    }

    public void run(){
        try{
            InputStream inputStream = this.socket.getInputStream();
            OutputStream outputStream = this.socket.getOutputStream();
            DataInputStream dis = new DataInputStream(inputStream);
            DataOutputStream dos = new DataOutputStream(outputStream);
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
                //  akcje
            }

        } catch (Exception ex){
            System.out.println(ex);
        } finally {
            //this.magazyn.delUser();
        }
    }

}
