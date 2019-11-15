package br.udesc.ceavi.dsd.chatio.commands;

import br.udesc.ceavi.dsd.chatio.MessageList;
import br.udesc.ceavi.dsd.chatio.Server;
import br.udesc.ceavi.dsd.chatio.data.ChatUser;
import br.udesc.ceavi.dsd.chatio.data.ChatUserDao;
import br.udesc.ceavi.dsd.chatio.data.Contact;
import br.udesc.ceavi.dsd.chatio.data.ContactDao;
import br.udesc.ceavi.dsd.chatio.data.exceptions.NonexistentEntityException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Comando para remover um contato do servidor.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public class ServerCommandRemoveContact implements ServerCommand {
    
    private String commandUser;
    private String result;
    private String executor;
    
    @Override
    public void execute() {
        Server.getInstance().notifyMessageForUser("Usuário " + executor + " está removendo o contato " + commandUser + ".");
            
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("DSD-FinalPU");
        ContactDao dao = new ContactDao(factory);
        ChatUserDao userDao = new ChatUserDao(factory);
        ChatUser user = userDao.findChatUserByLogin(this.executor);
        ChatUser targetContact = userDao.findChatUserByLogin(this.commandUser);
        if(user == null || targetContact == null){
            this.result = MessageList.MESSAGE_ERROR.toString() + "{\"mensagem\":\"Usuário não encontrado.\"}";
        }
        
        try {
            Contact contact = dao.findContactEntity(user, targetContact);
            dao.destroy(contact.getId());
            this.result = MessageList.MESSAGE_SUCCESS.toString();
        } catch (NonexistentEntityException ex) {
            this.result = MessageList.MESSAGE_ERROR.toString() + "{\"mensagem\":\"" + ex.getMessage() + "\"}";
            Logger.getLogger(ServerCommandRemoveContact.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            factory.close();
        }
    }
    
    /**
     * Define o contato para inserção.
     * @param user 
     */
    public void setUser(String user){
        this.commandUser = user;
    }
    
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
