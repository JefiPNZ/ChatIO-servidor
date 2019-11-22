package br.udesc.ceavi.dsd.chatio.commands;

import br.udesc.ceavi.dsd.chatio.ClientNode;
import br.udesc.ceavi.dsd.chatio.MessageList;
import br.udesc.ceavi.dsd.chatio.Server;
import br.udesc.ceavi.dsd.chatio.data.ChatUser;
import br.udesc.ceavi.dsd.chatio.data.ChatUserDao;
import br.udesc.ceavi.dsd.chatio.data.Contact;
import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

/**
 * Comando para alterar os dados do Usuário.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public class ServerCommandAlterUser implements ServerCommand {
    
    private ChatUser commandUser;
    private String result;
    private String executor;
    
    @Override
    public void execute() {
        Server.getInstance().notifyMessageForUser("Usuário " + executor + " está alterando os dados do Usuário.");
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("DSD-FinalPU");
        ChatUserDao dao = new ChatUserDao(factory);
        
        ChatUser user;
        try {
            user = dao.findChatUserByLogin(this.executor);
        } catch(NoResultException ex){
            user = null;
        }
        if(user == null){
            this.result = MessageList.MESSAGE_ERROR.toString() + "{\"message\":\"Usu\u00e1rio com o nome não existe.\"}";
        }
        else {
            if(this.commandUser.getBirthDate() != null){
                user.setBirthDate(this.commandUser.getBirthDate());
            }
            if(this.commandUser.getEmail() != null){
                user.setEmail(this.commandUser.getEmail());
            }
            if(this.commandUser.getPassword() != null){
                user.setPassword(Server.getInstance().passwordHash(this.commandUser.getPassword()));
            }
            if(this.commandUser.getNickname()!= null){
                user.setNickname(this.commandUser.getNickname());
            }
            try {
                dao.edit(user);
                ClientNode client = Server.getInstance().findClientConnectionByLogin(this.executor);
                if(client == null){
                    this.result = MessageList.MESSAGE_ERROR.toString() + "{\"message\":\"Cliente não está logado...\"}";
                }
                else {
                    client.setLogin(user.getNickname());
                    this.result = MessageList.MESSAGE_SUCCESS.toString();
                }
            } catch (Exception ex) {
                this.result = MessageList.MESSAGE_ERROR.toString() + "{\"message\":\"" + ex.getMessage() + "\"}";
                Logger.getLogger(ServerCommandAlterUser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        factory.close();
    }
    
    /**
     * Define o contato para inserção.
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
        this.setUser(new Gson().fromJson(params, ChatUser.class));
    }
    
}
