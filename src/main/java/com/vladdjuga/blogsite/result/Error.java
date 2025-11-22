package com.vladdjuga.blogsite.result;

public class Error {
    public final Exception error;
    public final String message;
    public final boolean isException;

    public Error(String message, Exception error) {
        this.error = error;
        this.message = message;
        this.isException = true;
    }
    public Error(String message) {
        this.error = null;
        this.message = message;
        this.isException = false;
    }
}
