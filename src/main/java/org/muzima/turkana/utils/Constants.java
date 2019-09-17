package org.muzima.turkana.utils;

public class Constants {

    public static final class REGISTRATION_CONSTANTS {
        public static final String REGISTERED_STATE_UNKNOWN = "UNKNOWN";
        public static final String REGISTERED_STATE_REGISTERED = "REGISTERED";
        public static final String REGISTERED_STATE_MULTIDEVICE = "MULTIDEVICE";

        public static final class TWO_FACTOR_STATUS_CONSTANTS {
            public static final String TWO_FACTOR_FAILED = "verification_failed";
            public static final String TWO_FACTOR_NUMBER_ERROR = "maformed_phone_number";
            public static final String TWO_FACTOR_PENDING_VERIFICATION = "pending_code_verification";
            public static final String TWO_FACTOR_VERIFICATION_SECCESSFUL = "two_factor_successful";
        }
    }
}
