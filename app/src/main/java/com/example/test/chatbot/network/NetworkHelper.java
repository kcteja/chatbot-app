package com.example.test.chatbot.network;

import com.android.volley.NetworkResponse;

public class NetworkHelper {

    public interface NetworkResponseHandler {
        void onSuccess(String response);

        void onFailure(NetworkResponse error);
    }
}
