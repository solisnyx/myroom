package com.myroom.common;

public class ForbiddenException extends RuntimeException {
    private final String messageKey;
    private final Object[] args;

    public ForbiddenException(String messageKey, Object... args) {
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
