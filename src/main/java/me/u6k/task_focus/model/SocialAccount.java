
package me.u6k.task_focus.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "t_social_account")
public class SocialAccount {

    @EmbeddedId
    private SocialAccountPK key;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    private User user;

    public SocialAccount() {
    }

    public SocialAccount(SocialAccountPK key, String name, User user) {
        this.key = key;
        this.name = name;
        this.user = user;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "user");
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, "user");
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public SocialAccountPK getKey() {
        return key;
    }

    public void setKey(SocialAccountPK key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
