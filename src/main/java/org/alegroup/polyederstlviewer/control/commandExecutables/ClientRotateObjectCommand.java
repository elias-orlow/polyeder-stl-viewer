package org.alegroup.polyederstlviewer.control.commandExecutables;

import com.google.gson.Gson;
import org.alegroup.polyederstlviewer.model.client.ActiveClientContainer;
import org.alegroup.polyederstlviewer.model.client.RotateObjectJSON;
import org.alegroup.polyederstlviewer.model.client.STLClient;
import org.alegroup.polyederstlviewer.model.client.TranslateObjectJSON;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

public class ClientRotateObjectCommand implements CommandExecuter{
    @Override
    public boolean execute(ConsoleObject console, String[] args) {

        if(args.length != 3){
            console.makeOutputToCurrentContext("Invalid arguments provided. rotate --X --Y --Z");
            return false;
        }else{

            try {
                RotateObjectJSON rotateObject = new RotateObjectJSON(Float.parseFloat(args[0]), Float.parseFloat(args[1]), Float.parseFloat(args[2]));
                String context = console.getCurrentContext();
                STLClient client = ActiveClientContainer.getInstance().getClient(context);

                Gson gson = new Gson();
                String data = "ro" + gson.toJson(rotateObject);

                client.inputCommand(data);

                console.makeOutputToCurrentContext("Sent rotate data to server!");
                return true;

            }catch (Exception e){
                console.makeOutputToCurrentContext("Arguments provided must be of type float. rotate --X --Y --Z");
                System.out.println(e.toString());
                System.out.println(e.getStackTrace().toString());
                return false;
            }
        }
    }
}
