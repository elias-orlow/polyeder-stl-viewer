package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.channels.NetworkChannel;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;

public class ServerIPCommand implements CommandExecuter{
    @Override
    public boolean execute(ConsoleObject console, String[] args) {

        Enumeration<NetworkInterface> networkInterfaces = null;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();

            for(NetworkInterface networkInterface : Collections.list(networkInterfaces)){

                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

                if(inetAddresses.hasMoreElements()){

                    String displayName = networkInterface.getDisplayName();
                    console.makeOutputToCurrentContext("IP-Addresses for " + displayName + ": ");

                    for(InetAddress inetAddress : Collections.list(inetAddresses)){
                        console.makeOutputToCurrentContext("---> " + inetAddress.getHostAddress());
                    }

                    console.makeOutputToCurrentContext("");
                }
            }

            return true;

        } catch (SocketException e) {
            console.makeOutputToCurrentContext("Could not fetch network interfaces!");
            return false;
        }

    }
}
