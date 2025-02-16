package com.blackfox.app;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface API {
    //@POST("/generateCode")
    //Call<String> generateCode(@Body GenerateCodeRequest request);

    //@POST("/addTime")
    //Call<Void> addTime(@Body AddTimeRequest request);

    //@POST("/addPlace")
    //Call<Void> addPlace(@Body AddPlaceRequest request);

    //@POST("/selectWorkersCount")
    //Call<List<Integer>> selectWorkersCount(@Body SelectWorkersCountRequest request);

    //@POST("/getUserList")
    //Call<>

    @GET("/allUsers")
    Call<ArrayList<CoolerUser>> getAllUsers();
    @POST("/activate")
    Call<User> activate(@Body Code code);

    @POST("/addUser")
    Call<String> addUser(@Body User user);

    @GET("/allPlaces")
    Call<ArrayList<Place>> getPlaces();

    @POST("/nearest")
    Call<Time> nearestShift(@Body Code code);
}

class User {

    public String fio;
    public String phone;
    public boolean isAdmin;

    public User(String fio, String phone, boolean isAdmin) {
        this.fio = fio;
        this.phone = phone;
        this.isAdmin = isAdmin;

    }

    public String getFio() {
        return fio;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}

//User Class with more info in it
class CoolerUser {
    private String phone;
    private boolean isAdmin;
    private String FIO;
    private String code;
    private boolean isActivated;

    public String getPhone() {
        return phone;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getFIO() {
        return FIO;
    }

    public String getCode() {
        return code;
    }

    public boolean isActivated() {
        return isActivated;
    }
}

class Code {
    private String code;

    public Code(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

class Time {
    private String code;
    private String address;
    private int date;
    private boolean first;
    private boolean second;
}

class Place {

    //GET request
    private String address;
    private String start;
    private String mid;
    private String end;

    public Place(String address, String start, String mid, String end) {
        this.address = address;
        this.start = start;
        this.mid = mid;
        this.end = end;
    }

    public String getAddressString() {
        return address;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }
}

class Places {
    private ArrayList<Place> places;

    public ArrayList<Place> getPlaces() {
        return places;
    }
}


class Count {
    int time;
    String address;
}

class Data {
    private int first;
    private int second;
}