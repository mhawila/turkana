package org.muzima.turkana.model;

import com.sun.jndi.toolkit.url.Uri;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.sun.org.apache.xml.internal.serializer.Method.UNKNOWN;

@Entity
@Table(name ="signal_recipient")
public class SignalRecipient {

    @Column(name ="address")
    private SignalAddress address;

    @Column(name ="name")
    private String name;

    @Column(name ="nickname")
    private String customLabel;

    @Column(name ="resolving_status")
    private boolean resolving;

    @Column(name ="system_contact_photo")
    private Uri systemContactPhoto;

    @Column(name ="group_avatar_id")
    private Long groupAvatarId;

    @Column(name ="contact_uri")
    private Uri contactUri;

    @Column(name ="muted_util")
    private long mutedUntil = 0;

    @Column(name ="blocked_status")
    private boolean blocked = false;

    @Column(name ="expire_messages")
    private int expireMessages = 0;

    @Column(name ="registered")
    private String registered = UNKNOWN;

    @Column(name ="seen_invite_reminder")
    private boolean seenInviteReminder;

    @Column(name ="profile_key")
    private byte[] profileKey;

    @Column(name ="profile_name")
    private String profileName;

    @Column(name ="profile_avatar")
    private String profileAvatar;

    @Column(name ="profile_sharing")
    private boolean profileSharing;

    @Column(name ="notification_channel")
    private String notificationChannel;

    public SignalRecipient() {
    }

    public SignalRecipient(SignalAddress address, List<SignalRecipient> participants, String name, String customLabel, boolean resolving, Uri systemContactPhoto, Long groupAvatarId, Uri contactUri, long mutedUntil, boolean blocked, int expireMessages, String registered, boolean seenInviteReminder, byte[] profileKey, String profileName, String profileAvatar, boolean profileSharing, String notificationChannel) {
        this.address = address;
        this.name = name;
        this.customLabel = customLabel;
        this.resolving = resolving;
        this.systemContactPhoto = systemContactPhoto;
        this.groupAvatarId = groupAvatarId;
        this.contactUri = contactUri;
        this.mutedUntil = mutedUntil;
        this.blocked = blocked;
        this.expireMessages = expireMessages;
        this.registered = registered;
        this.seenInviteReminder = seenInviteReminder;
        this.profileKey = profileKey;
        this.profileName = profileName;
        this.profileAvatar = profileAvatar;
        this.profileSharing = profileSharing;
        this.notificationChannel = notificationChannel;
    }

    @NonNull
    public SignalAddress getAddress() {
        return address;
    }

    public void setAddress(@NonNull SignalAddress address) {
        this.address = address;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getCustomLabel() {
        return customLabel;
    }

    public void setCustomLabel(@Nullable String customLabel) {
        this.customLabel = customLabel;
    }

    public boolean isResolving() {
        return resolving;
    }

    public void setResolving(boolean resolving) {
        this.resolving = resolving;
    }

    @Nullable
    public Uri getSystemContactPhoto() {
        return systemContactPhoto;
    }

    public void setSystemContactPhoto(@Nullable Uri systemContactPhoto) {
        this.systemContactPhoto = systemContactPhoto;
    }

    @Nullable
    public Long getGroupAvatarId() {
        return groupAvatarId;
    }

    public void setGroupAvatarId(@Nullable Long groupAvatarId) {
        this.groupAvatarId = groupAvatarId;
    }

    public Uri getContactUri() {
        return contactUri;
    }

    public void setContactUri(Uri contactUri) {
        this.contactUri = contactUri;
    }

    public long getMutedUntil() {
        return mutedUntil;
    }

    public void setMutedUntil(long mutedUntil) {
        this.mutedUntil = mutedUntil;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public int getExpireMessages() {
        return expireMessages;
    }

    public void setExpireMessages(int expireMessages) {
        this.expireMessages = expireMessages;
    }

    @NonNull
    public String getRegistered() {
        return registered;
    }

    public void setRegistered(@NonNull String registered) {
        this.registered = registered;
    }

    public boolean isSeenInviteReminder() {
        return seenInviteReminder;
    }

    public void setSeenInviteReminder(boolean seenInviteReminder) {
        this.seenInviteReminder = seenInviteReminder;
    }

    @Nullable
    public byte[] getProfileKey() {
        return profileKey;
    }

    public void setProfileKey(@Nullable byte[] profileKey) {
        this.profileKey = profileKey;
    }

    @Nullable
    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(@Nullable String profileName) {
        this.profileName = profileName;
    }

    @Nullable
    public String getProfileAvatar() {
        return profileAvatar;
    }

    public void setProfileAvatar(@Nullable String profileAvatar) {
        this.profileAvatar = profileAvatar;
    }

    public boolean isProfileSharing() {
        return profileSharing;
    }

    public void setProfileSharing(boolean profileSharing) {
        this.profileSharing = profileSharing;
    }

    public String getNotificationChannel() {
        return notificationChannel;
    }

    public void setNotificationChannel(String notificationChannel) {
        this.notificationChannel = notificationChannel;
    }

    @Override
    public String toString() {
        return "SignalRecipient{" +
                "address=" + address +
                ", name='" + name + '\'' +
                ", customLabel='" + customLabel + '\'' +
                ", resolving=" + resolving +
                ", systemContactPhoto=" + systemContactPhoto +
                ", groupAvatarId=" + groupAvatarId +
                ", contactUri=" + contactUri +
                ", mutedUntil=" + mutedUntil +
                ", blocked=" + blocked +
                ", expireMessages=" + expireMessages +
                ", registered='" + registered + '\'' +
                ", seenInviteReminder=" + seenInviteReminder +
                ", profileKey=" + Arrays.toString(profileKey) +
                ", profileName='" + profileName + '\'' +
                ", profileAvatar='" + profileAvatar + '\'' +
                ", profileSharing=" + profileSharing +
                ", notificationChannel='" + notificationChannel + '\'' +
                '}';
    }
}
