package org.muzima.turkana.data.signal;

public enum RegisteredState {
    UNKNOWN(0), REGISTERED(1), NOT_REGISTERED(2);

    private final int id;

    RegisteredState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static RegisteredState fromId(int id) {
        return values()[id];
    }
}
