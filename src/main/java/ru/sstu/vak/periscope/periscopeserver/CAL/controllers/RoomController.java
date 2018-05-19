package ru.sstu.vak.periscope.periscopeserver.CAL.controllers;


import com.google.gson.Gson;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.sstu.vak.periscope.periscopeserver.BLL.services.AccountService;
import ru.sstu.vak.periscope.periscopeserver.CAL.models.*;

import java.util.ArrayList;
import java.util.Random;

@RestController
public class RoomController {
    private final AccountService accServ;
    private ArrayList<RoomModel> rooms = new ArrayList<>();
    private Gson gson = new Gson();

    @Autowired
    public RoomController(AccountService accServ) {
        this.accServ = accServ;
    }


    @PostMapping("/getRoom")
    public Response<RoomModel> getRoom(@RequestBody Request<String> request) {
        Response<RoomModel> res = checkToken(request.getAuthToken());
        if (res != null) {
            return res;
        }


        String streamName = request.getData();

        for (RoomModel room : rooms) {
            if (room.getStreamName().equals(streamName)) {
                return new Response<>(room, null);
            }
        }
        return new Response<>(null, null);
    }

    @PostMapping("/getRooms")
    public Response<ArrayList<RoomModel>> getRooms(@RequestBody Request<Void> request) {
        Response<ArrayList<RoomModel>> res = checkToken(request.getAuthToken());
        if (res != null) {
            return res;
        }

        return new Response<>(rooms, null);
    }

    @PostMapping("/createRoom")
    public Response<Void> createRoom(@RequestBody Request<RoomModel> request) {
        String token = request.getAuthToken();
        Response<Void> res = checkToken(request.getAuthToken());
        if (res != null) {
            return res;
        }

        RoomModel requestRoomModel = request.getData();
        RoomModel roomModel = new RoomModel();

        roomModel.setRoomDescription(requestRoomModel.getRoomDescription());
        roomModel.setRoomOwner(accServ.getUser(token));
        roomModel.setObservers(new ArrayList<>());
        roomModel.setStreamName(requestRoomModel.getStreamName());
        roomModel.setLocation(requestRoomModel.getLocation());

        rooms.add(roomModel);


        return new Response<>(null, null);
    }

    @PostMapping("/removeRoom")
    public Response<Void> removeRoom(@RequestBody Request<RoomModel> request) {
        Response<Void> res = checkToken(request.getAuthToken());
        if (res != null) {
            return res;
        }

        String streamName = request.getData().getStreamName();
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getStreamName().equals(streamName)) {
                rooms.remove(i);
            }
        }

        return new Response<>(null, null);
    }

    @PostMapping("/joinBroadcast")
    public Response<RoomModel> joinBroadcast(@RequestBody Request<RoomModel> request) {
        String token = request.getAuthToken();
        Response<RoomModel> res = checkToken(request.getAuthToken());
        if (res != null) {
            return res;
        }

        String streamName = request.getData().getStreamName();
        for (RoomModel room : rooms) {
            if (room.getStreamName().equals(streamName)) {
                room.getObservers().add(accServ.getUser(token));
                return new Response<>(room, null);
            }
        }
        return new Response<>(null, null);
    }

    @PostMapping("/leaveBroadcast")
    public Response<Void> leaveBroadcast(@RequestBody Request<RoomModel> request) {
        String token = request.getAuthToken();
        Response<Void> res = checkToken(request.getAuthToken());
        if (res != null) {
            return res;
        }

        String streamName = request.getData().getStreamName();
        for (RoomModel room : rooms) {
            if (room.getStreamName().equals(streamName)) {
                UserModel user = accServ.getUser(token);
                ArrayList<UserModel> observers = room.getObservers();
                for (int j = 0; j < observers.size(); j++) {
                    if (observers.get(j).getId() == user.getId()) {
                        observers.remove(j);
                    }
                }
            }
        }
        return new Response<>(null, null);
    }


    @MessageMapping("/connectUser/{streamName}")
    @SendTo("/userConnected/{streamName}")
    public String connectUser(@DestinationVariable String streamName, String message) {
        UserConnectedModel userConnectedModel = gson.fromJson(message, UserConnectedModel.class);
        String authToken = userConnectedModel.getToken();

        UserModel user = accServ.getUser(authToken);
        int observersCount = getObserversCount(streamName);

        return gson.toJson(new UserConnectedModel(user.getLogin(), user.getProfileImagePath(),userConnectedModel.getUserColor(), observersCount, authToken));
    }

    @MessageMapping("/disconnectUser/{streamName}")
    @SendTo("/disconnectedUser/{streamName}")
    public String disconnectUser(@DestinationVariable String streamName, String message) {
        UserConnectedModel userConnectedModel = gson.fromJson(message, UserConnectedModel.class);
        String authToken = userConnectedModel.getToken();

        UserModel user = accServ.getUser(authToken);
        int observersCount = getObserversCount(streamName);

        return gson.toJson(new UserConnectedModel(user.getLogin(), user.getProfileImagePath(), userConnectedModel.getUserColor(), observersCount, null));
    }

    @MessageMapping("/addHeart/{streamName}")
    @SendTo("/heartAdded/{streamName}")
    public String addHeart(@DestinationVariable String streamName, String message) {
        int color = gson.fromJson(message, Integer.class);
        return gson.toJson(color);
    }

    @MessageMapping("/updateRoomLocation")
    @SendTo("/roomLocationUpdated")
    public String updateRoomLocation(String message) {
        RoomModel requestRoomModel = gson.fromJson(message, RoomModel.class);
        updateStreamLocation(requestRoomModel.getStreamName(),requestRoomModel.getLocation());
        return gson.toJson(requestRoomModel);
    }

    private void updateStreamLocation(String streamName, LocationModel location) {
        for (RoomModel room : rooms) {
            if (room.getStreamName().equals(streamName)) {
                room.setLocation(location);
            }
        }
    }

    private int getObserversCount(String streamName) {
        for (RoomModel room : rooms) {
            if (room.getStreamName().equals(streamName)) {
                return room.getObservers().size();
            }
        }
        return -1;
    }

    private <T> Response<T> checkToken(String token) {
        if (token != null && !token.equals("") && accServ.isTokenValid(token)) {
            return null;
        } else {
            return new Response<>(null, "invalid authToken");
        }
    }
}
