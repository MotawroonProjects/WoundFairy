package com.apps.wound_fairy.model;

import java.io.Serializable;

public class AddChatMessageModel implements Serializable {

    private String type;
    private String text = "";
    private String image;
    private String auth;

    public AddChatMessageModel(String type, String text, String image, String auth) {
        this.type = type;
        this.text = text;
        this.image = image;
        this.auth = auth;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }

    public String getAuth() {
        return auth;
    }
}
