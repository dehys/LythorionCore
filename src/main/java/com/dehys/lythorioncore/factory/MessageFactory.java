package com.dehys.lythorioncore.factory;

public enum MessageFactory {

    SERVER_START("Server started"),
    SERVER_STOP("Server stopped"),
    SERVER_RELOADING("Reloading server...");

    private final String message;

    MessageFactory(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
