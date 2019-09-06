package org.muzima.turkana.web.signalclients;

import java.io.IOException;

public class SignalRegistration {

    private static final String TAG = SignalRegistration.class.getSimpleName();

    private MasterSecret masterSecret;

    public SignalRegistration() {

        generateMasterSecret();

    }

    private void initializeResources() {
    }

    private class RegisterGoogleCloudMessagingAsyncTask {

        private String REGISTRATION_ID = "312334754206";

        protected void onPreExecute() {
        }

        protected Void doInBackground(Void... voids) {
            throw new AssertionError("Gcm disabled");
        }

        protected void onPostExecute(Void aVoid) {

        }
    }

    private boolean generateMasterSecret() {
        String salt = "mac_salt";//settings.getString("mac_salt", null);
        if (salt == null) {
            MasterSecretUtil.generateMasterSecret(MasterSecretUtil.UNENCRYPTED_PASSPHRASE);
        }

        return true;
    }

    private class SecretGenerator {
        private MasterSecret masterSecret;

        protected void onPreExecute() {
            System.out.println("Generating Identity Keys");
        }

        protected Void doInBackground(String... params) {
            String passphrase = params[0];
            masterSecret = MasterSecretUtil.generateMasterSecret(
                passphrase);

            MasterSecretUtil.generateAsymmetricMasterSecret(masterSecret);
            IdentityKeyUtil.generateIdentityKeys();


            setMasterSecret(masterSecret);
            return null;
        }

        protected void onPostExecute(Void param) {

        }
    }

    protected void setMasterSecret(MasterSecret masterSecret) {
        this.masterSecret = masterSecret;
    }

    public void onServiceConnected() {

        masterSecret = null;
    }

    public void onServiceDisconnected() {
    }


}
