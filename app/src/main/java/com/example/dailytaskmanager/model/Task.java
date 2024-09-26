package com.example.dailytaskmanager.model;

public class Task {
    private String name;
    private String time;

    public Task() {

    }

    public Task(String time, String name) {
        this.time = time;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
