package org.rosetta.todoweb.domain;

public class Todo {
    public static final int NOT_YET = -1;
    public static final int NEW = 0;
    public static final int DONE = 1;

    private Integer id;
    private String task;
    private Integer status;

    public Todo() { }

    public Todo(String task) {
        this.task = task;
        this.id = NOT_YET;
        this.status = NOT_YET;
    }

    public Todo(Integer id, String task, Integer status) {
        this.id = id;
        this.task = task;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
