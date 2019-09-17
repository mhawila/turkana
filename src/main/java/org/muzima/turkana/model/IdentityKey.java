package org.muzima.turkana.model;

import org.whispersystems.libsignal.ecc.ECPublicKey;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="identity_key")
public class IdentityKey extends org.whispersystems.libsignal.IdentityKey {

    @Id
    @GeneratedValue
    private int id;

    public IdentityKey(ECPublicKey publicKey) {
        super(publicKey);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
