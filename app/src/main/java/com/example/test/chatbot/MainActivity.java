package com.example.test.chatbot;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.chatbot.adapters.MessagesAdapter;
import com.example.test.chatbot.models.Message;
import com.example.test.chatbot.models.NetworkResponse;
import com.example.test.chatbot.network.MessageServiceInterface;
import com.example.test.chatbot.network.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private MessagesAdapter messagesAdapter;
    private RecyclerView messagesList;
    private ImageView send;
    private EditText enteredMsg;
    private TextView waitingMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        waitingMessage = findViewById(R.id.waiting_message);
        enteredMsg = findViewById(R.id.entered_message);
        send = findViewById(R.id.send_message);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msgText = enteredMsg.getText().toString().trim();
                if (!msgText.isEmpty()) {
                    enteredMsg.setText("");
                    Message message = new Message(msgText, Message.MessageType.SENDER);
                    messagesAdapter.addMessage(message);
                    sendMessageToServer(msgText);
                }
            }
        });

        messagesList = findViewById(R.id.messages_list);
        messagesAdapter = new MessagesAdapter(messagesList, this);
        messagesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        messagesList.setAdapter(messagesAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        messagesAdapter.scrollToBottom();
    }

    private void sendMessageToServer(String message) {
        waitingMessage.setVisibility(View.VISIBLE);
        MessageServiceInterface serviceInterface = RetrofitClientInstance.getRetrofitInstance().create(MessageServiceInterface.class);
        Call<NetworkResponse> call = serviceInterface.sendMessage("6nt5d1nJHkqbkphe", "63906", "KCT", message);
        call.enqueue(new Callback<NetworkResponse>() {
            @Override
            public void onResponse(@NonNull Call<NetworkResponse> call, @NonNull Response<NetworkResponse> response) {
                waitingMessage.setVisibility(View.INVISIBLE);
                NetworkResponse networkResponse = response.body();
                if (networkResponse != null) {
                    Message message = networkResponse.getMessage();
                    message.setType(Message.MessageType.RECEIVER);
                    messagesAdapter.addMessage(message);
                }
            }

            @Override
            public void onFailure(@NonNull Call<NetworkResponse> call, @NonNull Throwable t) {
                waitingMessage.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
