package com.apps.wound_fairy.model;

import java.io.Serializable;

public class UserModel extends StatusResponse {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable {
        private User user;
        private String access_token;
        private String token_type;
        private static String firebase_token;


        public User getUser() {
            return user;
        }

        public String getAccess_token() {
            return access_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public static String getFirebase_token() {
            return firebase_token;
        }

        public static void setFirebase_token(String firebase_token) {
            Data.firebase_token = firebase_token;
        }

        public static class User implements Serializable{
            private String id;
            private String name;
            private String phone_code;
            private String phone;
            private String email;
            private String image;

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getPhone_code() {
                return phone_code;
            }

            public String getPhone() {
                return phone;
            }

            public String getEmail() {
                return email;
            }

            public String getImage() {
                return image;
            }
        }
    }

}
