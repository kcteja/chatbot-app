package com.example.test.chatbot;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.chatbot.accessors.MessagesDataAccessor;
import com.example.test.chatbot.adapters.MessagesAdapter;
import com.example.test.chatbot.models.Message;
import com.example.test.chatbot.models.NetworkResponse;
import com.example.test.chatbot.network.MessageServiceInterface;
import com.example.test.chatbot.network.RetrofitClientInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static int CURRENT_SCREEN = 1; // 1 - Default Chat, 2 - Chat Space A, 3 - Chat Space B

    private MessagesAdapter messagesAdapter;
    private RecyclerView messagesList;
    private ImageView send;
    private EditText enteredMsg;
    private TextView waitingMessage;
    private MessagesDataAccessor messagesDataAccessor;
    private ArrayList<Message> allMessages;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.app_name) + " - " + getString(R.string.default_chat));
        messagesDataAccessor = new MessagesDataAccessor(this);

        drawerLayout = findViewById(R.id.main_drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.left_nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String title = "";
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.default_chat:
                        CURRENT_SCREEN = 1;
                        title = getString(R.string.default_chat);
                        break;
                    case R.id.chat_space_a:
                        CURRENT_SCREEN = 2;
                        title = getString(R.string.chat_space_a);
                        break;
                    case R.id.chat_space_b:
                        CURRENT_SCREEN = 3;
                        title = getString(R.string.chat_space_b);
                        break;
                }

                title = getString(R.string.app_name) + " - " + title;
                setTitle(title);
                allMessages = messagesDataAccessor.getAllMessages(CURRENT_SCREEN);
                messagesAdapter.setNewList(allMessages);
                drawerLayout.closeDrawer(navigationView);
                return true;

            }
        });

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
                    messagesDataAccessor.insert(CURRENT_SCREEN, message);
                    sendMessageToServer(msgText);
                }
            }
        });

        allMessages = messagesDataAccessor.getAllMessages(CURRENT_SCREEN);
        messagesList = findViewById(R.id.messages_list);
        messagesAdapter = new MessagesAdapter(messagesList, this, allMessages);
        messagesList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        messagesList.setAdapter(messagesAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
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
                    message.setType(Message.MessageType.RECEIVER.toString());
                    messagesAdapter.addMessage(message);
                    messagesDataAccessor.insert(CURRENT_SCREEN, message);
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
