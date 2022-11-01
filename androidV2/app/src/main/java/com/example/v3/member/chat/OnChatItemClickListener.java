package com.example.v3.member.chat;

import android.view.View;

public interface OnChatItemClickListener {
    public void onItemClick(ChatAdapter.ViewHolder holder, View view, int position, String id);
}
