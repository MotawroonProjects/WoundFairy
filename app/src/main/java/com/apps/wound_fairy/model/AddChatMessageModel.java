package com.apps.wound_fairy.model;

import java.io.Serializable;

public class AddChatMessageModel implements Serializable {
    private String order_id;
    private String from_user_id;
    private String to_user_id;
    private String type;
    private String message ="";
    private String image;

    public AddChatMessageModel(String order_id, String from_user_id, String to_user_id, String type, String message, String image) {
        this.order_id = order_id;
        this.from_user_id = from_user_id;
        this.to_user_id = to_user_id;
        this.type = type;
        this.message = message;
        this.image = image;
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

    public String getImage() {
        return image;
    }
}
