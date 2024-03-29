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
public class ServerCommandLogin implements ServerCommand {
    
    private String result;
    private String login;
    private String password;
    private String executor;

    @Override
    public void execute() {
        Server server = Server.getInstance();
        server.notifyMessageForUser("Usuário " + executor + " está realizando login como " + this.login + ".");
        if(this.login.isEmpty() || this.password.isEmpty()){
            this.result = MessageList.MESSAGE_ERROR.toString() + "{\"message\":\"Usuário ou senha inválidos...\"}";
            return;
        }
        if(server.findClientConnectionByLogin(login) != null){
            this.result = MessageList.MESSAGE_ERROR.toString() + "{\"message\":\"Usuário já conectado...\"}";
            return;
        }
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("DSD-FinalPU");
        ChatUserDao dao = new ChatUserDao(factory);
        dao.findChatUserEntities();
        try {
            ChatUser user = dao.findChatUserByLogin(this.login, Server.getInstance().passwordHash(this.password));
            ClientNode client = server.findClientConnectionByIp(this.executor);
            if(client == null){
                this.result = MessageList.MESSAGE_ERROR.toString() + "{\"message\":\"Cliente já está logado...\"}";
            }
            else {
                client.setLogin(user.getNickname());
                this.result = MessageList.MESSAGE_SUCCESS.toString();
            }
        } catch (NoResultException ex){
            this.result = MessageList.MESSAGE_ERROR.toString() + "{\"message\":\"Nenhum Usuário Encontrado\"}";
        }
        finally {
            factory.close();
        }
    }
    
    /**
     * Define os dados de login.
     * @param login
     * @param password 
     */
    public void setLogin(String login, String password){
        this.login = login;
        this.password = password;
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
        JsonElement passwordObj = jsonObject.get("password");
        String passwordVal = passwordObj != null ? passwordObj.getAsString() : "";
        this.setLogin(nicknameVal, passwordVal);
    }
    
}
