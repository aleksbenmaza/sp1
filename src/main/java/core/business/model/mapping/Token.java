package core.business.model.mapping;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Generated;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by alexandremasanes on 28/04/2017.
 */
@Entity
@Table(name = "tokens")
public class Token extends IdentifiableByIdImpl {

    @Column
    private String value;

    @Column(name = "old_value")
    private String oldValue;

    @Column(name = "api_server")
    private String apiServer;

    @Generated(GenerationTime.INSERT)
    @Column(name = "created_at")
    private Timestamp createdAt;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "compte_utilisateur_id", referencedColumnName = "id")
    private UserAccount userAccount;

    public Token() {

    }

    public Token(UserAccount userAccount) {
        setUserAccount(requireNonNull(userAccount));
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getApiServer() {
        return apiServer;
    }

    public void setApiServer(String apiServer) {
        this.apiServer = apiServer;
    }

    public Timestamp createdAt() {
        return createdAt;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
        if(userAccount != null && userAccount.getToken() != this)
            userAccount.setToken(this);
    }
}
