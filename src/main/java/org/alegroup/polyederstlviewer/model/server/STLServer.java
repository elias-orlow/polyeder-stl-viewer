package org.alegroup.polyederstlviewer.model.server;

import com.google.gson.Gson;
import org.alegroup.polyederstlviewer.constants.ServerConstants;
import org.alegroup.polyederstlviewer.model.client.RotateObjectJSON;
import org.alegroup.polyederstlviewer.model.client.TranslateObjectJSON;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;
import org.alegroup.polyederstlviewer.model.rendering.SceneModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server responsible for receiving JSON-based transformation commands from a client
 * and applying them to the 3D scene. The server supports rotation, translation, and
 * general message forwarding. It operates on a dedicated thread and maintains its own
 * connection state.
 */
public class STLServer implements Runnable
{

    /**
     * Port number on which the server listens for incoming client connections.
     */
    private final int portNumber;

    /**
     * Console object used for outputting server-related messages.
     */
    private final ConsoleObject console;

    /**
     * Context identifier used for routing console output.
     */
    private final String consoleContext;

    /**
     * Indicates whether a client is currently connected.
     */
    private boolean clientConnected = false;

    /**
     * Server socket instance used for accepting client connections.
     */
    private volatile ServerSocket server;

    /**
     * The currently connected client socket.
     */
    private volatile Socket connectedClient;

    /**
     * Constructs a new STLServer instance.
     *
     * @param portNumber     the port on which the server should listen
     * @param console        the console used for output messages
     * @param consoleContext the context for console output routing
     * @precondition console != null AND consoleContext != null
     * @postcondition A new server instance is created with the given configuration
     */
    public STLServer (int portNumber, ConsoleObject console, String consoleContext)
    {
        this.portNumber = portNumber;
        this.console = console;
        this.consoleContext = consoleContext;
    }

    /**
     * Indicates whether a client is currently connected to the server.
     *
     * @return true if a client is connected, false otherwise
     * @precondition none
     * @postcondition Returns the current connection state
     */
    public boolean isClientConnected ()
    {
        return this.clientConnected;
    }

    /**
     * Main server loop. Continuously attempts to open a server socket, waits for client
     * connections, and delegates incoming data to {@link #serveClient(Socket)}.
     *
     * @precondition none
     * @postcondition Server listens for connections until interrupted or stopped
     */
    @Override
    public void run ()
    {

        while (true)
        {

            console.makeOutputToSpecifiedContext(
                    ServerConstants.MSG_OPENING_SERVER + portNumber,
                    consoleContext
            );

            try
            {

                server = new ServerSocket(portNumber);

                while (!Thread.currentThread().isInterrupted())
                {

                    console.makeOutputToSpecifiedContext(
                            ServerConstants.MSG_WAITING_FOR_CLIENT + portNumber + ")",
                            consoleContext
                    );

                    connectedClient = server.accept();

                    console.makeOutputToSpecifiedContext(
                            ServerConstants.MSG_CLIENT_CONNECTED + portNumber,
                            consoleContext
                    );

                    serveClient(connectedClient);
                }

            } catch (IOException e)
            {

                if (!server.isClosed())
                {
                    console.makeOutputToSpecifiedContext(
                            ServerConstants.MSG_SERVER_SOCKET_ERROR + portNumber,
                            consoleContext
                    );
                }
                return;
            }
        }
    }

    /**
     * Stops the server and closes all associated sockets.
     *
     * @precondition none
     * @postcondition Server socket and client connection are closed
     */
    public void stop ()
    {
        try
        {
            if (server != null && !server.isClosed())
            {

                if (connectedClient != null && !connectedClient.isClosed())
                {
                    PrintWriter sendToClient =
                            new PrintWriter(connectedClient.getOutputStream(), true);
                    sendToClient.println(ServerConstants.MSG_SERVER_CLOSE);
                    sendToClient.close();
                    connectedClient.close();
                }

                console.makeOutputToSpecifiedContext(
                        ServerConstants.MSG_SERVER_STOPPED,
                        consoleContext
                );

                ActiveServerContainer.getInstance().removeServer(consoleContext);
                server.close();
            }
        } catch (IOException e)
        {
            // intentionally ignored
        }
    }

    /**
     * Handles communication with a connected client. Reads incoming messages,
     * interprets identifiers, and applies transformations to the 3D scene.
     *
     * @param client the connected client socket
     * @precondition client != null AND client is connected
     * @postcondition Incoming commands are processed until the client disconnects
     */
    private void serveClient (Socket client)
    {

        try
        {
            BufferedReader inputFromClient =
                    new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter sendToClient =
                    new PrintWriter(client.getOutputStream(), true);

            String line;
            while ((line = inputFromClient.readLine()) != null)
            {

                if (!line.isEmpty())
                {

                    String identifier = line.substring(0, 2);
                    line = line.substring(2);

                    switch (identifier)
                    {

                        case ServerConstants.ID_ROTATE:
                            Gson gsonRotate = new Gson();
                            RotateObjectJSON rotateData =
                                    gsonRotate.fromJson(line, RotateObjectJSON.class);
                            SceneModel.getInstance().rotateObject(rotateData);
                            break;

                        case ServerConstants.ID_TRANSLATE:
                            Gson gsonTranslate = new Gson();
                            TranslateObjectJSON translateData =
                                    gsonTranslate.fromJson(line, TranslateObjectJSON.class);
                            SceneModel.getInstance().translateObject(translateData);
                            break;

                        case ServerConstants.ID_SEND:
                            console.makeOutputToSpecifiedContext(
                                    ServerConstants.MSG_CLIENT_SENT + line + "'",
                                    consoleContext
                            );
                            break;

                        default:
                            console.makeOutputToSpecifiedContext(
                                    ServerConstants.MSG_INVALID_PACKET,
                                    consoleContext
                            );
                    }
                }
            }

            console.makeOutputToSpecifiedContext(
                    ServerConstants.MSG_CLIENT_DISCONNECTED,
                    consoleContext
            );

            inputFromClient.close();
            sendToClient.close();
            client.close();

        } catch (IOException e)
        {

            if (!connectedClient.isClosed())
            {
                console.makeOutputToSpecifiedContext(
                        ServerConstants.MSG_CLIENT_SERVE_ERROR + portNumber,
                        consoleContext
                );
            }
        }
    }
}