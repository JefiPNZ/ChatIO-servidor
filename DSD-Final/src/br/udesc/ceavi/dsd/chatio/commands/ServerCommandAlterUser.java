package br.udesc.ceavi.dsd.chatio.commands;

import br.udesc.ceavi.dsd.chatio.data.Contact;
import br.udesc.ceavi.dsd.chatio.data.ContactDao;
import br.udesc.ceavi.dsd.chatio.data.exceptions.NonexistentEntityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Comando para alterar os dados do Usuário.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public class ServerCommandAlterUser implements ServerCommand {
    
    private Contact commandContact;
    private boolean result = false;
    
    @Override
    public void execute() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("DSD-FinalPU-Test");
        ContactDao dao = new ContactDao(factory);
        try {
            dao.edit(this.commandContact);
            this.result = true;
        } catch (Exception ex) {
            Logger.getLogger(ServerCommandAlterUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Define o contato para inserção.
     * @param contact 
     */
    public void setContact(Contact contact){
        this.commandContact = contact;
    }
    
    /**
     * Retorna o resultado do comando.
     * @return 
     */
    public boolean getResult(){
        return this.result;
    }
    
}
