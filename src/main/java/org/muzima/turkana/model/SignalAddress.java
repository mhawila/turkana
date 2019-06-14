package org.muzima.turkana.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="signal_address")
public class SignalAddress {

    @Id
    private String uuid;

    @Column( name ="address")
    private String address;

    public SignalAddress() {
    }

    public SignalAddress(String uuid, String address) {
        this.uuid = uuid;
        this.address = address;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SignalAddress{" +
                "uuid='" + uuid + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
