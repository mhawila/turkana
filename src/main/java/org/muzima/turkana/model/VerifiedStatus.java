package org.muzima.turkana.model;

public enum VerifiedStatus {
    DEFAULT, VERIFIED, UNVERIFIED;

    public int toInt() {
        if (this == DEFAULT) return 0;
        else if (this == VERIFIED) return 1;
        else if (this == UNVERIFIED) return 2;
        else throw new AssertionError();
    }

    public static VerifiedStatus forState(int state) {
        if (state == 0) return DEFAULT;
        else if (state == 1) return VERIFIED;
        else if (state == 2) return UNVERIFIED;
        else throw new AssertionError("No such state: " + state);
    }
}
