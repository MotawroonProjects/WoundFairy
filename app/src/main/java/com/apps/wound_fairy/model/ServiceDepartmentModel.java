package com.apps.wound_fairy.model;

import java.io.Serializable;
import java.util.List;

public class ServiceDepartmentModel extends StatusResponse implements Serializable {
    private List<Department> data;

    public List<Department> getData() {
        return data;
    }

    public static class Department implements Serializable{
        private String id;
        private String title;

        public Department(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
    }
}
