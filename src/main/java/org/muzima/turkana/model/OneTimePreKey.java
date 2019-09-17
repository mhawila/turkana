package org.muzima.turkana.model;

import javax.persistence.*;

@Entity
@Table(name = "onetime_prekey")
public class OneTimePreKey {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "key_id")
    private int key_id;

    @Column(name = "public_key")
    private String public_key;

    @Column(name = "private_key")
    private String private_key;

    public OneTimePreKey() {
    }

    public OneTimePreKey(int id, int key_id, String public_key, String private_key) {
        this.id = id;
        this.key_id = key_id;
        this.public_key = public_key;
        this.private_key = private_key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    @Override
    public String toString() {
        return "OneTimePreKey{" +
            "id='" + id + '\'' +
            ", key_id='" + key_id + '\'' +
            ", public_key='" + public_key + '\'' +
            ", private_key='" + private_key + '\'' +
            '}';
    }
}
