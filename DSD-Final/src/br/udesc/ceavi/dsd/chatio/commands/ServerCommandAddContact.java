package br.udesc.ceavi.dsd.chatio.commands;

import br.udesc.ceavi.dsd.chatio.MessageList;
import br.udesc.ceavi.dsd.chatio.data.Contact;
import br.udesc.ceavi.dsd.chatio.data.ContactDao;
import br.udesc.ceavi.dsd.chatio.data.exceptions.NonexistentEntityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Comando para adicionar um contato ao servidor.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public class ServerCommandAddContact implements ServerCommand {
    
    private Contact commandContact;
    private String result;
    
    @Override
    public void execute() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("DSD-FinalPU-Test");
        ContactDao dao = new ContactDao(factory);
        dao.create(this.commandContact);
        this.result = MessageList.MESSAGE_SUCCESS.toString();
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
    @Override
    public String getResult(){
        return this.result;
    }

    @Override
    public void setParams(String params) {

    }
    
}
