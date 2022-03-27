package com.apps.wound_fairy.model;

import java.io.Serializable;
import java.util.List;

public class PaymentDataModel extends StatusResponse implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data implements Serializable{
      private String link;

        public String getLink() {
            return link;
        }
    }
}
