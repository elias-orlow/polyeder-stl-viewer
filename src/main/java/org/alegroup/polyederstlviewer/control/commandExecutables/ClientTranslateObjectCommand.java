package org.alegroup.polyederstlviewer.control.commandExecutables;

import com.google.gson.Gson;
import org.alegroup.polyederstlviewer.model.client.ActiveClientContainer;
import org.alegroup.polyederstlviewer.model.client.STLClient;
import org.alegroup.polyederstlviewer.model.client.TranslateObjectJSON;
import org.alegroup.polyederstlviewer.model.console.ConsoleObject;

public class ClientTranslateObjectCommand implements CommandExecuter{
    @Override
    public boolean execute(ConsoleObject console, String[] args) {

        if(args.length != 3){
            console.makeOutputToCurrentContext("Invalid arguments provided. translate --X --Y --Z");
            return false;
        }else{

            try {
                TranslateObjectJSON translateObject = new TranslateObjectJSON(Float.parseFloat(args[0]), Float.parseFloat(args[1]), Float.parseFloat(args[2]));
                System.out.println(args[0]);
                System.out.println(args[1]);
                System.out.println(args[2]);
                String context = console.getCurrentContext();
                STLClient client = ActiveClientContainer.getInstance().getClient(context);

                Gson gson = new Gson();
                String data = "tr" + gson.toJson(translateObject);

                client.inputCommand(data);

                console.makeOutputToCurrentContext("Sent translate data to server!");
                return true;

            }catch (Exception e){
                console.makeOutputToCurrentContext("Arguments provided must be of type float. translate --X --Y --Z");
                System.out.println(e.toString());
                System.out.println(e.getStackTrace().toString());
                return false;
            }
        }
    }
}
