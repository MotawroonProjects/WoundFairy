package com.apps.wound_fairy.model;

import java.io.Serializable;

public class SingleMessageModel extends StatusResponse implements Serializable {
    private MessageModel data;

    public MessageModel getData() {
        return data;
    }
}
