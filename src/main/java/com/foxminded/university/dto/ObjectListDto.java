package com.foxminded.university.dto;

import java.util.List;

public class ObjectListDto {

    private List items;

    public ObjectListDto(List items) {
        this.items = items;
    }

    public ObjectListDto() {
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }
}
