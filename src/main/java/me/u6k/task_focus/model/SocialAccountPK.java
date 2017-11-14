
package me.u6k.task_focus.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Embeddable
@SuppressWarnings("serial")
public class SocialAccountPK implements Serializable {

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Column(name = "provider_user_id", nullable = false)
    private String providerUserId;

    public SocialAccountPK() {
    }

    public SocialAccountPK(String providerId, String providerUserId) {
        this.providerId = providerId;
        this.providerUserId = providerUserId;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

}
