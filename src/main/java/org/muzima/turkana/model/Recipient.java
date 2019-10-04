package org.muzima.turkana.model;

import com.sun.jndi.toolkit.url.Uri;
import org.muzima.turkana.async.FutureTaskListener;
import org.muzima.turkana.async.ListenableFutureTask;
import org.muzima.turkana.data.signal.RecipientProvider;
import org.muzima.turkana.data.signal.RecipientSettings;
import org.muzima.turkana.data.signal.RegisteredState;
import org.muzima.turkana.data.signal.UnidentifiedAccessMode;
import org.muzima.turkana.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whispersystems.libsignal.util.guava.Optional;

import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class Recipient implements RecipientModifiedListener {

    public static Logger logger = LoggerFactory.getLogger(Recipient.class);
    private static final String TAG = Recipient.class.getSimpleName();
    private static final RecipientProvider provider = new RecipientProvider();

    private final Set<RecipientModifiedListener> listeners = Collections.newSetFromMap(new WeakHashMap<RecipientModifiedListener, Boolean>());

    private final String address;
    private final
    List<Recipient> participants = new LinkedList<>();

    private String name;
    private String customLabel;
    private boolean resolving;
    private boolean isLocalNumber;

    private Uri systemContactPhoto;
    private Long groupAvatarId;
    private Uri contactUri;
    private Uri messageRingtone = null;
    private Uri callRingtone = null;
    private long mutedUntil = 0;
    private boolean blocked = false;
    private int expireMessages = 0;
    private org.whispersystems.libsignal.util.guava.Optional<Integer> defaultSubscriptionId = org.whispersystems.libsignal.util.guava.Optional.absent();
    private RegisteredState registered = RegisteredState.UNKNOWN;

    private boolean seenInviteReminder;
    private byte[] profileKey;
    private String profileName;
    private String profileAvatar;
    private boolean profileSharing;
    private String notificationChannel;

    private UnidentifiedAccessMode unidentifiedAccessMode = UnidentifiedAccessMode.DISABLED;

    @SuppressWarnings("ConstantConditions")
    public static Recipient from(String address, boolean asynchronous) throws MalformedURLException {
        if (address == null) throw new AssertionError(address);
        return provider.getRecipient(address, org.whispersystems.libsignal.util.guava.Optional.absent(), asynchronous);
    }

    @SuppressWarnings("ConstantConditions")
    public static Recipient from(String address, org.whispersystems.libsignal.util.guava.Optional<RecipientSettings> settings, boolean asynchronous) throws MalformedURLException {
        if (address == null) throw new AssertionError(address);
        return provider.getRecipient(address, settings, asynchronous);
    }

    public static void applyCached(String address, Consumer<Recipient> consumer) {
        org.whispersystems.libsignal.util.guava.Optional<Recipient> recipient = provider.getCached(address);
        if (recipient.isPresent()) consumer.accept(recipient.get());
    }

    public Recipient(String address,
                     Recipient stale,
                     org.whispersystems.libsignal.util.guava.Optional<RecipientProvider.RecipientDetails> details,
                     ListenableFutureTask<RecipientProvider.RecipientDetails> future) {
        this.address = address;
        this.resolving = true;

        if (stale != null) {
            this.name = stale.name;
            this.contactUri = stale.contactUri;
            this.systemContactPhoto = stale.systemContactPhoto;
            this.groupAvatarId = stale.groupAvatarId;
            this.isLocalNumber = stale.isLocalNumber;
            this.customLabel = stale.customLabel;
            this.messageRingtone = stale.messageRingtone;
            this.callRingtone = stale.callRingtone;
            this.mutedUntil = stale.mutedUntil;
            this.blocked = stale.blocked;
            this.expireMessages = stale.expireMessages;
            this.seenInviteReminder = stale.seenInviteReminder;
            this.defaultSubscriptionId = stale.defaultSubscriptionId;
            this.registered = stale.registered;
            this.notificationChannel = stale.notificationChannel;
            this.profileKey = stale.profileKey;
            this.profileName = stale.profileName;
            this.profileAvatar = stale.profileAvatar;
            this.profileSharing = stale.profileSharing;
            this.unidentifiedAccessMode = stale.unidentifiedAccessMode;

            this.participants.clear();
            this.participants.addAll(stale.participants);
        }

        if (details.isPresent()) {
            this.name = details.get().name;
            this.systemContactPhoto = details.get().systemContactPhoto;
            this.groupAvatarId = details.get().groupAvatarId;
            this.isLocalNumber = details.get().isLocalNumber;
            this.messageRingtone = details.get().messageRingtone;
            this.callRingtone = details.get().callRingtone;
            this.mutedUntil = details.get().mutedUntil;
            this.blocked = details.get().blocked;
            this.expireMessages = details.get().expireMessages;
            this.seenInviteReminder = details.get().seenInviteReminder;
            this.defaultSubscriptionId = details.get().defaultSubscriptionId;
            this.registered = details.get().registered;
            this.notificationChannel = details.get().notificationChannel;
            this.profileKey = details.get().profileKey;
            this.profileName = details.get().profileName;
            this.profileAvatar = details.get().profileAvatar;
            this.profileSharing = details.get().profileSharing;
            this.unidentifiedAccessMode = details.get().unidentifiedAccessMode;

            this.participants.clear();
            this.participants.addAll(details.get().participants);
        }

        future.addListener(new FutureTaskListener<RecipientProvider.RecipientDetails>() {
            @Override
            public void onSuccess(RecipientProvider.RecipientDetails result) {
                if (result != null) {
                    synchronized (Recipient.this) {
                        Recipient.this.name = result.name;
                        Recipient.this.contactUri = result.contactUri;
                        Recipient.this.systemContactPhoto = result.systemContactPhoto;
                        Recipient.this.groupAvatarId = result.groupAvatarId;
                        Recipient.this.isLocalNumber = result.isLocalNumber;
                        Recipient.this.customLabel = result.customLabel;
                        Recipient.this.messageRingtone = result.messageRingtone;
                        Recipient.this.callRingtone = result.callRingtone;
                        Recipient.this.mutedUntil = result.mutedUntil;
                        Recipient.this.blocked = result.blocked;
                        Recipient.this.expireMessages = result.expireMessages;
                        Recipient.this.seenInviteReminder = result.seenInviteReminder;
                        Recipient.this.defaultSubscriptionId = result.defaultSubscriptionId;
                        Recipient.this.registered = result.registered;
                        Recipient.this.notificationChannel = result.notificationChannel;
                        Recipient.this.profileKey = result.profileKey;
                        Recipient.this.profileName = result.profileName;
                        Recipient.this.profileAvatar = result.profileAvatar;
                        Recipient.this.profileSharing = result.profileSharing;
                        Recipient.this.profileName = result.profileName;
                        Recipient.this.unidentifiedAccessMode = result.unidentifiedAccessMode;

                        Recipient.this.participants.clear();
                        Recipient.this.participants.addAll(result.participants);
                        Recipient.this.resolving = false;

                        if (!listeners.isEmpty()) {
                            for (Recipient recipient : participants) recipient.addListener(Recipient.this);
                        }

                        Recipient.this.notifyAll();
                    }

                    notifyListeners();
                }
            }

            @Override
            public void onFailure(ExecutionException error) {
                logger.info(error.getMessage());
            }
        });
    }

    public Recipient(String address, RecipientProvider.RecipientDetails details) {
        this.address = address;
        this.contactUri = details.contactUri;
        this.name = details.name;
        this.systemContactPhoto = details.systemContactPhoto;
        this.groupAvatarId = details.groupAvatarId;
        this.isLocalNumber = details.isLocalNumber;
        this.customLabel = details.customLabel;
        this.messageRingtone = details.messageRingtone;
        this.callRingtone = details.callRingtone;
        this.mutedUntil = details.mutedUntil;
        this.blocked = details.blocked;
        this.expireMessages = details.expireMessages;
        this.seenInviteReminder = details.seenInviteReminder;
        this.defaultSubscriptionId = details.defaultSubscriptionId;
        this.registered = details.registered;
        this.notificationChannel = details.notificationChannel;
        this.profileKey = details.profileKey;
        this.profileName = details.profileName;
        this.profileAvatar = details.profileAvatar;
        this.profileSharing = details.profileSharing;
        this.unidentifiedAccessMode = details.unidentifiedAccessMode;

        this.participants.addAll(details.participants);
        this.resolving = false;
    }

    public boolean isLocalNumber() {
        return isLocalNumber;
    }

    public synchronized Uri getContactUri() {
        return this.contactUri;
    }

    public void setContactUri(Uri contactUri) {
        boolean notify = false;

        synchronized (this) {
            if (!Util.equals(contactUri, this.contactUri)) {
                this.contactUri = contactUri;
                notify = true;
            }
        }

        if (notify) notifyListeners();
    }

    public synchronized String getName() {
        if (this.name == null && isMmsGroupRecipient()) {
            List<String> names = new LinkedList<>();

            for (Recipient recipient : participants) {
                names.add(recipient.toShortString());
            }

            return Util.join(names, ", ");
        }

        return this.name;
    }

    public void setName(String name) {
        boolean notify = false;

        synchronized (this) {
            if (!Util.equals(this.name, name)) {
                this.name = name;
                notify = true;
            }
        }

        if (notify) notifyListeners();
    }

    public String getAddress() {
        return address;
    }

    public synchronized String getCustomLabel() {
        return customLabel;
    }

    public void setCustomLabel(String customLabel) {
        boolean notify = false;

        synchronized (this) {
            if (!Util.equals(customLabel, this.customLabel)) {
                this.customLabel = customLabel;
                notify = true;
            }
        }

        if (notify) notifyListeners();
    }

    public synchronized org.whispersystems.libsignal.util.guava.Optional<Integer> getDefaultSubscriptionId() {
        return defaultSubscriptionId;
    }

    public void setDefaultSubscriptionId(Optional<Integer> defaultSubscriptionId) {
        synchronized (this) {
            this.defaultSubscriptionId = defaultSubscriptionId;
        }

        notifyListeners();
    }

    public synchronized String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        synchronized (this) {
            this.profileName = profileName;
        }

        notifyListeners();
    }

    public synchronized String getProfileAvatar() {
        return profileAvatar;
    }

    public void setProfileAvatar(String profileAvatar) {
        synchronized (this) {
            this.profileAvatar = profileAvatar;
        }

        notifyListeners();
    }

    public synchronized boolean isProfileSharing() {
        return profileSharing;
    }

    public void setProfileSharing(boolean value) {
        synchronized (this) {
            this.profileSharing = value;
        }

        notifyListeners();
    }

    public boolean isGroupRecipient() {
        return false;
    }

    public boolean isMmsGroupRecipient() {
        return false;
    }

    public boolean isPushGroupRecipient() {
        return false;
    }

    public synchronized List<Recipient> getParticipants() {
        return new LinkedList<>(participants);
    }

    public void setParticipants(List<Recipient> participants) {
        synchronized (this) {
            this.participants.clear();
            this.participants.addAll(participants);
        }

        notifyListeners();
    }

    public synchronized void addListener(RecipientModifiedListener listener) {
        if (listeners.isEmpty()) {
            for (Recipient recipient : participants) recipient.addListener(this);
        }
        listeners.add(listener);
    }


    public synchronized String toShortString() {
        return (getName() == null ? address : getName());
    }

    public void setGroupAvatarId(Long groupAvatarId) {
        boolean notify = false;

        synchronized (this) {
            if (!Util.equals(this.groupAvatarId, groupAvatarId)) {
                this.groupAvatarId = groupAvatarId;
                notify = true;
            }
        }

        if (notify) notifyListeners();
    }

    public synchronized Uri getMessageRingtone() {
        if (messageRingtone != null && messageRingtone.getScheme() != null && messageRingtone.getScheme().startsWith("file")) {
            return null;
        }

        return messageRingtone;
    }

    public void setMessageRingtone(Uri ringtone) {
        synchronized (this) {
            this.messageRingtone = ringtone;
        }

        notifyListeners();
    }

    public synchronized Uri getCallRingtone() {
        if (callRingtone != null && callRingtone.getScheme() != null && callRingtone.getScheme().startsWith("file")) {
            return null;
        }

        return callRingtone;
    }

    public void setCallRingtone(Uri ringtone) {
        synchronized (this) {
            this.callRingtone = ringtone;
        }

        notifyListeners();
    }

    public synchronized boolean isMuted() {
        return System.currentTimeMillis() <= mutedUntil;
    }

    public void setMuted(long mutedUntil) {
        synchronized (this) {
            this.mutedUntil = mutedUntil;
        }

        notifyListeners();
    }

    public synchronized boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        synchronized (this) {
            this.blocked = blocked;
        }

        notifyListeners();
    }

    public synchronized int getExpireMessages() {
        return expireMessages;
    }

    public void setExpireMessages(int expireMessages) {
        synchronized (this) {
            this.expireMessages = expireMessages;
        }

        notifyListeners();
    }

    public synchronized boolean hasSeenInviteReminder() {
        return seenInviteReminder;
    }

    public void setHasSeenInviteReminder(boolean value) {
        synchronized (this) {
            this.seenInviteReminder = value;
        }

        notifyListeners();
    }

    public synchronized RegisteredState getRegistered() {
        if (isPushGroupRecipient()) return RegisteredState.REGISTERED;
        else if (isMmsGroupRecipient()) return RegisteredState.NOT_REGISTERED;

        return registered;
    }

    public void setRegistered(RegisteredState value) {
        boolean notify = false;

        synchronized (this) {
            if (this.registered != value) {
                this.registered = value;
                notify = true;
            }
        }

        if (notify) notifyListeners();
    }


    public synchronized byte[] getProfileKey() {
        return profileKey;
    }

    public void setProfileKey(byte[] profileKey) {
        synchronized (this) {
            this.profileKey = profileKey;
        }

        notifyListeners();
    }

    public synchronized UnidentifiedAccessMode getUnidentifiedAccessMode() {
        return unidentifiedAccessMode;
    }

    public void setUnidentifiedAccessMode(UnidentifiedAccessMode unidentifiedAccessMode) {
        synchronized (this) {
            this.unidentifiedAccessMode = unidentifiedAccessMode;
        }

        notifyListeners();
    }

    public synchronized boolean isSystemContact() {
        return contactUri != null;
    }

    public synchronized Recipient resolve() {
        while (resolving) Util.wait(this, 0);
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Recipient)) return false;

        Recipient that = (Recipient) o;

        return this.address.equals(that.address);
    }

    @Override
    public int hashCode() {
        return this.address.hashCode();
    }

    private void notifyListeners() {
        Set<RecipientModifiedListener> localListeners;

        synchronized (this) {
            localListeners = new HashSet<>(listeners);
        }

        for (RecipientModifiedListener listener : localListeners)
            listener.onModified(this);
    }

    @Override
    public void onModified(Recipient recipient) {
        notifyListeners();
    }

    public synchronized boolean isResolving() {
        return resolving;
    }


}
