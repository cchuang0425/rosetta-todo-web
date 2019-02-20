package org.rosetta.todoweb.service;

import java.util.List;

import org.rosetta.todoweb.domain.Todo;

public interface TodoService {
    List<Todo> getNewTasks();

    boolean saveTask(Todo task);

    boolean checkId(Integer id);

    boolean updateTask(Todo task);

    boolean updateDone(Integer id);
}
