package com.example.v3.member.chat;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class ChatItem {

    long id;
    Bitmap trainerImg;
    String trainerName;
    String trainerSex;
    String trainerHistoryPeriod;

    public ChatItem(long id, Bitmap trainerImg, String trainerName, String trainerSex, String trainerHistoryPeriod) {
        this.id = id;
        this.trainerImg = trainerImg;
        this.trainerName = trainerName;
        this.trainerSex = trainerSex;
        this.trainerHistoryPeriod = trainerHistoryPeriod;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Bitmap getTrainerImg() {
        return trainerImg;
    }

    public void setTrainerImg(Bitmap trainerImg) {
        this.trainerImg = trainerImg;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public String getTrainerSex() {
        return trainerSex;
    }

    public void setTrainerSex(String trainerSex) {
        this.trainerSex = trainerSex;
    }

    public String getTrainerHistoryPeriod() {
        return trainerHistoryPeriod;
    }

    public void setTrainerHistoryPeriod(String trainerHistoryPeriod) {
        this.trainerHistoryPeriod = trainerHistoryPeriod;
    }
}
