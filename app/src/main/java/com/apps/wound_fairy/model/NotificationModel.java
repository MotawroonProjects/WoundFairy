package com.apps.wound_fairy.model;

import java.io.Serializable;

public class NotificationModel implements Serializable {
    private String id;
    private String title;
    private String text;
    private String date;
    private String time;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
