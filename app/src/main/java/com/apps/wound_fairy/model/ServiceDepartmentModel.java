package com.apps.wound_fairy.model;

import java.io.Serializable;

public class ServiceDepartmentModel extends StatusResponse implements Serializable {
    private Department data;

    public Department getData() {
        return data;
    }

    public class Department implements Serializable{
        private String id;
        private String title;

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }
}
