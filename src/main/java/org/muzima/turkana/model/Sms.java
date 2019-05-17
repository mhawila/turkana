package org.muzima.turkana.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sms")
public class Sms extends Message {
    @Column(name = "signal_person")
    private String signalPerson;

    private String protocol;

    @Column(name = "replyPath_present")
    private String replyPathPresent;

    private String subject;

    @Column(name = "service_center")
    private String serviceCenter;

    private String uuid;

    @Column(name = "is_secure")
    private boolean isSecure;

    @Column(name = "is_key_exchange")
    private boolean isKeyExchange;

    @Column(name = "is_end_session")
    private boolean isEndSession;

    private int unidentified;

    private String type;

    public Sms() {
        this.transportType = "sms";
    }

    public String getSignalPerson() {
        return signalPerson;
    }

    public void setSignalPerson(String signalPerson) {
        this.signalPerson = signalPerson;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getReplyPathPresent() {
        return replyPathPresent;
    }

    public void setReplyPathPresent(String replyPathPresent) {
        this.replyPathPresent = replyPathPresent;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getServiceCenter() {
        return serviceCenter;
    }

    public void setServiceCenter(String serviceCenter) {
        this.serviceCenter = serviceCenter;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isSecure() {
        return isSecure;
    }

    public void setSecure(boolean secure) {
        isSecure = secure;
    }

    public boolean isKeyExchange() {
        return isKeyExchange;
    }

    public void setKeyExchange(boolean keyExchange) {
        isKeyExchange = keyExchange;
    }

    public boolean isEndSession() {
        return isEndSession;
    }

    public void setEndSession(boolean endSession) {
        isEndSession = endSession;
    }

    public int getUnidentified() {
        return unidentified;
    }

    public void setUnidentified(int unidentified) {
        this.unidentified = unidentified;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
