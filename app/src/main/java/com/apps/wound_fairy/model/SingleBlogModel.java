package com.apps.wound_fairy.model;

import java.io.Serializable;

public class SingleBlogModel extends StatusResponse implements Serializable {
    private BlogModel data;

    public BlogModel getData() {
        return data;
    }
}
