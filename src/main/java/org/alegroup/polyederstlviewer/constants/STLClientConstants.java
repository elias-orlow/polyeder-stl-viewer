package org.alegroup.polyederstlviewer.constants;

public interface STLClientConstants
{
    boolean AUTO_FLUSH_SERVER_WRITER = true;

    String SUCCESSFULLY_CONNECTED_MESSAGE =
            "Successfully connected socket to %s on port: %d";

    String SOCKET_OPENING_ERROR_MESSAGE =
            "Something went wrong opening a client socket to %s on port: %d";

    String CLIENT_STOPPED_MESSAGE =
            "Client stopped!";

    String INPUT_COMMAND_MESSAGE =
            "Please input your command!";

    String CLIENT_ERROR_MESSAGE =
            "Something went wrong with the client!";
}
