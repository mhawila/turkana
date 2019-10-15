package org.muzima.turkana.model;

public class Session {

    private String id;
    private String address;
    private String device;
    private String record;

    public Session(String id, String address, String device, String record) {
        this.id = id;
        this.address = address;
        this.device = device;
        this.record = record;
    }

    public Session() {
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

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    @Override
    public String toString() {
        return "Session{" +
            "id='" + id + '\'' +
            ", address='" + address + '\'' +
            ", device='" + device + '\'' +
            ", record='" + record + '\'' +
            '}';
    }
}
