package br.udesc.ceavi.dsd.chatio.commands;

import br.udesc.ceavi.dsd.chatio.MessageList;
import br.udesc.ceavi.dsd.chatio.data.ChatUserDao;
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

    @Override
    public void execute() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("DSD-FinalPU");
        ChatUserDao dao = new ChatUserDao(factory);
        try {
            dao.findChatUserByLogin(this.login, this.password);
            this.result = MessageList.MESSAGE_SUCCESS.toString();
        } catch (NoResultException ex){
            this.result = MessageList.MESSAGE_ERROR.toString() + "{\"mensagem\":\"Nenhum Usuário Encontrado\"}";
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
    public String getResult(){
        return this.result;
    }

    @Override
    public void setParams(String params) {

    }
    
}
