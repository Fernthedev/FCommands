package com.github.fernthedev.fcommands.proxy.data.pref;

public class PrefDataParameter<T> {

    private final T data;

    public PrefDataParameter(T data) {
        this.data = data;
    }

    public T getData() {
        return this.data;
    }
}
