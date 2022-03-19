package com.apps.wound_fairy.model;

import java.io.Serializable;

public class AboutAusModel extends StatusResponse implements Serializable {
    private About data;

    public About getData() {
        return data;
    }

    public class About implements Serializable {
        private int id;
        private String title;
        private String details;
        private String image;

        public int getId() {
            return id;
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
    }
}
