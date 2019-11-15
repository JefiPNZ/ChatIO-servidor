package br.udesc.ceavi.dsd.chatio;

import br.udesc.ceavi.dsd.chatio.commands.ServerCommandAddContact;
import br.udesc.ceavi.dsd.chatio.commands.ServerCommandAlterUser;
import br.udesc.ceavi.dsd.chatio.commands.ServerCommandCreateUser;
import br.udesc.ceavi.dsd.chatio.commands.ServerCommandGetContactList;
import br.udesc.ceavi.dsd.chatio.commands.ServerCommandGetUserData;
import br.udesc.ceavi.dsd.chatio.commands.ServerCommandLogin;
import br.udesc.ceavi.dsd.chatio.commands.ServerCommandRemoveContact;

/**
 * Lista de mensagens que o cliente/servidor podem trocar.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public enum MessageList {
    
    MESSAGE_LOGIN("LOGIN>",                   ServerCommandLogin.class),
    MESSAGE_CONNECTED_STATUS("CONNECTED>",    null),
    MESSAGE_CREATE_USER("CREATE>",            ServerCommandCreateUser.class),
    MESSAGE_ALTER_USER_DATA("ALTER>",         ServerCommandAlterUser.class),
    MESSAGE_SUCCESS("SUCCESS>",               null),
    MESSAGE_ERROR("ERROR>",                   null),
    MESSAGE_ADD_CONTACT("ADDCONTACT>",        ServerCommandAddContact.class),
    MESSAGE_REMOVE_CONTACT("REMOVECONTACT>",  ServerCommandRemoveContact.class),
    MESSAGE_GET_CONTACT_LIST("GETCONTACT>",   ServerCommandGetContactList.class),
    MESSAGE_GET_USER_DATA("GETUSERDATA>",     ServerCommandGetUserData.class),
    MESSAGE_DATA("DATA>",                     null);
    
    private final String message;
    private final Class  command;
    private MessageList(String message, Class command){
        this.message = message;
        this.command = command;
    }

    /**
     * Retorna o texto para envio da mensagem.
     * @return 
     */
    @Override
    public String toString(){
        return this.message;
    }
 
    /**
     * Retorna a classe para instanciar o comando.
     * @return 
     */
    public Class getCommandClass(){
        return this.command;
    }

    public static MessageList fromString(String text) {
        for (MessageList value : MessageList.values()) {
            if (value.message.equalsIgnoreCase(text)) {
                return value;
            }
        }
        return null;
    }
    
}
