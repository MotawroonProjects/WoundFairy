package com.apps.wound_fairy.model;

import java.io.Serializable;

public class BlogModel implements Serializable {
    private String id;
    private String date_time;
    private String title;
    private String details;
    private String image;
    private String video;

    public String getId() {
        return id;
    }

    public String getDate_time() {
        return date_time;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public String getImage() {
        return image;
    }

    public String getVideo() {
        return video;
    }
}
