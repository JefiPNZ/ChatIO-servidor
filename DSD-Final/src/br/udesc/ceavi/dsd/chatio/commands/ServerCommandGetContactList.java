package br.udesc.ceavi.dsd.chatio.commands;

import br.udesc.ceavi.dsd.chatio.MessageList;
import br.udesc.ceavi.dsd.chatio.Server;
import br.udesc.ceavi.dsd.chatio.data.ChatUser;
import br.udesc.ceavi.dsd.chatio.data.ContactDao;
import com.google.gson.Gson;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Comando para buscar a lista de contatos do servidor.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public class ServerCommandGetContactList implements ServerCommand {
    
    private ChatUser commandUser;
    private String result;
    private String executor;

    @Override
    public void execute() {
        Server.getInstance().notifyMessageForUser("Usuário " + executor + " está solicitando a lista de contatos.");
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("DSD-FinalPU");
        ContactDao dao = new ContactDao(factory);
        this.result = MessageList.MESSAGE_DATA.toString() + new Gson().toJson(dao.findContactEntities(commandUser));
        factory.close();
    }
    
    /**
     * Define o Usuário para buscar a lista.
     * @param user 
     */
    public void setUser(ChatUser user){
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
        
    }
    
}
