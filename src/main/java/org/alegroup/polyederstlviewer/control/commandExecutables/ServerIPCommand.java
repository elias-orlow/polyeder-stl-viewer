package org.alegroup.polyederstlviewer.control.commandExecutables;

import org.alegroup.polyederstlviewer.constants.CommandConstants;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Prints all available IP addresses grouped by network interface.
 *
 * @precondition console != null
 * @postcondition IP addresses are printed to the current console context
 */
public class ServerIPCommand implements CommandExecuter
{

    @Override
    public boolean execute (ConsoleObject console, String[] args)
    {

        try
        {
            Enumeration<NetworkInterface> networkInterfaces =
                    NetworkInterface.getNetworkInterfaces();

            for (NetworkInterface networkInterface :
                    Collections.list(networkInterfaces))
            {

                Enumeration<InetAddress> inetAddresses =
                        networkInterface.getInetAddresses();

                if (inetAddresses.hasMoreElements())
                {

                    String displayName = networkInterface.getDisplayName();
                    console.makeOutputToCurrentContext(
                            CommandConstants.IP_HEADER_PREFIX + displayName + ": "
                    );

                    for (InetAddress inetAddress :
                            Collections.list(inetAddresses))
                    {

                        console.makeOutputToCurrentContext(
                                CommandConstants.IP_ENTRY_PREFIX + inetAddress.getHostAddress()
                        );
                    }

                    console.makeOutputToCurrentContext(CommandConstants.EMPTY_LINE);
                }
            }

            return true;

        } catch (SocketException e)
        {
            console.makeOutputToCurrentContext(CommandConstants.NETWORK_ERROR);
            return false;
        }
    }
}