package br.udesc.ceavi.dsd.chatio;

/**
 * Lista de mensagens que o cliente/servidor podem trocar.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public enum MessageList {
    
    MESSAGE_LOGIN("LOGIN>"),
    MESSAGE_CONNECTED_STATUS("CONNECTED"),
    MESSAGE_CREATE_USER("CREATE>"),
    MESSAGE_ALTER_USER_DATA("ALTER>"),
    MESSAGE_SUCCESS("SUCCESS"),
    MESSAGE_ERROR("ERROR>"),
    MESSAGE_ADD_CONTACT("ADDCONTACT"),
    MESSAGE_REMOVE_CONTACT("REMOVECONTACT"),
    MESSAGE_GET_CONTACT_LIST("GETCONTACT"),
    MESSAGE_DATA("DATA>");
    
    private String message;
    private MessageList(String message){
        this.message = message;
    }

    /**
     * Retorna o texto para envio da mensagem.
     * @return 
     */
    @Override
    public String toString(){
        return this.message;
    }
    
}
