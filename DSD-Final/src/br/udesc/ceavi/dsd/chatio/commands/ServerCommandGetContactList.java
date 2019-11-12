package br.udesc.ceavi.dsd.chatio.commands;

import br.udesc.ceavi.dsd.chatio.data.ChatUser;
import br.udesc.ceavi.dsd.chatio.data.Contact;
import br.udesc.ceavi.dsd.chatio.data.ContactDao;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Comando para buscar a lista de contatos do servidor.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public class ServerCommandGetContactList implements ServerCommand {
    
    private ChatUser commandUser;
    private List<Contact> result;

    @Override
    public void execute() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("DSD-FinalPU-Test");
        ContactDao dao = new ContactDao(factory);
        this.result = dao.findContactEntities(commandUser);
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
    public List<Contact> getResult(){
        return this.result;
    }
    
}
