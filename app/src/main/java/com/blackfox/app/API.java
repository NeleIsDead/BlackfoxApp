package com.blackfox.app;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface API {

    @GET("/allPlaces")
    Call<ArrayList<Place>> getPlaces();
    @GET("/allUsers")
    Call<ArrayList<CoolerUser>> getAllUsers();
    @POST("/addTime")
    Call<String> enrollForShift(@Body Time time);
    @POST("/countByTime")
    Call<Data> getWorkerNumForShift(@Body Count count);
    @POST("/activate")
    Call<User> activate(@Body Code code);
    @POST("/addUser")
    Call<String> addUser(@Body User user);
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
    private String fio;
    private String code;
    private boolean isActivated;

    public String getPhone() {
        return phone;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getFIO() {
        return fio;
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
    private long date;
    private boolean first;
    private boolean second;

    public String getCode() {
        return code;
    }

    public String getAddress() {
        return address;
    }

    public long getDate() {
        return date*1000;
    }

    public boolean isFirst() {
        return first;
    }

    public boolean isSecond() {
        return second;
    }

    public Time(String code, String address, long date, boolean first, boolean second) {
        this.code = code;
        this.address = address;
        this.date = date;
        this.first = first;
        this.second = second;
    }
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

    public String getMid() {
        return mid;
    }

    public String getAddress() {
        return address;
    }
}
class Places {
    private ArrayList<Place> places;
    public ArrayList<Place> getPlaces() {
        return places;
    }
}


class Count {
    long time;
    String address;

    public Count(long time, String address) {
        this.time = time;
        this.address = address;
    }
}

class Data {
    private int first;
    private int second;

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }
}