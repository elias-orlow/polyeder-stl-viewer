package org.alegroup.polyederstlviewer.model.client;

import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class STLClient implements Runnable {

    private final String hostname;
    private final int portNumber;

    // Client (and Server) have a single consoleContext they might write to. Outside of it they cannot make outputs or take inputs
    private final ConsoleObject console;
    private final String consoleContext;

    // waiting for commands send to this thread
    private final BlockingQueue<String> commandQueue = new LinkedBlockingQueue<>();

    // closing the socket correctly
    private volatile Socket client;

    public STLClient(String hostname, int portNumber, ConsoleObject console, String consoleContext){

        this.hostname = hostname;
        this.portNumber = portNumber;
        this.console = console;
        this.consoleContext = consoleContext;
    }


    @Override
    public void run() {

        // open socket
        try {
            client = new Socket(this.hostname, this.portNumber);
            this.console.makeOutputToSpecifiedContext("Successfully connected socket to " + this.hostname + " on port: " + this.portNumber, this.consoleContext);

        } catch (IOException e) {
            this.console.makeOutputToSpecifiedContext("Something went wrong opening a client socket to " + this.hostname + " on port: " + this.portNumber, this.consoleContext);
        }

        sendCommandToServer();
    }

    public void stop() {
        try {
            if (client != null && !client.isClosed()) {
                this.console.makeOutputToSpecifiedContext("Client stopped!", this.consoleContext);
                ActiveClientContainer.getInstance().removeClient(this.consoleContext);
                client.close(); // Server receives null in stream
            }
        } catch (IOException e) {
            // ignore?
        }
    }

    private void sendCommandToServer(){

        if(client == null){
            return;
        }

        try {
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter toServer = new PrintWriter(client.getOutputStream(), true);

            this.console.makeOutputToSpecifiedContext("Please input your command!", this.consoleContext);

            // Instantly stop client to test server behaviour
            //stop();

            while (true){

                // Blocks until inputCommand is called
                String command = this.commandQueue.take();

                if(command.isEmpty()){
                    break;
                }

                toServer.println(command);
            }

            fromServer.close();
            toServer.close();
            client.close();


        } catch (IOException | InterruptedException e) {
            this.console.makeOutputToSpecifiedContext("Something went wrong with the client!", this.consoleContext);
            stop();
        }
    }

    public void inputCommand(String command){
        this.commandQueue.add(command);
    }
}
