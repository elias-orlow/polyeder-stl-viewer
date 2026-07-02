package org.alegroup.polyederstlviewer.model.client;

import org.alegroup.polyederstlviewer.constants.STLClientConstants;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Represents a client that communicates with an STL server over a TCP socket.
 * Commands can be sent asynchronously, and server responses are written to
 * a context-bound console.
 *
 * @precondition hostname != null AND console != null AND consoleContext != null
 * @postcondition A fully initialized STLClient instance is created
 */
public class STLClient implements Runnable
{

    /**
     * Hostname of the server.
     */
    private final String hostname;

    /**
     * Port number of the server.
     */
    private final int portNumber;

    /**
     * Console used for output and context-bound messaging.
     */
    private final ConsoleObject console;

    /**
     * Console context associated with this client.
     */
    private final String consoleContext;

    /**
     * Queue of commands waiting to be sent to the server.
     */
    private final BlockingQueue<String> commandQueue = new LinkedBlockingQueue<>();

    /**
     * The underlying socket connection.
     */
    private volatile Socket client;

    /**
     * Creates a new STLClient instance.
     *
     * @param hostname       the server hostname
     * @param portNumber     the server port number
     * @param console        the console used for output
     * @param consoleContext the console context associated with this client
     * @precondition hostname != null AND console != null AND consoleContext != null
     * @postcondition A new STLClient instance is created
     */
    public STLClient (String hostname, int portNumber, ConsoleObject console, String consoleContext)
    {
        this.hostname = hostname;
        this.portNumber = portNumber;
        this.console = console;
        this.consoleContext = consoleContext;
    }

    /**
     * Starts the client thread, opens the socket connection,
     * and begins processing commands.
     *
     * @precondition none
     * @postcondition Client attempts to connect and begins command processing
     */
    @Override
    public void run ()
    {

        try
        {
            client = new Socket(hostname, portNumber);
            console.makeOutputToSpecifiedContext(
                    STLClientConstants.SUCCESSFULLY_CONNECTED_MESSAGE.formatted(
                            hostname,
                            portNumber
                    ),
                    consoleContext
            );

        } catch (IOException e)
        {
            console.makeOutputToSpecifiedContext(
                    STLClientConstants.SOCKET_OPENING_ERROR_MESSAGE.formatted(
                            hostname,
                            portNumber
                    ),
                    consoleContext
            );
        }

        sendCommandToServer();
    }

    /**
     * Stops the client and closes the socket connection.
     *
     * @precondition none
     * @postcondition Socket is closed and client is removed from ActiveClientContainer
     */
    public void stop ()
    {
        try
        {
            if (client != null && !client.isClosed())
            {
                console.makeOutputToSpecifiedContext(
                        STLClientConstants.CLIENT_STOPPED_MESSAGE,
                        consoleContext
                );
                ActiveClientContainer.getInstance().removeClient(consoleContext);
                client.close();
            }
        } catch (IOException ignored)
        {
            // Intentionally ignored
        }
    }

    /**
     * Sends commands from the queue to the server until an empty command is received.
     *
     * @precondition client != null AND client is connected
     * @postcondition Commands are sent until termination signal is received
     */
    private void sendCommandToServer ()
    {

        if (client == null)
        {
            return;
        }

        try
        {
            BufferedReader fromServer =
                    new BufferedReader(new InputStreamReader(client.getInputStream()));

            PrintWriter toServer =
                    new PrintWriter(
                            client.getOutputStream(),
                            STLClientConstants.AUTO_FLUSH_SERVER_WRITER
                    );

            console.makeOutputToSpecifiedContext(
                    STLClientConstants.INPUT_COMMAND_MESSAGE,
                    consoleContext
            );

            while (true)
            {

                String command = commandQueue.take();

                if (command.isEmpty())
                {
                    break;
                }

                toServer.println(command);
            }

            fromServer.close();
            toServer.close();
            client.close();

        } catch (IOException | InterruptedException e)
        {
            console.makeOutputToSpecifiedContext(
                    STLClientConstants.CLIENT_ERROR_MESSAGE,
                    consoleContext
            );
            stop();
        }
    }

    /**
     * Adds a command to the queue to be sent to the server.
     *
     * @param command the command to send
     * @precondition command != null
     * @postcondition Command is added to the queue
     */
    public void inputCommand (String command)
    {
        commandQueue.add(command);
    }
}