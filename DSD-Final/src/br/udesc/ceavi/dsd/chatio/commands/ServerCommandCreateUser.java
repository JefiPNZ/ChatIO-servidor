package br.udesc.ceavi.dsd.chatio.commands;

import br.udesc.ceavi.dsd.chatio.data.ChatUser;
import br.udesc.ceavi.dsd.chatio.data.ChatUserDao;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Comando para criar um Usuário.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public class ServerCommandCreateUser implements ServerCommand {
    
    private ChatUser commandUser;
    private boolean result = false;

    @Override
    public void execute() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("DSD-FinalPU-Test");
        ChatUserDao dao = new ChatUserDao(factory);
        dao.create(this.commandUser);
        this.result = true;
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
    public boolean getResult(){
        return this.result;
    }
    
}
