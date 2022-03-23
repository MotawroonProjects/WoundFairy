package com.apps.wound_fairy.model;

import java.io.Serializable;
import java.util.List;

public class ReservationDataModel extends StatusResponse implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data implements Serializable{
        private List<ReservationModel> current;
        private List<ReservationModel> previous;

        public List<ReservationModel> getCurrent() {
            return current;
        }

        public List<ReservationModel> getPrevious() {
            return previous;
        }
    }

}
