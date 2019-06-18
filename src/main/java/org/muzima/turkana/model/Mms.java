package org.muzima.turkana.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Willa aka Baba Imu on 5/17/19.
 */
@Entity
@Table(name = "mms")
public class Mms extends Message {
    @Column(name = "message_box")
    private String messageBox;

    @Column(name = "content_location")
    private String contentLocation;

    private String expiry;

    @Column(name = "message_size")
    private String messageSize;

    @Column(name = "message_type")
    protected String messageType;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "part_count")
    private String partCount;

    @Column(name = "network_failure")
    private String networkFailure;

    @Column(name = "quote_id")
    private String quoteId;

    @Column(name = "quote_author")
    private String quoteAuthor;

    @Column(name = "quote_body")
    private String quoteBody;

    @Column(name = "quote_attachment")
    private String quoteAttachment;

    @Column(name = "quote_missing")
    private String quoteMissing;

    @Column(name = "shared_contacts")
    private String sharedContacts;

    public Mms() {
        this.transportType = "mms";
    }

    public String getMessageBox() {
        return messageBox;
    }

    public void setMessageBox(String messageBox) {
        this.messageBox = messageBox;
    }

    public String getContentLocation() {
        return contentLocation;
    }

    public void setContentLocation(String contentLocation) {
        this.contentLocation = contentLocation;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(String messageSize) {
        this.messageSize = messageSize;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPartCount() {
        return partCount;
    }

    public void setPartCount(String partCount) {
        this.partCount = partCount;
    }

    public String getNetworkFailure() {
        return networkFailure;
    }

    public void setNetworkFailure(String networkFailure) {
        this.networkFailure = networkFailure;
    }

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    public String getQuoteAuthor() {
        return quoteAuthor;
    }

    public void setQuoteAuthor(String quoteAuthor) {
        this.quoteAuthor = quoteAuthor;
    }

    public String getQuoteBody() {
        return quoteBody;
    }

    public void setQuoteBody(String quoteBody) {
        this.quoteBody = quoteBody;
    }

    public String getQuoteAttachment() {
        return quoteAttachment;
    }

    public void setQuoteAttachment(String quoteAttachment) {
        this.quoteAttachment = quoteAttachment;
    }

    public String getQuoteMissing() {
        return quoteMissing;
    }

    public void setQuoteMissing(String quoteMissing) {
        this.quoteMissing = quoteMissing;
    }

    public String getSharedContacts() {
        return sharedContacts;
    }

    public void setSharedContacts(String sharedContacts) {
        this.sharedContacts = sharedContacts;
    }
}
