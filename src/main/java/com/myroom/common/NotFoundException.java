package com.myroom.common;

public class NotFoundException extends RuntimeException {
    private final String messageKey;
    private final Object[] args;

    public NotFoundException(String messageKey, Object... args) {
        super(messageKey);
        this.messageKey = messageKey;
        this.args = args;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getArgs() {
        return args;
    }
}
