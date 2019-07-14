package com.fafu.app.dfb.data;


import androidx.annotation.NonNull;

import java.util.List;

public class DFInfo {
    private String id;
    private String name;
    private int next;
    private List<DFInfo> data;

    public DFInfo() {
    }

    public DFInfo(String id, String name, int next, List<DFInfo> data) {
        this.id = id;
        this.name = name;
        this.next = next;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public List<DFInfo> getData() {
        return data;
    }

    public void setData(List<DFInfo> data) {
        this.data = data;
    }

    @Override
    @NonNull
    public String toString() {
        return "DFInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", next=" + next +
                ", data=" + data +
                '}';
    }
}