package com.blackfox.app;

import com.blackfox.app.Slave;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface API {
    //@POST("/generateCode")
    //Call<String> generateCode(@Body GenerateCodeRequest request);
//
    //@POST("/addTime")
    //Call<Void> addTime(@Body AddTimeRequest request);
//
    //@POST("/addPlace")
    //Call<Void> addPlace(@Body AddPlaceRequest request);

    //@POST("/selectWorkersCount")
    //Call<List<Integer>> selectWorkersCount(@Body SelectWorkersCountRequest request);

    @POST("/activate")
    Call<String> activate(@Body ActivateRequest request);

    //@POST("/nearestTime")
    //Call<List<String>> nearestTime(@Body NearestTimeRequest request);
}

class ActivateRequest {
    private String code;

    // Конструктор, геттеры и сеттеры
    public ActivateRequest(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}