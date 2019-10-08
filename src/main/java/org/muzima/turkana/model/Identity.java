package org.muzima.turkana.model;

import org.muzima.turkana.utils.Base64;
import org.springframework.context.annotation.Primary;
import org.whispersystems.libsignal.IdentityKey;

import javax.persistence.*;

@Entity
@Table(name = "identity")
public class Identity {

    @Column(name = "_id")
    @Id
    @GeneratedValue
    private String id;

    @Column(name = "address")
    private String address;

    @Column(name = "identity_key")
    private String identity_key;

    @Column(name = "fingerpring")
    private String fingerprint;

    @Column(name = "timestamp")
    private String timestamp;

    @Column(name = "first_use_status")
    private boolean first_use;

    @Column(name = "non_blocking_approval")
    private boolean nonblocking_approval;

    @Column(name = "verified_status")
    private String verified;

    public Identity() {
    }

    public Identity(String id, String address, String identity_key, String timestamp, boolean first_use, boolean nonblocking_approval, String verified) {
        this.id = id;
        this.address = address;
        this.identity_key = identity_key;
        this.timestamp = timestamp;
        this.first_use = first_use;
        this.nonblocking_approval = nonblocking_approval;
        this.verified = verified;
    }

    public Identity(String signalAddress, IdentityKey identityKey,String fingerprint,long timestamp) {
        this.address = signalAddress;
        this.identity_key = Base64.encodeBytes(identityKey.getPublicKey().serialize());
        this.fingerprint = fingerprint;
        this.timestamp = String.valueOf(timestamp);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdentity_key() {
        return identity_key;
    }

    public void setIdentity_key(String identity_key) {
        this.identity_key = identity_key;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean getFirst_use() {
        return first_use;
    }

    public void setFirst_use(boolean first_use) {
        this.first_use = first_use;
    }

    public boolean getNonblocking_approval() {
        return nonblocking_approval;
    }

    public void setNonblocking_approval(boolean nonblocking_approval) {
        this.nonblocking_approval = nonblocking_approval;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        return "Identity{" +
            "id='" + id + '\'' +
            ", address='" + address + '\'' +
            ", identity_key='" + identity_key + '\'' +
            ", timestamp='" + timestamp + '\'' +
            ", first_use='" + first_use + '\'' +
            ", nonblocking_approval='" + nonblocking_approval + '\'' +
            ", verified='" + verified + '\'' +
            '}';
    }
}
