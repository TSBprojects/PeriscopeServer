package ru.sstu.vak.periscope.periscopeserver.CAL.controllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import ru.sstu.vak.periscope.periscopeserver.BLL.services.AccountService;
import ru.sstu.vak.periscope.periscopeserver.CAL.models.MessageModel;
import ru.sstu.vak.periscope.periscopeserver.CAL.models.UserModel;

@RestController
public class ChatController {

    private final AccountService accServ;
    private Gson gson = new Gson();

    @Autowired
    public ChatController(AccountService accServ) {
        this.accServ = accServ;
    }

    @MessageMapping("/sendMessage/{streamName}")
    @SendTo("/receiveMessage/{streamName}")
    public String sendMessage(String message) {
        MessageModel messageModel = gson.fromJson(message, MessageModel.class);
        String authToken = messageModel.getAuthToken();
        UserModel user = accServ.getUser(authToken);
        user.setUserAlphaColor(messageModel.getUser().getUserAlphaColor());
        messageModel.setUser(user);
        messageModel.setAuthToken("");
        return gson.toJson(messageModel);
    }
}
