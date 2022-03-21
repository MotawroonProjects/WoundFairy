package com.apps.wound_fairy.model;

import java.io.Serializable;
import java.util.List;

public class SliderProductModel extends StatusResponse implements Serializable {
    private List<ProductModel.Product.Images> data;

    public List<ProductModel.Product.Images> getData() {
        return data;
    }
}
