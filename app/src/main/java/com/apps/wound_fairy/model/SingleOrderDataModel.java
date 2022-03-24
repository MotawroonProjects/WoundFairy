package com.apps.wound_fairy.model;

import java.io.Serializable;
import java.util.List;

public class SingleOrderDataModel extends StatusResponse implements Serializable {
    private OrderModel data;

    public OrderModel getData() {
        return data;
    }


}
