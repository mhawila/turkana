package org.muzima.turkana.model;

import org.springframework.stereotype.Repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "messaging_usage_logs")
public class UsageLogs {

    @Id
    private String uuid;

    @Column(name = "timestamp")
    private String timestamp;

    @Column(name = "phone_number")
    private String address;

    @Column(name = "logs")
    private String logsText;

    public UsageLogs() {
    }

    public UsageLogs(String uuid, String timestamp, String address, String logsText) {
        this.uuid = uuid;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
                "uuid='" + uuid + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
