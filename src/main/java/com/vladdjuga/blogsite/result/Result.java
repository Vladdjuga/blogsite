package com.vladdjuga.blogsite.result;

public class Result<T> {
    public final T value;
    public Error error;
    public final boolean isSuccess;

    private Result(T value,Error error) {
        this.value = value;
        this.error = error;
        this.isSuccess = error == null;
    }

    public static <T> Result<T> ok(T value) {
        return new Result<>(value, null);
    }

    public static <T> Result<T> fail(String message) {
        var error = new Error(message);
        return new Result<>(null, error);
    }

    public static <T> Result<T> exception(String message,Exception e) {
        var error = new Error(message, e);
        return new Result<>(null, error);
    }
}