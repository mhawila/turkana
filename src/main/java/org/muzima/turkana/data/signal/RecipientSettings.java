package org.muzima.turkana.data.signal;

import com.sun.jndi.toolkit.url.Uri;
import org.whispersystems.libsignal.util.guava.Optional;

public class RecipientSettings {
    private final boolean blocked;
    private final long muteUntil;
    private final Uri messageRingtone;
    private final Uri callRingtone;
    private final boolean seenInviteReminder;
    private final int defaultSubscriptionId;
    private final int expireMessages;
    private final RegisteredState registered;
    private final byte[] profileKey;
    private final String systemDisplayName;
    private final String systemContactPhoto;
    private final String systemPhoneLabel;
    private final String systemContactUri;
    private final String signalProfileName;
    private final String signalProfileAvatar;
    private final boolean profileSharing;
    private final String notificationChannel;
    private final UnidentifiedAccessMode unidentifiedAccessMode;

    RecipientSettings(boolean blocked, long muteUntil,
                      Uri messageRingtone,
                      Uri callRingtone,
                      boolean seenInviteReminder,
                      int defaultSubscriptionId,
                      int expireMessages,
                      RegisteredState registered,
                      byte[] profileKey,
                      String systemDisplayName,
                      String systemContactPhoto,
                      String systemPhoneLabel,
                      String systemContactUri,
                      String signalProfileName,
                      String signalProfileAvatar,
                      boolean profileSharing,
                      String notificationChannel,
                      UnidentifiedAccessMode unidentifiedAccessMode) {
        this.blocked = blocked;
        this.muteUntil = muteUntil;
        this.messageRingtone = messageRingtone;
        this.callRingtone = callRingtone;
        this.seenInviteReminder = seenInviteReminder;
        this.defaultSubscriptionId = defaultSubscriptionId;
        this.expireMessages = expireMessages;
        this.registered = registered;
        this.profileKey = profileKey;
        this.systemDisplayName = systemDisplayName;
        this.systemContactPhoto = systemContactPhoto;
        this.systemPhoneLabel = systemPhoneLabel;
        this.systemContactUri = systemContactUri;
        this.signalProfileName = signalProfileName;
        this.signalProfileAvatar = signalProfileAvatar;
        this.profileSharing = profileSharing;
        this.notificationChannel = notificationChannel;
        this.unidentifiedAccessMode = unidentifiedAccessMode;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public long getMuteUntil() {
        return muteUntil;
    }


    public Uri getMessageRingtone() {
        return messageRingtone;
    }

    public Uri getCallRingtone() {
        return callRingtone;
    }

    public boolean hasSeenInviteReminder() {
        return seenInviteReminder;
    }

    public Optional<Integer> getDefaultSubscriptionId() {
        return defaultSubscriptionId != -1 ? Optional.of(defaultSubscriptionId) : Optional.absent();
    }

    public int getExpireMessages() {
        return expireMessages;
    }

    public RegisteredState getRegistered() {
        return registered;
    }

    public byte[] getProfileKey() {
        return profileKey;
    }

    public String getSystemDisplayName() {
        return systemDisplayName;
    }

    public String getSystemContactPhotoUri() {
        return systemContactPhoto;
    }

    public String getSystemPhoneLabel() {
        return systemPhoneLabel;
    }

    public String getSystemContactUri() {
        return systemContactUri;
    }

    public String getProfileName() {
        return signalProfileName;
    }

    public String getProfileAvatar() {
        return signalProfileAvatar;
    }

    public boolean isProfileSharing() {
        return profileSharing;
    }

    public String getNotificationChannel() {
        return notificationChannel;
    }

    public UnidentifiedAccessMode getUnidentifiedAccessMode() {
        return unidentifiedAccessMode;
    }
}