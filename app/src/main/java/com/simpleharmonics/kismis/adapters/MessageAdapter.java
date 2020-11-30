package com.simpleharmonics.kismis.adapters;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simpleharmonics.kismis.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.CustomViewHolder> {

    private final Context context;
    private final List<CustomMessage> customMessageList;

    public MessageAdapter(Context context, List<CustomMessage> customMessageList) {
        this.context = context;
        this.customMessageList = customMessageList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int position) {
        boolean notFirstMessage = true, lastMessageWasReceived = false;
        if (position <= 0) {
            notFirstMessage = false;
        }

        if (notFirstMessage) {
            CustomMessage customMessageLast = customMessageList.get(position - 1);
            lastMessageWasReceived = customMessageLast.isReceived();
        }

        CustomMessage customMessage = customMessageList.get(position);
        TextView tvMessage = customViewHolder.itemView.findViewById(R.id.xrg);
        tvMessage.setText(customMessage.getMessage());

        TextView tvTime = customViewHolder.itemView.findViewById(R.id.tvf);
        tvTime.setText(customMessage.getTime());

        RelativeLayout rlContainer = customViewHolder.itemView.findViewById(R.id.isd);
        LinearLayout llMessageBubble = customViewHolder.itemView.findViewById(R.id.vyu);
        RelativeLayout.LayoutParams layoutParamsBubble = (RelativeLayout.LayoutParams) llMessageBubble.getLayoutParams();
        if (customMessage.isReceived()) {
            rlContainer.setGravity(Gravity.START);
            if (notFirstMessage) {
                if (lastMessageWasReceived) {
                    layoutParamsBubble.setMargins(0, 10, 100, 0);
                    llMessageBubble.setBackground(context.getDrawable(R.drawable.drawable_message_received_bubble_round));
                } else {
                    layoutParamsBubble.setMargins(0, 20, 100, 0);
                    llMessageBubble.setBackground(context.getDrawable(R.drawable.drawable_message_received_bubble));
                }
            } else {
                layoutParamsBubble.setMargins(0, 20, 100, 0);
                llMessageBubble.setBackground(context.getDrawable(R.drawable.drawable_message_received_bubble));
            }
        } else {
            rlContainer.setGravity(Gravity.END);
            if (notFirstMessage) {
                if (lastMessageWasReceived) {
                    layoutParamsBubble.setMargins(100, 20, 0, 0);
                    llMessageBubble.setBackground(context.getDrawable(R.drawable.drawable_message_sent_bubble));
                } else {
                    layoutParamsBubble.setMargins(100, 10, 0, 0);
                    llMessageBubble.setBackground(context.getDrawable(R.drawable.drawable_message_sent_bubble_round));
                }
            } else {
                layoutParamsBubble.setMargins(100, 20, 0, 0);
                llMessageBubble.setBackground(context.getDrawable(R.drawable.drawable_message_sent_bubble));
            }
        }
        llMessageBubble.setLayoutParams(layoutParamsBubble);
    }

    @Override
    public int getItemCount() {
        return customMessageList.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class CustomMessage {

        private String message;
        private String time;
        private boolean isReceived;

        public CustomMessage(String message, String time, boolean isReceived) {
            this.message = message;
            this.time = time;
            this.isReceived = isReceived;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public boolean isReceived() {
            return isReceived;
        }

        public void setReceived(boolean received) {
            isReceived = received;
        }
    }
}
