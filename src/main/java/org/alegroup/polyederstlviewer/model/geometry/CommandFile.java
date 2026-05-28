package org.alegroup.polyederstlviewer.model.geometry;

import org.alegroup.polyederstlviewer.control.CommandWriter;

import java.util.ArrayList;

public class CommandFile {

    public ArrayList<CommandBlueprint> commands = new ArrayList<CommandBlueprint>();

    public CommandFile(){
        this.commands = new ArrayList<CommandBlueprint>();
    }
}
