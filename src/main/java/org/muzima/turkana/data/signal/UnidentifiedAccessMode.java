package org.muzima.turkana.data.signal;

public enum UnidentifiedAccessMode {
    UNKNOWN(0), DISABLED(1), ENABLED(2), UNRESTRICTED(3);

    private final int mode;

    UnidentifiedAccessMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public static UnidentifiedAccessMode fromMode(int mode) {
        return values()[mode];
    }
}
