package com.example.deportes2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<ChatMessage> messageList;
    private String currentUserId;

    public MessageAdapter(List<ChatMessage> messages, String currentUserId) {
        this.messageList = messages;
        this.currentUserId = currentUserId;
    }

    public void updateMessages(List<ChatMessage> newMessages) {
        this.messageList = newMessages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getSenderId().equals(currentUserId) ? 1 : 0;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType == 1 ? R.layout.item_message_sent : R.layout.item_message_received, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        ChatMessage msg = messageList.get(position);
        holder.messageText.setText(msg.getMessage());
        holder.timestamp.setText(msg.getCreatedAt().substring(11, 16)); // HH:mm

        if (getItemViewType(position) == 1) {
            // Only sender side shows seen tick
            holder.seenTick.setVisibility(View.VISIBLE);
            holder.seenTick.setImageResource(
                    msg.isSeen() ? R.drawable.ic_tick_blue : R.drawable.ic_tick_grey
            );
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, timestamp;
        ImageView seenTick;

        public MessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            timestamp = itemView.findViewById(R.id.messageTime);
            seenTick = itemView.findViewById(R.id.tickIcon); // Only present in sent layout
        }
    }
}
