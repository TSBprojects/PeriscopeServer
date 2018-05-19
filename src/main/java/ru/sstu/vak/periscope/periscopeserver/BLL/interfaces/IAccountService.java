package ru.sstu.vak.periscope.periscopeserver.BLL.interfaces;

import ru.sstu.vak.periscope.periscopeserver.CAL.models.UserModel;

public interface IAccountService {
    String loginUser(String login, String password);

    String registerUser(String login, String password);

    boolean isTokenValid(String token);

    UserModel getUser(String token);

    boolean updateUser(UserModel user, String token);

    void setUserProfileImage(String imgPath, String token);

    boolean isNeedUpdate(String date, String token);
}
