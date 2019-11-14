package br.udesc.ceavi.dsd.chatio.data;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Teste para persistência de Usuários.
 * @author Jeferson Penz
 */
public class ContactDaoTest {
    
    private ChatUserDao userDao;
    private ContactDao dao;
    private EntityManagerFactory factory;
    
    public ContactDaoTest() {}
    
    @Before
    public void setUp() {
        this.factory = Persistence.createEntityManagerFactory("DSD-FinalPU-Test");
        this.userDao = new ChatUserDao(factory);
        this.dao = new ContactDao(factory);
    }
    
    @After
    public void tearDown() {
        this.factory.close();
        this.factory = null;
        this.dao     = null;
        File dbFile  = new File("server-test.db");
        dbFile.delete();
    }

    /**
     * Test of create method, of class ChatUserDao.
     */
    @Test
    public void testCreate() {
        int qtdUsersBegin = this.dao.getContactCount();
        ChatUser user1 = this.insertTestUser(1);
        ChatUser user2 = this.insertTestUser(2);
        this.insertTestContact(user1, user2);
        int qtdUsersEnd = this.dao.getContactCount();
        Assert.assertThat("Contato não adicionado.", qtdUsersEnd, Matchers.greaterThan(qtdUsersBegin));
    }

    /**
     * Test of edit method, of class ChatUserDao.
     * @throws java.lang.Exception
     */
    @Test
    public void testEdit() throws Exception {
        String expected = "Teste 3";
        ChatUser user1 = this.insertTestUser(1);
        ChatUser user2 = this.insertTestUser(2);
        long contactId = this.insertTestContact(user1, user2);
        Contact contact = this.findTestContact(contactId);
        ChatUser user3 = this.insertTestUser(3);
        contact.setContact(user3);
        this.dao.edit(contact);
        contact = this.findTestContact(contactId);
        Assert.assertEquals(contact.getContact().getNickname(), expected);
    }

    /**
     * Test of destroy method, of class ChatUserDao.
     * @throws java.lang.Exception
     */
    @Test
    public void testDestroy() throws Exception {
        ChatUser user1 = this.insertTestUser(1);
        ChatUser user2 = this.insertTestUser(2);
        long contactId = this.insertTestContact(user1, user2);
        int qtdUsersBegin = this.dao.getContactCount();
        this.dao.destroy(contactId);
        int qtdUsersEnd = this.dao.getContactCount();
        Assert.assertThat("Usuário não removido.", qtdUsersEnd, Matchers.lessThan(qtdUsersBegin));
    }
    
    /**
     * Test of findContactEntities using User, of class ChatUserDao.
     */
    @Test
    public void testeFindContactEntitierByUser(){
        ChatUser user1 = this.insertTestUser(1);
        ChatUser user2 = this.insertTestUser(2);
        this.insertTestContact(user1, user2);
        
        ChatUser user3 = this.insertTestUser(3);
        this.insertTestContact(user1, user3);
        ChatUser user4 = this.insertTestUser(4);
        this.insertTestContact(user3, user4);
        
        List<Contact> contacts = this.dao.findContactEntities(user1);
    
        // São inseridos 3 contatos, porém deve retornar apenas 2 na query.
        Assert.assertEquals("Usuários não adicionados.", 2, contacts.size());
    }
    
    private ChatUser insertTestUser(int count){
        ChatUser user = new ChatUser();
        user.setBirthDate("1999");
        user.setEmail("teste@teste.com");
        user.setNickname("Teste " + count);
        user.setPassword("123456789");
        this.userDao.create(user);
        return user;
    }
    
    private Contact findTestContact(long id){
        return this.dao.findContact(id);
    }
    
    private long insertTestContact(ChatUser user1, ChatUser user2){
        Contact contact = new Contact();
        contact.setUser(user1);
        contact.setContact(user2);
        this.dao.create(contact);
        return contact.getId();
    }
    
}
