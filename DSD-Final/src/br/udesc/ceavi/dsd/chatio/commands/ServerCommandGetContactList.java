package br.udesc.ceavi.dsd.chatio.commands;

import br.udesc.ceavi.dsd.chatio.MessageList;
import br.udesc.ceavi.dsd.chatio.Server;
import br.udesc.ceavi.dsd.chatio.data.ChatUser;
import br.udesc.ceavi.dsd.chatio.data.ChatUserDao;
import br.udesc.ceavi.dsd.chatio.data.ContactDao;
import com.google.gson.Gson;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Comando para buscar a lista de contatos do servidor.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public class ServerCommandGetContactList implements ServerCommand {
    
    private String result;
    private String executor;

    @Override
    public void execute() {
        Server.getInstance().notifyMessageForUser("Usuário " + executor + " está solicitando a lista de contatos.");
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("DSD-FinalPU");
        ContactDao  dao     = new ContactDao(factory);
        ChatUserDao userDao = new ChatUserDao(factory);
        ChatUser    user    = userDao.findChatUserByLogin(this.executor);
        this.result = MessageList.MESSAGE_DATA.toString() + new Gson().toJson(dao.findContactEntities(user));
        factory.close();
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
    public void setParams(String params) {}
    
}
