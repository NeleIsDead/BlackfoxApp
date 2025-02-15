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

    @POST("/activate")
    Call<User> activate(@Body Code code);

    @POST("/addUser")
    Call<String> addUser(@Body User user);

    @GET("/allPlaces")
    Call<Places> getPlaces();

    //@POST("/nearestTime")
    //Call<List<String>> nearestTime(@Body NearestTimeRequest request);
}
class User{
    @JsonProperty("FIO")
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
class Code{
    public String code;

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
    private String end;

    public Place(String address, String start, String end) {
        this.address = address;
        this.start = start;
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

