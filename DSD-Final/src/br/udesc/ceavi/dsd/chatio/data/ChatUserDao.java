package br.udesc.ceavi.dsd.chatio.data;

import br.udesc.ceavi.dsd.chatio.data.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Classe para persistencia de dados de Usuário via JPA.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public class ChatUserDao implements Serializable {

    public ChatUserDao(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ChatUser chatUser) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(chatUser);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ChatUser chatUser) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            chatUser = em.merge(chatUser);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                long id = chatUser.getId();
                if (findChatUser(id) == null) {
                    throw new NonexistentEntityException("The chatUser with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ChatUser chatUser;
            try {
                chatUser = em.getReference(ChatUser.class, id);
                chatUser.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The chatUser with id " + id + " no longer exists.", enfe);
            }
            em.remove(chatUser);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ChatUser> findChatUserEntities() {
        return findChatUserEntities(true, -1, -1);
    }

    public List<ChatUser> findChatUserEntities(int maxResults, int firstResult) {
        return findChatUserEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
    private List<ChatUser> findChatUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ChatUser.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ChatUser findChatUser(long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ChatUser.class, id);
        } finally {
            em.close();
        }
    }
    
    public ChatUser findChatUserByLogin(String login){
        return this.findChatUserByLogin(login, null);
    }
    
    @SuppressWarnings("unchecked")
    public ChatUser findChatUserByLogin(String login, String password){
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<ChatUser> c = cq.from(ChatUser.class);
            cq.select(c)
              .where(cb.equal(c.get("nickname"), login));
            if(password != null){
                cq.where(cb.equal(c.get("password"), password));
            }
            Query q = em.createQuery(cq);
            return (ChatUser) q.getSingleResult();
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public int getChatUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ChatUser> rt = cq.from(ChatUser.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
