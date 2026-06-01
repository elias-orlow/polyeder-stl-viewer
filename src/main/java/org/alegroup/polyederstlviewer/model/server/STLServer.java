package org.alegroup.polyederstlviewer.model.server;

import org.alegroup.polyederstlviewer.model.commands.ConsoleObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// just echoing for now
public class STLServer implements Runnable{

    private int portNumber;
    private ConsoleObject console;
    private String consoleContext;

    private boolean clientConnected = false;

    public STLServer(int portNumber, ConsoleObject console, String consoleContext){
        this.portNumber = portNumber;
        this.console = console;
        this.consoleContext = consoleContext;
    }

    public boolean isClientConnected(){
        return this.clientConnected;
    }


    @Override
    public void run(){

        ServerSocket server = null;
        try {
            server = new ServerSocket(this.portNumber);
        } catch (IOException e) {

            this.console.makeOutputToSpecifiedContext("Error while opening server socket on port: " + this.portNumber, this.consoleContext);
            return;
        }

        this.console.makeOutputToSpecifiedContext("Waiting for client to connect... (Port: " + this.portNumber + ")", this.consoleContext);
        Socket client = null;

        try {
            client = server.accept();
            clientConnected = true;

        } catch (IOException e) {

            this.console.makeOutputToSpecifiedContext("Error while waiting for client to connect on port: " + this.portNumber, this.consoleContext);
            return;
        }

        this.console.makeOutputToSpecifiedContext("Client connected on port: " + this.portNumber, this.consoleContext);
        serveClient(client);
    }

    public void serveClient(Socket client){

        try{
            BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter sendToClient = new PrintWriter(client.getOutputStream(), true);

            String line;
            while ((line = inputFromClient.readLine()) != null){
                if(!line.isEmpty()){
                    this.console.makeOutputToSpecifiedContext("Client sent '" + line + "'", this.consoleContext);

                    // Should react to clients commands here idk how yet
                }
            }


            // client disconnected when line == null
            this.console.makeOutputToSpecifiedContext("Client disconnected! Closing streams...", this.consoleContext);

            // close
            inputFromClient.close();
            sendToClient.close();
            client.close();

        } catch (IOException e) {
            this.console.makeOutputToSpecifiedContext("Something went wrong serving the client on port: " + this.portNumber, this.consoleContext);
            // must remove itself from global list of servers based on its context
            // or wait for a new connection to this port? Until .stop is called on the thread
        }
    }
}
