package ru.sstu.vak.periscope.periscopeserver.CAL.models;

import javax.xml.stream.Location;
import java.util.ArrayList;

public class RoomModel {
//    public RoomModel(int id, String roomDescription, UserModel roomOwner, ArrayList<UserModel> observers, String streamName) {
//        this.id = id;
//        this.roomDescription = roomDescription;
//        this.roomOwner = roomOwner;
//        this.observers = observers;
//        this.streamName = streamName;
//    }

    private String roomDescription;
    private UserModel roomOwner;
    private ArrayList<UserModel> observers;
    private String streamName;
    private LocationModel location;

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public UserModel getRoomOwner() {
        return roomOwner;
    }

    public void setRoomOwner(UserModel roomOwner) {
        this.roomOwner = roomOwner;
    }

    public ArrayList<UserModel> getObservers() {
        return observers;
    }

    public void setObservers(ArrayList<UserModel> observers) {
        this.observers = observers;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }
}
