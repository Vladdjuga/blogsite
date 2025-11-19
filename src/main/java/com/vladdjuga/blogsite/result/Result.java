package com.vladdjuga.blogsite.result;

public class Result<T> {
    public final T value;
    public final Exception error;
    public final boolean isSuccess;

    private Result(T value, Exception error) {
        this.value = value;
        this.error = error;
        this.isSuccess = error == null;
    }

    public static <T> Result<T> ok(T value) {
        return new Result<>(value, null);
    }

    public static <T> Result<T> fail(Exception e) {
        return new Result<>(null, e);
    }
}