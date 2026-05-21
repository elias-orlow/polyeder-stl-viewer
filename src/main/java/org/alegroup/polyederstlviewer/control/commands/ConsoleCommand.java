package org.alegroup.polyederstlviewer.control.commands;

import org.alegroup.polyederstlviewer.model.geometry.ConsoleObject;

public interface ConsoleCommand {

    public <D> void execute(ConsoleObject console, D data);
}
