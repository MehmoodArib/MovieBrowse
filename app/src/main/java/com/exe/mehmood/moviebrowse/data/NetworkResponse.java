package com.exe.mehmood.moviebrowse.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/***A generic class that contains data and status about loading this data.
 * Used with Api's
 * @param <T> DataType returned by server on Api call.
 */

public class NetworkResponse<T> {
    @NonNull
    private final Status status;
    @Nullable
    private final T data;
    @Nullable
    private final String message;

    NetworkResponse(@NonNull Status status, @Nullable T data,
                    @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> NetworkResponse<T> success(@NonNull T data) {
        return new NetworkResponse<>(Status.SUCCESS, data, null);
    }

    public static <T> NetworkResponse<T> error(String msg, @Nullable T data) {
        return new NetworkResponse<>(Status.ERROR, data, msg);
    }

    public static <T> NetworkResponse<T> loading(@Nullable T data) {
        return new NetworkResponse<>(Status.LOADING, data, null);
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    public enum Status {SUCCESS, ERROR, LOADING}
}