package org.muzima.turkana.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "message_thread")
public class MessageThread extends DisplayRecord{

    @Column(name = "messages_count")
    private long count;

    @Column(name = "unread_messages_count")
    private int unreadCount;

    @Column(name = "distribution_type")
    private int distributionType;

    @Column(name ="archived_status")
    private boolean archived;

    @Column(name ="expiration_time")
    private long expiresIn;

    @Column(name = "message_latest_update")
    private long lastSeen;

    public MessageThread() {
    }

    public MessageThread(long count, int unreadCount, int distributionType, boolean archived, long expiresIn, long lastSeen) {
        this.count = count;
        this.unreadCount = unreadCount;
        this.distributionType = distributionType;
        this.archived = archived;
        this.expiresIn = expiresIn;
        this.lastSeen = lastSeen;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public int getDistributionType() {
        return distributionType;
    }

    public void setDistributionType(int distributionType) {
        this.distributionType = distributionType;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    @Override
    public String toString() {
        return "MessageThread{" +
                ", count=" + count +
                ", unreadCount=" + unreadCount +
                ", distributionType=" + distributionType +
                ", archived=" + archived +
                ", expiresIn=" + expiresIn +
                ", lastSeen=" + lastSeen +
                '}';
    }
}
