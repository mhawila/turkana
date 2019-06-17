package org.muzima.turkana.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DisplayRecord {

    @Id
    @GeneratedValue
    protected Long id;

    @Column(name ="thread_type")
    protected long type;

    @Column(name ="thread_recipient_phone")
    protected String recipient;

    @Column(name ="date_sent")
    protected long dateSent;

    @Column(name ="date_received")
    protected long dateReceived;

    @Column(name ="thread_id")
    protected long threadId;

    @Column(name ="thread_content")
    protected String body;

    @Column(name ="delivery_status")
    protected int deliveryStatus;

    @Column(name ="delivery_receipt_count")
    protected int deliveryReceiptCount;

    @Column(name ="read_receipt_count")
    protected int readReceiptCount;

    public DisplayRecord() {

    }

    public DisplayRecord(String body, long dateSent,
                         long dateReceived, long threadId, int deliveryStatus, int deliveryReceiptCount,
                         long type, int readReceiptCount) {
        this.threadId = threadId;
        this.recipient = recipient;
        this.dateSent = dateSent;
        this.dateReceived = dateReceived;
        this.type = type;
        this.body = body;
        this.deliveryReceiptCount = deliveryReceiptCount;
        this.readReceiptCount = readReceiptCount;
        this.deliveryStatus = deliveryStatus;
    }

    public long getType() {
        return type;
    }

    public long getDateSent() {
        return dateSent;
    }

    public long getDateReceived() {
        return dateReceived;
    }

    public long getThreadId() {
        return threadId;
    }

    public String getBody() {
        return body;
    }

    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public int getDeliveryReceiptCount() {
        return deliveryReceiptCount;
    }

    public int getReadReceiptCount() {
        return readReceiptCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DisplayRecord{" +
                "type=" + type +
                ", recipient=" + recipient +
                ", dateSent=" + dateSent +
                ", dateReceived=" + dateReceived +
                ", threadId=" + threadId +
                ", body='" + body + '\'' +
                ", deliveryStatus=" + deliveryStatus +
                ", deliveryReceiptCount=" + deliveryReceiptCount +
                ", readReceiptCount=" + readReceiptCount +
                '}';
    }
}
