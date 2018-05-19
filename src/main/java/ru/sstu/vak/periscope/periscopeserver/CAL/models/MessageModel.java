package ru.sstu.vak.periscope.periscopeserver.CAL.models;

public class MessageModel {
    public MessageModel(String authToken, UserModel user, String message) {
        this.authToken = authToken;
        this.user = user;
        this.message = message;
    }

    private String authToken;
    private UserModel user;
    private String message;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
