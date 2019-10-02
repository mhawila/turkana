package org.muzima.turkana.data.signal;

import com.sun.jndi.toolkit.url.Uri;
import org.apache.http.util.TextUtils;
import org.muzima.turkana.model.Recipient;
import org.muzima.turkana.utils.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.concurrent.ListenableFutureTask;
import org.whispersystems.libsignal.util.guava.Optional;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class RecipientProvider {

    @SuppressWarnings("unused")
    private static final String TAG = RecipientProvider.class.getSimpleName();

    @Value("${turkana.phonenumber}")
    public String LOCAL_PHONE_NUMBER;

    private static final RecipientCache recipientCache = new RecipientCache();
    private static final ExecutorService asyncRecipientResolver = Util.newSingleThreadedLifoExecutor();

    private static final Map<String, RecipientDetails> STATIC_DETAILS = new HashMap<String, RecipientDetails>() {{
        put("262966", new RecipientDetails("Amazon", null, false, false, null, null));
    }};


    public Recipient getRecipient(String address, Optional<RecipientSettings> settings, boolean asynchronous) {
        Recipient cachedRecipient = recipientCache.get(address);

        if (cachedRecipient != null && (asynchronous || !cachedRecipient.isResolving()) && ((!settings.isPresent()) || !cachedRecipient.isResolving() || cachedRecipient.getName() != null)) {
            return cachedRecipient;
        }

        Optional<RecipientDetails> prefetchedRecipientDetails = createPrefetchedRecipientDetails(address, settings);

        if (asynchronous) {
            cachedRecipient = new Recipient(address, cachedRecipient, prefetchedRecipientDetails, getRecipientDetailsAsync(address, settings));
        } else {
            cachedRecipient = new Recipient(address, getRecipientDetailsSync(address, settings, false));
        }

        recipientCache.set(address, cachedRecipient);
        return cachedRecipient;
    }


    public Optional<Recipient> getCached(String address) {
        return Optional.fromNullable(recipientCache.get(address));
    }

    private Optional<RecipientDetails> createPrefetchedRecipientDetails(String address, Optional<RecipientSettings> settings) {
        boolean isLocalNumber = address.equals(LOCAL_PHONE_NUMBER);
        return Optional.of(new RecipientDetails(null, null, !TextUtils.isEmpty(settings.get().getSystemDisplayName()), isLocalNumber, settings.get(), null));
    }

    private ListenableFutureTask<RecipientDetails> getRecipientDetailsAsync(final String address, final Optional<RecipientSettings> settings) {
        Callable<RecipientDetails> task = () -> getRecipientDetailsSync(address, settings,true);

        ListenableFutureTask<RecipientDetails> future = new ListenableFutureTask<>(task);
        asyncRecipientResolver.submit(future);
        return future;
    }

    private RecipientDetails getRecipientDetailsSync(String address, Optional<RecipientSettings> settings,boolean nestedAsynchronous) {
        return getIndividualRecipientDetails(address, settings);
    }

    private RecipientDetails getIndividualRecipientDetails(String address, Optional<RecipientSettings> settings) {

        if (!settings.isPresent() && STATIC_DETAILS.containsKey(address)) {
            return STATIC_DETAILS.get(address);
        } else {
            boolean systemContact = settings.isPresent() && !TextUtils.isEmpty(settings.get().getSystemDisplayName());
            boolean isLocalNumber = address.equals(LOCAL_PHONE_NUMBER);
            return new RecipientDetails(null, null, systemContact, isLocalNumber, settings.orNull(), null);
        }
    }

    public static class RecipientDetails {

        public String name;
        public String customLabel;
        public Uri systemContactPhoto;
        public Uri contactUri;
        public Long groupAvatarId;
        public Uri messageRingtone;
        public Uri callRingtone;
        public long mutedUntil;
        public boolean blocked;
        public int expireMessages;
        public List<Recipient> participants;

        public String profileName;
        public boolean seenInviteReminder;
        public Optional<Integer> defaultSubscriptionId;

        public RegisteredState registered;

        public byte[] profileKey;

        public String profileAvatar;
        public boolean profileSharing;
        public boolean systemContact;
        public boolean isLocalNumber;

        public String notificationChannel;

        public UnidentifiedAccessMode unidentifiedAccessMode;

        public RecipientDetails(String name, Long groupAvatarId,
                         boolean systemContact, boolean isLocalNumber, RecipientSettings settings,
                         List<Recipient> participants) {
            this.groupAvatarId = groupAvatarId;
            this.systemContactPhoto = settings != null ? Util.uri(settings.getSystemContactPhotoUri()) : null;
            this.customLabel = settings != null ? settings.getSystemPhoneLabel() : null;
            this.contactUri = settings != null ? Util.uri(settings.getSystemContactUri()) : null;
            this.messageRingtone = settings != null ? settings.getMessageRingtone() : null;
            this.callRingtone = settings != null ? settings.getCallRingtone() : null;
            this.mutedUntil = settings != null ? settings.getMuteUntil() : 0;
            this.blocked = settings != null && settings.isBlocked();
            this.expireMessages = settings != null ? settings.getExpireMessages() : 0;
            this.participants = participants == null ? new LinkedList<>() : participants;
            this.profileName = settings != null ? settings.getProfileName() : null;
            this.seenInviteReminder = settings != null && settings.hasSeenInviteReminder();
            this.defaultSubscriptionId = settings != null ? settings.getDefaultSubscriptionId() : Optional.absent();
            this.registered = settings != null ? settings.getRegistered() : RegisteredState.UNKNOWN;
            this.profileKey = settings != null ? settings.getProfileKey() : null;
            this.profileAvatar = settings != null ? settings.getProfileAvatar() : null;
            this.profileSharing = settings != null && settings.isProfileSharing();
            this.systemContact = systemContact;
            this.isLocalNumber = isLocalNumber;
            this.notificationChannel = settings != null ? settings.getNotificationChannel() : null;
            this.unidentifiedAccessMode = settings != null ? settings.getUnidentifiedAccessMode() : UnidentifiedAccessMode.DISABLED;

            if (name == null && settings != null) this.name = settings.getSystemDisplayName();
            else this.name = name;
        }
    }

    private static class RecipientCache {

        private final Map<String, Recipient> cache = new HashMap<>(1000);

        public synchronized Recipient get(String address) {
            return cache.get(address);
        }

        public synchronized void set(String address, Recipient recipient) {
            cache.put(address, recipient);
        }

    }

}