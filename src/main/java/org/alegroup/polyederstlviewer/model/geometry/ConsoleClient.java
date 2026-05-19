package org.alegroup.polyederstlviewer.model.geometry;

import org.alegroup.polyederstlviewer.constants.ConsoleCommandEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConsoleClient implements Runnable{

    private String hostname = null;
    private int port = -1;

    public ConsoleClient(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }


    @Override
    public void run() {

    }

    private Socket openSocket(){
        try {
            return new Socket(this.hostname, this.port);
        }
        catch (IOException e) {
            System.out.println("Something went wrong trying to create the client socket");
            return null;
        }
    }

    private void serverInteraction(Socket client){

        if(client == null){
            return;
        }

        try {
            BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter sendToServer = new PrintWriter(client.getOutputStream(), true);

            synchronized (this) {
                this.wait();
            }

            synchronized (this) {
                this.notifyAll();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
