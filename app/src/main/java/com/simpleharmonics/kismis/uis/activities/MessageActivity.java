package com.simpleharmonics.kismis.uis.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.adapters.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivityTAG";
    private RecyclerView rvMessages;
    private MessageAdapter messageAdapter;
    private final List<MessageAdapter.CustomMessage> customMessageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        final Toolbar toolbar = findViewById(R.id.jie);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            Log.e(TAG, "Kismis: onCreate: actionBar is null");
        } else {
            actionBar.setHomeAsUpIndicator(getDrawable(R.drawable.ic_arrow_back_white_24));
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());

            actionBar.setTitle("Alan Walker");
        }

        rvMessages = findViewById(R.id.wop);
        rvMessages.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
        rvMessages.setHasFixedSize(true);

        messageAdapter = new MessageAdapter(MessageActivity.this, customMessageList);
        rvMessages.setAdapter(messageAdapter);

        getMessages();

        final EditText edtMessage = findViewById(R.id.fgc);
        ImageButton ibSend = findViewById(R.id.vps);
        ibSend.setOnClickListener(v -> {
            MessageAdapter.CustomMessage customMessage = new MessageAdapter.CustomMessage(edtMessage.getText().toString(), "12:45PM", false);
            customMessageList.add(customMessage);

            edtMessage.setText(null);
            messageAdapter.notifyDataSetChanged();

            scrollToBottom();
        });
    }

    private void scrollToBottom() {
        rvMessages.scrollToPosition(customMessageList.size() - 1);
    }

    private void getMessages() {
        for (int i = 0; i < 100; i++) {
            MessageAdapter.CustomMessage customMessage = new MessageAdapter.CustomMessage("There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable." + i, "12:45PM", i % 2 == 0);
            customMessageList.add(customMessage);
        }

        messageAdapter = new MessageAdapter(MessageActivity.this, customMessageList);
        rvMessages.setAdapter(messageAdapter);
        scrollToBottom();
    }
}