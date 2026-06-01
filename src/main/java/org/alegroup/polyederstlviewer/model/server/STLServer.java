package org.alegroup.polyederstlviewer.model.server;

import org.alegroup.polyederstlviewer.model.client.ActiveClientContainer;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// just echoing for now
public class STLServer implements Runnable{

    private final int portNumber;
    private final ConsoleObject console;
    private final String consoleContext;

    private boolean clientConnected = false;

    private volatile ServerSocket server;
    private volatile Socket connectedClient;

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

        while (true){

            this.console.makeOutputToSpecifiedContext("Trying to open server on port: " + this.portNumber, this.consoleContext);


            try {

                server = new ServerSocket(this.portNumber);

                while (!Thread.currentThread().isInterrupted()){

                    this.console.makeOutputToSpecifiedContext("Waiting for client to connect... (Port: " + this.portNumber + ")", this.consoleContext);
                    this.connectedClient = server.accept();

                    this.console.makeOutputToSpecifiedContext("Client connected on port: " + this.portNumber, this.consoleContext);

                    // blocking until exception or client disconnect
                    serveClient(this.connectedClient);
                }

            } catch(IOException e){
                // a real Exception has been thrown. If server socket is closed, then this was intentional by calling stop()
                if(!server.isClosed()){
                    this.console.makeOutputToSpecifiedContext("Error while opening server socket on port: " + this.portNumber, this.consoleContext);
                    System.out.println("Error while opening server socket on port: " + this.portNumber + ", e: " + e.toString());
                }
                return;
            }

        }
    }

    public void stop() {
        try {
            if (server != null && !server.isClosed()) {

                if (this.connectedClient != null && !this.connectedClient.isClosed()) {
                    PrintWriter sendToClient = new PrintWriter(this.connectedClient.getOutputStream(), true);
                    sendToClient.println("SERVER_CLOSE");
                    sendToClient.close();
                    this.connectedClient.close(); // Client gets Exception
                }

                this.console.makeOutputToSpecifiedContext("Server stopped!", this.consoleContext);
                ActiveServerContainer.getInstance().removeServer(this.consoleContext);
                System.out.println("Server closed!!");
                server.close();
            }
        } catch (IOException e) {
            // ignore?
        }
    }


    private void serveClient(Socket client){

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

            // if client is not closed, then the Exception did not happen intentionally
            if(!this.connectedClient.isClosed()){
                this.console.makeOutputToSpecifiedContext("Something went wrong serving the client on port: " + this.portNumber, this.consoleContext);
                System.out.println("Something went wrong serving the client on port: " + this.portNumber + ", e: " + e.toString());
            }
        }
    }
}
