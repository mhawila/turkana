package org.muzima.turkana.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "registration")
public class Registration {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(name = "public_key", nullable = false)
    private String publicKey;

    @Column(name = "date_registered", nullable = false)
    private LocalDateTime dateRegistered;

    @Column
    private Boolean retired;

    @Column(name = "date_retired")
    private LocalDateTime dateRetired;

    public Registration() {
        this.retired = false;
        this.dateRegistered = LocalDateTime.now();
    }

    public Registration(String phoneNumber, String deviceId, String publicKey) {
        this.phoneNumber = phoneNumber;
        this.deviceId = deviceId;
        this.publicKey = publicKey;
        this.dateRegistered = LocalDateTime.now();
        this.retired = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public LocalDateTime getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(LocalDateTime dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public Boolean getRetired() {
        return retired;
    }

    public void setRetired(Boolean retired) {
        this.retired = retired;
    }

    public LocalDateTime getDateRetired() {
        return dateRetired;
    }

    public void setDateRetired(LocalDateTime dateRetired) {
        this.dateRetired = dateRetired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Registration that = (Registration) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (!phoneNumber.equals(that.phoneNumber)) return false;
        if (deviceId != null ? !deviceId.equals(that.deviceId) : that.deviceId != null) return false;
        return publicKey.equals(that.publicKey);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + phoneNumber.hashCode();
        result = 31 * result + (deviceId != null ? deviceId.hashCode() : 0);
        result = 31 * result + publicKey.hashCode();
        return result;
    }
}
