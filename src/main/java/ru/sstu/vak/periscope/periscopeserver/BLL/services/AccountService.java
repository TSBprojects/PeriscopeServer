package ru.sstu.vak.periscope.periscopeserver.BLL.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sstu.vak.periscope.periscopeserver.BLL.interfaces.IAccountService;
import ru.sstu.vak.periscope.periscopeserver.CAL.models.UserModel;
import ru.sstu.vak.periscope.periscopeserver.DAL.entity.User;
import ru.sstu.vak.periscope.periscopeserver.DAL.repositories.IUserRepository;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@Service
public class AccountService implements IAccountService {

    private final IUserRepository users;
    private MessageDigest messageDigest;

    public static final String PROFILE_IMG_FOLDER = "http://anton-var.ddns.net:8080/public/";

    @Autowired
    public AccountService(IUserRepository users) {
        try {
            this.messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        messageDigest.digest();
        this.users = users;
    }

    @Override
    public String loginUser(String login, String password) {
        User user = users.findByLoginAndHashPassword(login, getPasswordHash(password));
        if (user != null) {
            String token = generateAuthToken();
            user.setAuthToken(token);
            users.save(user);
            return token;
        }
        return null;
    }

    @Override
    public String registerUser(String login, String password) {
        User userExist = users.findByLogin(login);
        if (userExist == null) {
            User user = new User();
            String token = generateAuthToken();
            user.setLogin(login);
            user.setFirstName("");
            user.setLastName("");
            user.setAboutMe("");
            user.setProfileImagePath(PROFILE_IMG_FOLDER + "default.jpg");
            user.setHashPassword(getPasswordHash(password));
            user.setAuthToken(token);
            user.setUpdateDate(getCurrentDate());
            users.save(user);
            return token;
        }
        return null;
    }

    @Override
    public boolean isTokenValid(String token) {
        User user = users.findByAuthToken(token);
        return user != null;
    }

    @Override
    public UserModel getUser(String token) {
        User user = users.findByAuthToken(token);
        UserModel newUser = new UserModel();
        newUser.setId(user.getId());
        newUser.setLogin(user.getLogin());
        newUser.setProfileImagePath(user.getProfileImagePath());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setAboutMe(user.getAboutMe());
        newUser.setUpdateDate(user.getUpdateDate());
        return newUser;
    }

    @Override
    public boolean updateUser(UserModel userNew, String token) {
        User user = users.findByAuthToken(token);

        User userByLogin = users.findByLogin(userNew.getLogin());
        if (userByLogin != null && userByLogin.getId() != user.getId()) {
            return false;
        }

        user.setFirstName(userNew.getFirstName());
        user.setLastName(userNew.getLastName());
        user.setLogin(userNew.getLogin());
        if (!userNew.getProfileImagePath().equals("")) {
            user.setProfileImagePath(userNew.getProfileImagePath());
        }
        user.setAboutMe(userNew.getAboutMe());
        user.setUpdateDate(getCurrentDate());

        users.save(user);
        return true;
    }

    @Override
    public boolean isNeedUpdate(String date, String token) {
        if (date == null || date.equals("")) {
            return true;
        }
        User user = users.findByAuthToken(token);
        return user.getUpdateDate().after(getDateFromString(date));
    }

    @Override
    public void setUserProfileImage(String imgPath, String token) {
        User user = users.findByAuthToken(token);
        user.setProfileImagePath(imgPath);
        users.save(user);
    }


    private String generateAuthToken() {
        messageDigest.update(Long.toString(System.currentTimeMillis()).getBytes());
        byte[] digest = messageDigest.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }

    private String getPasswordHash(String password) {
        messageDigest.update(password.getBytes());
        byte[] digest = messageDigest.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }

    private Date getDateFromString(String str) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

        try {
            calendar.setTime(format.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getTime();
    }

    private Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }
}
