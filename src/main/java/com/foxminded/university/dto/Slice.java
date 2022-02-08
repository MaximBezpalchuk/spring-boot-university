package com.foxminded.university.dto;

import java.util.List;

public class Slice {

    private List items;

    public Slice(List items) {
        this.items = items;
    }

    public Slice() {
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }
}
