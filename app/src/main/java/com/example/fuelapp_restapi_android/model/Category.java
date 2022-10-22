package com.example.fuelapp_restapi_android.model;

public class Category {
    int id;
    String _id;
    String category;

    public Category(int id, String _id, String category) {
        this.id = id;
        this._id = _id;
        this.category = category;
    }
    public Category() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
