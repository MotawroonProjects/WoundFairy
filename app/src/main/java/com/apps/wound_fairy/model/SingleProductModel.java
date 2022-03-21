package com.apps.wound_fairy.model;

import java.io.Serializable;

public class SingleProductModel extends StatusResponse implements Serializable {
    private ProductModel.Product data;

    public ProductModel.Product getData() {
        return data;
    }
}
