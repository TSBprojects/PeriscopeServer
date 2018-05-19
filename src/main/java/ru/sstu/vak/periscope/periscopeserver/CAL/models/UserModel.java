package ru.sstu.vak.periscope.periscopeserver.CAL.models;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

public class UserModel {
//    public UserModel(int id, String login, String profileImagePath, String firstName, String lastName, String aboutMe) {
//        this.id = id;
//        this.login = login;
//        this.profileImagePath = profileImagePath;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.aboutMe = aboutMe;
//    }

    private int id;

    private String login;

    private String profileImagePath;

    private int userAlphaColor;

    private String firstName;

    private String lastName;

    private String aboutMe;

    private Date updateDate;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public int getUserAlphaColor() {
        return userAlphaColor;
    }

    public void setUserAlphaColor(int userAlphaColor) {
        this.userAlphaColor = userAlphaColor;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
