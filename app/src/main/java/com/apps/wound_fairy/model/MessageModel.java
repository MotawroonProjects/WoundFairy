package com.apps.wound_fairy.model;

import java.io.Serializable;

public class MessageModel implements Serializable {
    private String id;
    private String order_id;
    private String from_user_id;
    private String to_user_id;
    private String type;
    private String message;
    private String voice;
    private String image;
    private String is_read;
    private String created_at;
    private String updated_at;
    private UserModel.Data from_user;
    private UserModel.Data to_user;

    public MessageModel() {
    }

    public MessageModel(String id, String order_id, String from_user_id, String to_user_id, String type, String message, String voice, String image, String is_read, String created_at, String updated_at, UserModel.Data from_user, UserModel.Data to_user) {
        this.id = id;
        this.order_id = order_id;
        this.from_user_id = from_user_id;
        this.to_user_id = to_user_id;
        this.type = type;
        this.message = message;
        this.voice = voice;
        this.image = image;
        this.is_read = is_read;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.from_user = from_user;
        this.to_user = to_user;
    }

    public String getId() {
        return id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getFrom_user_id() {
        return from_user_id;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getVoice() {
        return voice;
    }

    public String getImage() {
        return image;
    }

    public String getIs_read() {
        return is_read;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public UserModel.Data getFrom_user() {
        return from_user;
    }

    public UserModel.Data getTo_user() {
        return to_user;
    }
}
