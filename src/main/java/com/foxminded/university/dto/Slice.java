package com.foxminded.university.dto;

import java.util.List;
import java.util.Objects;

public class Slice<T> {

    private List<T> items;

    public Slice(List<T> items) {
        this.items = items;
    }

    public Slice() {
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slice slice = (Slice) o;
        return items.equals(slice.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }
}
