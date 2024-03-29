package br.udesc.ceavi.dsd.chatio.commands;

import br.udesc.ceavi.dsd.chatio.MessageList;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Fabrica para criação dos comandos para execução no servidor. 
 * @author Jeferson Penz
 */
public class ServerCommandFactory {
    
    /**
     * Realiza a criação de um comando a partir da string passada.
     * @param command
     * @return 
     */
    public ServerCommand createCommand(String command){
        Pattern pattern = Pattern.compile("([A-Z]+>?)(.*)");
        Matcher matcher = pattern.matcher(command);
        String param    = null;
        if (matcher.find()) {
            command = matcher.group(1);
            param   = matcher.group(2);
        }
        MessageList message = MessageList.fromString(command);
        Class classData = message.getCommandClass();
        if(classData == null){
            return null;
        }
        try {
            @SuppressWarnings("unchecked")
            Constructor constructor       = classData.getConstructor();
            ServerCommand commandInstance = (ServerCommand) constructor.newInstance(new Object[] {});
            commandInstance.setParams(param);
            return commandInstance;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MessageList.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
