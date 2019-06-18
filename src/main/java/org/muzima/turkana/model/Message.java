package org.muzima.turkana.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class Message {

    @Id
    @GeneratedValue
    protected Long id;

    @Column(name = "date_sent")
    protected LocalDateTime dateSent;

    @Column(name = "date_received")
    protected LocalDateTime dateReceived;

    @Column(name = "thread_id")
    protected String threadId;

    @Column(name = "is_read")
    protected Byte isRead;

    protected String body;

    @Column(name = "delivery_status")
    protected String deliveryStatus;

    @Column(name = "recipient_address")
    protected String recipientAddress;

    @Column(name = "recipient_device_id")
    protected String recipientDeviceId;

    @Column(name = "delivery_receipt_count")
    protected Short deliveryReceiptCount;

    @Column(name = "read_receipt_count")
    protected Short readReceiptCount;

    @Column(name = "mismatched_identities")
    protected String mismatchedIdentities;

    @Column(name = "subscription_id")
    protected String subscriptionId;

    @Column(name = "expires_in")
    protected String expiresIn;

    @Column(name = "expire_started")
    protected String expireStarted;

    protected String notification;

    @Column(name = "transport_type")
    protected String transportType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateSent() {
        return dateSent;
    }

    public void setDateSent(LocalDateTime dateSent) {
        this.dateSent = dateSent;
    }

    public LocalDateTime getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(LocalDateTime dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public Byte getIsRead() {
        return isRead;
    }

    public void setIsRead(Byte isRead) {
        this.isRead = isRead;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getRecipientDeviceId() {
        return recipientDeviceId;
    }

    public void setRecipientDeviceId(String recipientDeviceId) {
        this.recipientDeviceId = recipientDeviceId;
    }

    public Short getDeliveryReceiptCount() {
        return deliveryReceiptCount;
    }

    public void setDeliveryReceiptCount(Short deliveryReceiptCount) {
        this.deliveryReceiptCount = deliveryReceiptCount;
    }

    public Short getReadReceiptCount() {
        return readReceiptCount;
    }

    public void setReadReceiptCount(Short readReceiptCount) {
        this.readReceiptCount = readReceiptCount;
    }

    public String getMismatchedIdentities() {
        return mismatchedIdentities;
    }

    public void setMismatchedIdentities(String mismatchedIdentities) {
        this.mismatchedIdentities = mismatchedIdentities;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getExpireStarted() {
        return expireStarted;
    }

    public void setExpireStarted(String expireStarted) {
        this.expireStarted = expireStarted;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }


}
