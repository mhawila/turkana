package org.muzima.turkana.model;

import javax.persistence.*;

@Entity
@Table(name = "preferences")
public class Preference {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "pref_key")
    private String key;

    @Column(name = "pref")
    private String pref;

    @Column(name = "value")
    private String value;

    public Preference() {
    }

    public Preference(int id, String key, String pref, String value) {
        this.id = id;
        this.key = key;
        this.pref = pref;
        this.value = value;
    }

    public Preference(String key, int value) {
        this.key = key;
        this.value = String.valueOf(value);
    }

    public Preference(String key, String encodeBytes) {
        this.key = key;
        this.value = String.valueOf(encodeBytes);
    }

    public Preference(String key, boolean value) {
        this.key = key;
        this.value = String.valueOf(value);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPref() {
        return pref;
    }

    public void setPref(String pref) {
        this.pref = pref;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
