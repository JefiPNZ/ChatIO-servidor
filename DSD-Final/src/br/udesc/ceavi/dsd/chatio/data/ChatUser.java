package br.udesc.ceavi.dsd.chatio.data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 * Entidade para representar um Usuário do sistema.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
@Entity
public class ChatUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "birthdate")
    private Date birthDate;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "nickname", nullable = false)
    private String nickname;
    @Column(name = "password", nullable = false)
    private String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ChatUser)) {
            return false;
        }
        ChatUser other = (ChatUser) object;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "ChatUser{" + "id=" + id + ", birthDate=" + birthDate + ", email=" + email + ", nickname=" + nickname + ", password=" + password + '}';
    }
    
}
