package com.testapp.models;

public class RecyclerItem {

    private String text;

    public RecyclerItem(String title) {
        this.text = title;

    }

    public String getTitle() {
        return text;
    }

    public void setTitle(String title) {
        this.text = title;
    }
}
