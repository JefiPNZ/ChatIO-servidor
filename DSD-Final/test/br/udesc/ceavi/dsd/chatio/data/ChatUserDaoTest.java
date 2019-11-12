package br.udesc.ceavi.dsd.chatio.data;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
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
public class ChatUserDaoTest {
    
    private ChatUserDao dao;
    private EntityManagerFactory factory;
    
    public ChatUserDaoTest() {}
    
    @Before
    public void setUp() {
        this.factory = Persistence.createEntityManagerFactory("DSD-FinalPU-Test");
        this.dao = new ChatUserDao(factory);
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
        int qtdUsersBegin = this.dao.getChatUserCount();
        this.insertTestUser();
        int qtdUsersEnd = this.dao.getChatUserCount();
        Assert.assertThat("Usuário não adicionado.", qtdUsersEnd, Matchers.greaterThan(qtdUsersBegin));
    }

    /**
     * Test of edit method, of class ChatUserDao.
     * @throws java.lang.Exception
     */
    @Test
    public void testEdit() throws Exception {
        String expected = "Teste Edit";
        long userId = this.insertTestUser();
        ChatUser user = this.findTestUser(userId);
        user.setNickname(expected);
        this.dao.edit(user);
        user = this.dao.findChatUser(userId);
        Assert.assertEquals(user.getNickname(), expected);
    }

    /**
     * Test of destroy method, of class ChatUserDao.
     * @throws java.lang.Exception
     */
    @Test
    public void testDestroy() throws Exception {
        long userId = this.insertTestUser();
        int qtdUsersBegin = this.dao.getChatUserCount();
        this.dao.destroy(userId);
        int qtdUsersEnd = this.dao.getChatUserCount();
        Assert.assertThat("Usuário não removido.", qtdUsersEnd, Matchers.lessThan(qtdUsersBegin));
    }
    
    /**
     * Test of findChatUserByLogin method, of class ChatUserDao.
     */
    @Test
    public void testFindChatUserByLogin(){
        String expected = "Teste";
        this.insertTestUser();
        ChatUser user = this.dao.findChatUserByLogin("Teste", "123456789");
        Assert.assertEquals(expected, user.getNickname());
        try {
            this.dao.findChatUserByLogin("Teste", "12345678");
            Assert.assertTrue("Encontrou um Usuário inexistente.", false);
        } catch(NoResultException ex){
            Assert.assertTrue(true);
        }
    }
    
    private long insertTestUser(){
        ChatUser user = new ChatUser();
        user.setBirthDate(new GregorianCalendar(1999, Calendar.JANUARY, 1).getTime());
        user.setEmail("teste@teste.com");
        user.setNickname("Teste");
        user.setPassword("123456789");
        this.dao.create(user);
        return user.getId();
    }
    
    private ChatUser findTestUser(long id){
        return this.dao.findChatUser(id);
    }
    
}
