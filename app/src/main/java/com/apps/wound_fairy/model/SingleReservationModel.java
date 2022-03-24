package com.apps.wound_fairy.model;

import java.io.Serializable;

public class SingleReservationModel  extends StatusResponse implements Serializable {
    private ReservationModel data;

    public ReservationModel getData() {
        return data;
    }
}
