package com.apps.wound_fairy.model;

import java.io.Serializable;

public class VisitOnlineModel extends StatusResponse implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }
    public  class Data implements Serializable{
        private String id;
        private String title;
        private String details;
        private String image;

        public String getId() {
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
