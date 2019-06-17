package org.muzima.turkana.model;

import org.springframework.stereotype.Repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "messaging_usage_logs")
public class UsageLogs {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "timestamp")
    private String timestamp;

    @Column(name = "phone_number")
    private String address;

    @Column(name = "logs")
    private String logsText;

    public UsageLogs() {
    }

    public UsageLogs(Long id, String timestamp, String address, String logsText) {
        this.id = id;
        this.timestamp = timestamp;
        this.address = address;
        this.logsText = logsText;
    }

    public String getLogsText() {
        return logsText;
    }

    public void setLogsText(String logsText) {
        this.logsText = logsText;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "UsageLogs{" +
                "id='" + id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
