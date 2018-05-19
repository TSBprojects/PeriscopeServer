package ru.sstu.vak.periscope.periscopeserver.CAL.models;

public class Response<T> {
    public Response(T data, String error) {
        this.data = data;
        this.error = error;
    }

    private String error;

    private T data;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}