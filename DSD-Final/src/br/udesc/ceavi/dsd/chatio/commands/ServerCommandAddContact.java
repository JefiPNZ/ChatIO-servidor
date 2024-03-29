package br.udesc.ceavi.dsd.chatio.commands;

import br.udesc.ceavi.dsd.chatio.MessageList;
import br.udesc.ceavi.dsd.chatio.Server;
import br.udesc.ceavi.dsd.chatio.data.ChatUserDao;
import br.udesc.ceavi.dsd.chatio.data.Contact;
import br.udesc.ceavi.dsd.chatio.data.ContactDao;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

/**
 * Comando para adicionar um contato ao servidor.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public class ServerCommandAddContact implements ServerCommand {
    
    private String commandUser;
    private String result;
    private String executor;
    
    @Override
    public void execute() {
        Server.getInstance().notifyMessageForUser("Usuário " + executor + " está adicionando o contato " + this.commandUser + ".");
        Contact contact = new Contact();
        
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("DSD-FinalPU");
        ChatUserDao userDao = new ChatUserDao(factory);
        try {
            contact.setUser(userDao.findChatUserByLogin(this.executor));
            contact.setContact(userDao.findChatUserByLogin(this.commandUser));
        }
        catch (NoResultException ex){}
        if(contact.getUser() == null || contact.getContact() == null){
            this.result = MessageList.MESSAGE_ERROR.toString() + "{\"message\":\"Usuário não encontrado.\"}";
        }
        else {
            ContactDao dao = new ContactDao(factory);
            try {
                Contact duplicate = dao.findContactEntity(contact.getUser(), contact.getContact());
                if(duplicate != null){
                    this.result = MessageList.MESSAGE_ERROR.toString() + "{\"message\":\"Contato já adicionado.\"}";
                    return;
                }
            }
            catch(NoResultException ex){} // Caso não encontre, continua normalmente pois não está duplicado.
            dao.create(contact);
            Contact contact2 = new Contact();
            contact2.setUser(contact.getContact());
            contact2.setContact(contact.getUser());
            dao.create(contact2);
            this.result = MessageList.MESSAGE_SUCCESS.toString();
        }
        factory.close();
    }
    
    /**
     * Define o contato para inserção.
     * @param user 
     */
    public void setUser(String user){
        this.commandUser = user;
    }
    
    /**
     * Retorna o resultado do comando.
     * @return 
     */
    @Override
    public String getResult(){
        return this.result;
    }
    
    @Override
    public void setExecutor(String executor){
        this.executor = executor;
    }

    @Override
    public void setParams(String params) {
        JsonObject jsonObject = JsonParser.parseString(params).getAsJsonObject();
        JsonElement nicknameObj = jsonObject.get("nickname");
        String nicknameVal = nicknameObj != null ? nicknameObj.getAsString() : "";
        this.setUser(nicknameVal);
    }
    
}
