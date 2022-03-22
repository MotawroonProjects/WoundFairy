package com.apps.wound_fairy.model;

import java.io.Serializable;
import java.util.List;

public class CurrentReservationModel extends StatusResponse implements Serializable {
    private List<ReservationModel> data;

    public List<ReservationModel> getData() {
        return data;
    }
}
