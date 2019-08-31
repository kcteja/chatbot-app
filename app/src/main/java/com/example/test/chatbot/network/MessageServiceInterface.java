package com.example.test.chatbot.network;

import com.example.test.chatbot.models.NetworkResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MessageServiceInterface {

    @GET("/api/chat")
    Call<NetworkResponse> sendMessage(@Query("apiKey") String apiKey, @Query("chatBotID") String chatBotID,
                                      @Query("externalID") String externalID, @Query("message") String message);
}
