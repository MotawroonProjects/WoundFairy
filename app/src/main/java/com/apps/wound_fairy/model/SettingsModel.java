package com.apps.wound_fairy.model;

import android.media.audiofx.Equalizer;

import java.io.Serializable;

public class SettingsModel extends StatusResponse implements Serializable {
    private Settings data;

    public Settings getData() {
        return data;
    }
    public static class Settings implements Serializable{
        private String terms;
        private String privacy;
        private String facebook;
        private String insta;
        private String twitter;
        private String linkedin;
        private String online_price;
        private String home_visit_price;

        public String getTerms() {
            return terms;
        }

        public String getPrivacy() {
            return privacy;
        }

        public String getFacebook() {
            return facebook;
        }

        public String getInsta() {
            return insta;
        }

        public String getTwitter() {
            return twitter;
        }

        public String getLinkedin() {
            return linkedin;
        }

        public String getOnline_price() {
            return online_price;
        }

        public String getHome_visit_price() {
            return home_visit_price;
        }
    }

}
