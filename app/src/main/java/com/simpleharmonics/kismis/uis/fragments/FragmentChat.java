package com.simpleharmonics.kismis.uis.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.simpleharmonics.kismis.R;
import com.simpleharmonics.kismis.adapters.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentChat extends Fragment {

    private static final String TAG = "FragmentChatTAG";

    private RecyclerView rvChats;
    private ChatAdapter chatAdapter;
    private ContentLoadingProgressBar pbProgress;
    private final List<ChatAdapter.CustomChat> customChatList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_chat, container, false);

        FragmentActivity fragmentActivity = FragmentChat.this.getActivity();
        if (fragmentActivity == null) {
            Log.e(TAG, "Kismis: onCreateView: fragmentActivity is null");
        } else {
            final Toolbar toolbar = fragment.findViewById(R.id.skw);
            ((AppCompatActivity) fragmentActivity).setSupportActionBar(toolbar);
            ActionBar actionBar = ((AppCompatActivity) fragmentActivity).getSupportActionBar();
            if (actionBar == null) {
                Log.e(TAG, "Kismis: onCreate: actionBar is null");
            } else {
                actionBar.setTitle("Direct Messages");
            }

            setHasOptionsMenu(true);

            rvChats = fragment.findViewById(R.id.bsa);
            rvChats.setLayoutManager(new LinearLayoutManager(fragmentActivity));
            rvChats.setHasFixedSize(true);
            pbProgress = fragment.findViewById(R.id.ckh);

            chatAdapter = new ChatAdapter(fragmentActivity, customChatList);
            rvChats.setAdapter(chatAdapter);

            new Handler().postDelayed(this::getChatList, 200);
        }
        return fragment;
    }

    private void getChatList() {
        new Handler().postDelayed(() -> {
            pbProgress.hide();
            FragmentActivity fragmentActivity = FragmentChat.this.getActivity();
            if (fragmentActivity == null) {
                Log.e(TAG, "Kismis: getChatList: fragmentActivity is null");
            } else {
                for (int i = 0; i < 20; i++) {
                    ChatAdapter.CustomChat customChat = new ChatAdapter.CustomChat("", "", "War lord", "4:34PM", "There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable.");
                    customChatList.add(customChat);
                }
                chatAdapter = new ChatAdapter(fragmentActivity, customChatList);
                rvChats.setAdapter(chatAdapter);
            }
        }, 400);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        FragmentActivity fragmentActivity = FragmentChat.this.getActivity();
        if (fragmentActivity == null) {
            Log.e(TAG, "Kismis: onCreateOptionsMenu: fragmentActivity is null");
        } else {
            fragmentActivity.getMenuInflater().inflate(R.menu.menu_chat, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        FragmentActivity fragmentActivity = FragmentChat.this.getActivity();
        if (fragmentActivity == null) {
            Log.e(TAG, "Kismis: onOptionsItemSelected: fragmentActivity is null");
        } else {
            if (item.getItemId() == R.id.uxb) {

            } else {
                Log.e(TAG, "Kismis: onOptionsItemSelected: Unknown item id: " + item.getItemId());
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
