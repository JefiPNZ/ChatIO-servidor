package br.udesc.ceavi.dsd.chatio.commands;

import br.udesc.ceavi.dsd.chatio.ClientNode;
import br.udesc.ceavi.dsd.chatio.MessageList;
import br.udesc.ceavi.dsd.chatio.Server;
import br.udesc.ceavi.dsd.chatio.data.ChatUser;
import br.udesc.ceavi.dsd.chatio.data.ChatUserDao;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

/**
 * Comando para realizar o login no servidor.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public class ServerCommandLogout implements ServerCommand {
    
    private String result;
    private String executor;

    @Override
    public void execute() {
        Server server = Server.getInstance();
        ClientNode client = server.findClientConnectionByLogin(this.executor);
        if(client == null){
            this.result = MessageList.MESSAGE_ERROR.toString() + "{\"message\":\"Usuário não está logado...\"}";
        }
        else {
            client.setLogin(null);
            this.result = MessageList.MESSAGE_SUCCESS.toString();
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
