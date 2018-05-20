package ru.sstu.vak.periscope.periscopeserver.CAL.controllers;

import com.sun.jndi.toolkit.url.Uri;
import com.sun.org.apache.xml.internal.security.keys.storage.StorageResolver;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.repository.query.Param;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.sstu.vak.periscope.periscopeserver.BLL.services.AccountService;
import ru.sstu.vak.periscope.periscopeserver.CAL.models.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.sstu.vak.periscope.periscopeserver.BLL.services.AccountService.PROFILE_IMG_FOLDER;

@RestController
public class AccountController {
    private final AccountService accServ;

    @Autowired
    public AccountController(AccountService accServ) {
        this.accServ = accServ;
    }

    @PostMapping("/registration")
    public Response<String> registration(@RequestBody Request<LoginModel> request) {
        LoginModel loginModel = request.getData();
        String token = accServ.registerUser(loginModel.getLogin(), loginModel.getPassword());
        if (token != null) {
            return new Response<>(token, null);
        } else {
            return new Response<>(null, "этот логин уже занят!");
        }
    }

    @PostMapping("/login")
    public Response<String> login(@RequestBody Request<LoginModel> request) {
        LoginModel loginModel = request.getData();
        String token = accServ.loginUser(loginModel.getLogin(), loginModel.getPassword());
        if (token != null) {
            return new Response<>(token, null);
        } else {
            return new Response<>(null, "неправильный логин или пароль!");
        }
    }

    @PostMapping("/getUser")
    public Response<UserModel> getUser(@RequestBody Request<Void> request) {
        Response<UserModel> res = checkToken(request.getAuthToken());
        if (res != null) {
            return res;
        }

        UserModel user = accServ.getUser(request.getAuthToken());

        return new Response<>(user, null);
    }

    @PostMapping("/saveChanges")
    public Response<Void> saveChanges(@RequestPart(name = "img", required = false) MultipartFile image,
                                      @RequestPart("request") Request<UserModel> request) throws IOException {
        String authToken = request.getAuthToken();
        Response<Void> res = checkToken(authToken);
        if (res != null) {
            return res;
        }
        UserModel userModel = request.getData();

        if (!accServ.updateUser(userModel, request.getAuthToken())) {
            return new Response<>(null, "логин уже занят");
        }

        if (image != null) {
            UserModel user = accServ.getUser(authToken);
            String fileType = getFileType(image.getOriginalFilename());
            String fileName = user.getId() + "." + fileType;
            String imgPath = PROFILE_IMG_FOLDER + fileName;


            File prevFile = new File("./public/profileImages/" + getFileName(user.getProfileImagePath()));
            if (!isDefaultImg(user.getProfileImagePath())) {
                prevFile.delete();
            }

            File file = new File("./public/profileImages/" + fileName);
            FileUtils.writeByteArrayToFile(file, image.getBytes());

            accServ.setUserProfileImage(imgPath, authToken);
        }

        return new Response<>(null, null);
    }
    @PostMapping("/checkForUpdates")
    public Response<UserModel> checkForUpdates(@RequestBody Request<String> request) {
        String authToken = request.getAuthToken();
        Response<UserModel> res = checkToken(authToken);
        if (res != null) {
            return res;
        }

        UserModel user = accServ.getUser(authToken);
        if (accServ.isNeedUpdate(request.getData(), authToken)) {
            UserModel respUser = new UserModel();
            respUser.setId(user.getId());
            respUser.setLogin(user.getLogin());
            respUser.setProfileImagePath(user.getProfileImagePath());
            respUser.setFirstName(user.getFirstName());
            respUser.setLastName(user.getLastName());
            respUser.setAboutMe(user.getAboutMe());
            respUser.setUpdateDate(user.getUpdateDate());
            return new Response<>(respUser, null);
        }
        return new Response<>(null, null);
    }


    private <T> Response<T> checkToken(String token) {
        if (!token.equals("") && accServ.isTokenValid(token)) {
            return null;
        } else {
            return new Response<>(null, "Invalid authToken");
        }
    }

    private String getFileType(String fileName) {
        Pattern p = Pattern.compile("(.+\\.)(.+)");
        Matcher m = p.matcher(fileName);

        if (m.matches()) {
            return m.group(2);
        } else {
            return "";
        }
    }

    private boolean isDefaultImg(String path) {
        Pattern p = Pattern.compile(".+default\\..+");
        Matcher m = p.matcher(path);

        return m.matches();
    }

    private String getFileName(String url) {
        Pattern p = Pattern.compile(".+\\/(.+)");
        Matcher m = p.matcher(url);

        if (m.matches()) {
            return m.group(1);
        } else {
            return "";
        }
    }
}

//    @PostMapping("/uploadImage")
//    public Response<String> uploadImage(@RequestPart("img") MultipartFile multipartFile,
//                                        @RequestHeader("authToken") String authToken) throws IOException {
//        Response<String> res = checkToken(authToken);
//        if (res != null) {
//            return res;
//        }
//
//        UserModel user = accServ.getUser(authToken);
//
//        String fileType = getFileType(multipartFile.getOriginalFilename());
//        String fileName = user.getId() + "." + fileType;
//        String imgPath = PROFILE_IMG_FOLDER + fileName;
//
//        File prevFile = new File("./public/profileImages/" + getFileName(user.getProfileImagePath()));
//        if (!isDefaultImg(user.getProfileImagePath())) {
//            prevFile.delete();
//        }
//
//        File file = new File("./public/profileImages/" + fileName);
//        FileUtils.writeByteArrayToFile(file, multipartFile.getBytes());
//
//        accServ.setUserProfileImage(imgPath, authToken);
//
//        return new Response<>(imgPath, null);
//    }
//    @PostMapping("/updateUser")
//    public Response<Void> updateUser(@RequestBody Request<UserModel> request) {
//        Response<Void> res = checkToken(request.getAuthToken());
//        if (res != null) {
//            return res;
//        }
//
//        if (!accServ.updateUser(request.getData(), request.getAuthToken())) {
//            return new Response<>(null, "логин уже знаят");
//        }
//
//        return new Response<>(null, null);
//    }