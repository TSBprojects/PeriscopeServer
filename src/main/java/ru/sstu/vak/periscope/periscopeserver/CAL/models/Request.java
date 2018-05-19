package ru.sstu.vak.periscope.periscopeserver.CAL.models;

public class Request<T> {
    private T data;

    private String authToken;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
