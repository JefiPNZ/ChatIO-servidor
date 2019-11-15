package br.udesc.ceavi.dsd.chatio.data;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Entidade para representar um contato do sistema.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
@Entity
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "id", foreignKey = @ForeignKey(name = "none"))
    private ChatUser user;
    @ManyToOne
    @JoinColumn(name = "contact", referencedColumnName = "id", foreignKey = @ForeignKey(name = "none"))
    private ChatUser contact;
    
    private boolean online;
    private String  ip;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ChatUser getUser() {
        return user;
    }

    public void setUser(ChatUser user) {
        this.user = user;
    }

    public ChatUser getContact() {
        return contact;
    }

    public void setContact(ChatUser contact) {
        this.contact = contact;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Contact)) {
            return false;
        }
        Contact other = (Contact) object;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "Contact{" + "id=" + id + ", user=" + user + ", contact=" + contact + '}';
    }
    
}
