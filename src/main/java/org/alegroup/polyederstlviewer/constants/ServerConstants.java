package org.alegroup.polyederstlviewer.constants;

/**
 * Contains literal constants used by the STLServer for messages,
 * identifiers, and protocol strings.
 */
public interface ServerConstants
{

    // Identifiers
    String ID_ROTATE = "ro";
    String ID_TRANSLATE = "tr";
    String ID_SEND = "se";

    // Server messages
    String MSG_OPENING_SERVER = "Trying to open server on port: ";
    String MSG_WAITING_FOR_CLIENT = "Waiting for client to connect... (Port: ";
    String MSG_CLIENT_CONNECTED = "Client connected on port: ";
    String MSG_SERVER_SOCKET_ERROR = "Error while opening server socket on port: ";
    String MSG_SERVER_CLOSE = "SERVER_CLOSE";
    String MSG_SERVER_STOPPED = "Server stopped!";
    String MSG_CLIENT_SENT = "Client sent '";
    String MSG_INVALID_PACKET = "A Packet with an invalid identifier reached the server and was ignored!";
    String MSG_CLIENT_DISCONNECTED = "Client disconnected! Closing streams...";
    String MSG_CLIENT_SERVE_ERROR = "Something went wrong serving the client on port: ";
}