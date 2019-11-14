package br.udesc.ceavi.dsd.chatio.commands;

import br.udesc.ceavi.dsd.chatio.MessageList;
import br.udesc.ceavi.dsd.chatio.Server;
import br.udesc.ceavi.dsd.chatio.data.Contact;
import br.udesc.ceavi.dsd.chatio.data.ContactDao;
import br.udesc.ceavi.dsd.chatio.data.exceptions.NonexistentEntityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Comando para remover um contato do servidor.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public class ServerCommandRemoveContact implements ServerCommand {
    
    private Contact commandContact;
    private String result;
    private String executor;
    
    @Override
    public void execute() {
        Server.getInstance().notifyMessageForUser("Usuário " + executor + " está removendo o contato de id " + commandContact.getId() + ".");
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("DSD-FinalPU");
        ContactDao dao = new ContactDao(factory);
        try {
            dao.destroy(this.commandContact.getId());
            this.result = MessageList.MESSAGE_SUCCESS.toString();
        } catch (NonexistentEntityException ex) {
            this.result = MessageList.MESSAGE_ERROR.toString() + "{\"mensagem\":\"" + ex.getMessage() + "\"}";
            Logger.getLogger(ServerCommandRemoveContact.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            factory.close();
        }
    }
    
    /**
     * Define o contato para exclusão.
     * @param contact 
     */
    public void setContact(Contact contact){
        this.commandContact = contact;
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
    public void setParams(String params) {
    
    }
    
}
