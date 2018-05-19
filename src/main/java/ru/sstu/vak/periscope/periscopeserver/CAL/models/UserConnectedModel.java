package ru.sstu.vak.periscope.periscopeserver.CAL.models;

public class UserConnectedModel {
    public UserConnectedModel(String userLogin, String profileImagePath, int userColor, int observersCount, String token) {
        this.userLogin = userLogin;
        this.profileImagePath = profileImagePath;
        this.userColor = userColor;
        this.observersCount = observersCount;
        this.token = token;
    }

    private String userLogin;

    private String profileImagePath;

    private int userColor;

    private int observersCount;

    private String token;

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public int getUserColor() {
        return userColor;
    }

    public void setUserColor(int userColor) {
        this.userColor = userColor;
    }

    public int getObserversCount() {
        return observersCount;
    }

    public void setObserversCount(int observersCount) {
        this.observersCount = observersCount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
