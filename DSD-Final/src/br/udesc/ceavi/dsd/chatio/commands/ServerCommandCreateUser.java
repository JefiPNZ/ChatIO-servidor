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
public class ServerCommandCreateUser implements ServerCommand {
    
    private ChatUser commandUser;
    private String result;
    private String executor;

    @Override
    public void execute() {
        Server.getInstance().notifyMessageForUser("Usuário " + executor + " está criando um novo usuário.");
        if(this.commandUser.getNickname().isEmpty() ||
           this.commandUser.getPassword().isEmpty() ||
           this.commandUser.getEmail().isEmpty() ||
           this.commandUser.getBirthDate().isEmpty()){
            this.result = MessageList.MESSAGE_ERROR.toString() + "{\"message\":\"Dados de cadastro faltando, por favor preencha todos os campos.\"}";
            return;
        }
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("DSD-FinalPU");
        ChatUserDao dao = new ChatUserDao(factory);
        ChatUser user;
        try {
            user = dao.findChatUserByLogin(this.commandUser.getNickname());
        } catch(NoResultException ex){
            user = null;
        }
        if(user != null){
            this.result = MessageList.MESSAGE_ERROR.toString() + "{\"message\":\"Usu\u00e1rio com o nome j\u00e1 existe.\"}";
        }
        else {
            this.commandUser.setPassword(Server.getInstance().passwordHash(this.commandUser.getPassword()));
            dao.create(this.commandUser);
            this.result = MessageList.MESSAGE_SUCCESS.toString();
        }
        factory.close();
    }
    
    /**
     * Define o Usuário para executar o comando.
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
