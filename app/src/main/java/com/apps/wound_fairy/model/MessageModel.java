package com.apps.wound_fairy.model;

import java.io.Serializable;

public class MessageModel implements Serializable {
    private String id;
    private String from_user_id;
    private String to_user_id;
    private String date;
    private String time;
    private String text;
    private String image;
    private String type;

    public MessageModel(String id, String from_user_id, String to_user_id, String date, String time, String text, String image, String type) {
        this.id = id;
        this.from_user_id = from_user_id;
        this.to_user_id = to_user_id;
        this.date = date;
        this.time = time;
        this.text = text;
        this.image = image;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getFrom_user_id() {
        return from_user_id;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }

    public String getType() {
        return type;
    }
}
