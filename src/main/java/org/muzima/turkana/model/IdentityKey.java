package org.muzima.turkana.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.ecc.ECPublicKey;
import org.whispersystems.libsignal.util.Hex;

import javax.persistence.*;
import java.io.IOException;

@Entity
@Table(name ="identity_key")
public class IdentityKey  {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "public_key")
    private String ecPublicKey;

    @Transient
    ObjectMapper objectMapper = new ObjectMapper();

    @Transient
    private ECPublicKey publicKey;

    public IdentityKey() {
    }

    public IdentityKey(ECPublicKey publicKey) {
        this.publicKey = publicKey;
        try {
            this.ecPublicKey = objectMapper.writeValueAsString(publicKey);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public IdentityKey(byte[] bytes, int offset) throws InvalidKeyException {
        this.publicKey = Curve.decodePoint(bytes, offset);
    }

    public ECPublicKey getPublicKey() {
        try {
            return objectMapper.readValue(ecPublicKey,ECPublicKey.class);
        } catch (IOException e) {
            throw new AssertionError();
        }
    }


    public byte[] serialize() {
        return this.publicKey.serialize();
    }

    public String getFingerprint() {
        return Hex.toString(this.publicKey.serialize());
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else {
            return !(other instanceof org.whispersystems.libsignal.IdentityKey) ? false : this.publicKey.equals(((org.whispersystems.libsignal.IdentityKey)other).getPublicKey());
        }
    }

    public int hashCode() {
        return this.publicKey.hashCode();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
