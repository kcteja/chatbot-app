package com.example.test.chatbot.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.test.chatbot.R;
import com.example.test.chatbot.models.Message;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesItemViewHolder> {

    private RecyclerView messagesList;
    private Context mContext;
    private ArrayList<Message> messages;

    public MessagesAdapter(RecyclerView messagesList, Context context, ArrayList<Message> allMessages) {
        this.messagesList = messagesList;
        this.mContext = context;
        this.messages = allMessages;
    }

    @NonNull
    @Override
    public MessagesItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.message_item_layout, viewGroup, false);
        return new MessagesItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesItemViewHolder holder, int pos) {
        Message message = messages.get(pos);
        switch (message.getType()) {
            case SENDER:
                holder.senderMessage.setText(message.getMessage());
                holder.senderMessage.setVisibility(View.VISIBLE);
                holder.receiverMessage.setVisibility(View.GONE);
                break;
            case RECEIVER:
                holder.receiverMessage.setText(message.getMessage());
                holder.receiverMessage.setVisibility(View.VISIBLE);
                holder.senderMessage.setVisibility(View.GONE);
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setNewList(ArrayList<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyDataSetChanged();
        scrollToBottom();
    }

    public void scrollToBottom() {
        try {
            messagesList.smoothScrollToPosition(messages.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MessagesItemViewHolder extends RecyclerView.ViewHolder {

        TextView senderMessage;
        TextView receiverMessage;

        MessagesItemViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessage = itemView.findViewById(R.id.sender_message_text);
            receiverMessage = itemView.findViewById(R.id.receiver_message_text);
        }
    }
}
