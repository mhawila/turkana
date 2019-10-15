package org.muzima.turkana.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Representation of a signal Session inclusive of the the
 * Session State data in column record @see {@link org.muzima.turkana.model.SessionRecord}
 *
 * @author Samuel Owino
 */

@Entity
public class SignalSession {

    @Id
    @GeneratedValue
    private long ID;

    @Column(name = "address")
    private String address;

    @Column(name = "deviceId")
    private String deviceId;

    @Column(name = "record")
    private String record;

    public SignalSession() {
    }

    public SignalSession(String address, String deviceId, String record) {
        this.address = address;
        this.deviceId = deviceId;
        this.record = record;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    @Override
    public String toString() {
        return "SignalSession{" +
            "ID=" + ID +
            ", address='" + address + '\'' +
            ", deviceId='" + deviceId + '\'' +
            ", record='" + record + '\'' +
            '}';
    }
}
