package org.muzima.turkana.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Created by Willa aka Baba Imu on 6/20/19.
 */
@Entity
@Table(name = "media_metadata")
public class MediaMetadata {

    @Id @GeneratedValue
    private Long id;

    @Column
    private String caption;

    @Column
    private String filename;

    @Column(name = "client_storage_dir")
    private String storageDir;

    @Column(name = "sender_phone_number")
    private String senderPhoneNumber;

    @Column(name = "date_received")
    private LocalDateTime dateReceived;

    @Column(name = "date_sent")
    private LocalDateTime dateSent;

    @Column
    private Integer size;

    @Column(name = "server_file_path")
    private String serverFilePath;

    @Column(name = "thread_id")
    private Long threadId;

    @Column(name = "outgoing_status")
    private Boolean outgoingStatus;

    @Column(name = "incoming_status")
    private Boolean incomingStatus;

    @Column
    private String extension;

    @Column(name = "media_type")
    private String mediaType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", referencedColumnName = "id")
    private Registration registration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(LocalDateTime dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getServerFilePath() {
        return serverFilePath;
    }

    public void setServerFilePath(String serverFilePath) {
        this.serverFilePath = serverFilePath;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getStorageDir() {
        return storageDir;
    }

    public void setStorageDir(String storageDir) {
        this.storageDir = storageDir;
    }

    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        this.senderPhoneNumber = senderPhoneNumber;
    }

    public LocalDateTime getDateSent() {
        return dateSent;
    }

    public void setDateSent(LocalDateTime dateSent) {
        this.dateSent = dateSent;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public Boolean getOutgoingStatus() {
        return outgoingStatus;
    }

    public void setOutgoingStatus(Boolean outgoingStatus) {
        this.outgoingStatus = outgoingStatus;
    }

    public Boolean getIncomingStatus() {
        return incomingStatus;
    }

    public void setIncomingStatus(Boolean incomingStatus) {
        this.incomingStatus = incomingStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaMetadata that = (MediaMetadata) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
