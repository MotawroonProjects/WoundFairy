package com.apps.wound_fairy.model;

import android.media.Image;

import java.io.Serializable;
import java.util.List;

public class ProductModel extends StatusResponse implements Serializable {
    private List<Product> data;

    public List<Product> getData() {
        return data;
    }

    public class Product implements Serializable{
        private String id;
        private String title;
        private String details;
        private String image;
        private String price;
        private List<Images> images;


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

        public String getPrice() {
            return price;
        }

        public List<Images> getImages() {
            return images;
        }

        public class Images implements Serializable{
            private String image;

            public String getImage() {
                return image;
            }
        }
    }

}
