package br.udesc.ceavi.dsd.chatio.commands;

import br.udesc.ceavi.dsd.chatio.MessageList;
import br.udesc.ceavi.dsd.chatio.Server;
import br.udesc.ceavi.dsd.chatio.data.ChatUser;
import br.udesc.ceavi.dsd.chatio.data.ChatUserDao;
import com.google.gson.Gson;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

/**
 * Comando para criar um Usuário.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public class ServerCommandGetUserData implements ServerCommand {
    
    private String result;
    private String executor;

    @Override
    public void execute() {
        Server.getInstance().notifyMessageForUser("Usuário " + executor + " está buscando seus dados.");
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("DSD-FinalPU");
        ChatUserDao dao = new ChatUserDao(factory);
        try {
            ChatUser user = dao.findChatUserByLogin(this.executor);
            this.result = MessageList.MESSAGE_DATA.toString() + new Gson().toJson(user, ChatUser.class);
        } catch(NoResultException ex){
            this.result = MessageList.MESSAGE_ERROR.toString() + "{\"mensagem\":\"Usu\u00e1rio com o nome j\u00e1 existe.\"}";
        }
        finally {
            factory.close();
        }
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
    public void setParams(String params) {}
}
